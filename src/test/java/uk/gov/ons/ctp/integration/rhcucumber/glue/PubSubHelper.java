package uk.gov.ons.ctp.integration.rhcucumber.glue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.stub.GrpcSubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStubSettings;
import com.google.pubsub.v1.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.threeten.bp.Duration;
import uk.gov.ons.ctp.common.domain.Channel;
import uk.gov.ons.ctp.common.domain.Source;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.common.event.EventPublisher;
import uk.gov.ons.ctp.common.event.EventTopic;
import uk.gov.ons.ctp.common.event.EventType;
import uk.gov.ons.ctp.common.event.model.EventPayload;

import java.io.IOException;
import java.util.Collections;

import static uk.gov.ons.ctp.common.log.ScopedStructuredArguments.kv;

@Slf4j public class PubSubHelper {

    private static PubSubHelper instance = null;
    private final static long DEFAULT_TIMEOUT_MS = 3000;

    private EventPublisher eventPublisher;
    private String projectId;

    private ObjectMapper mapper = new ObjectMapper();

    private String emulatorPubSubHost;
    private boolean useEmulatorPubSub;

    private SubscriberStubSettings defaultSubscriberStubSettings;

    private PubSubHelper(String projectId, boolean addRmProperties, boolean useEmulatorPubSub, String emulatorPubSubHost) throws CTPException {
        try {
            this.useEmulatorPubSub = useEmulatorPubSub;
            this.emulatorPubSubHost = emulatorPubSubHost;
            NativePubSubEventSender sender = new NativePubSubEventSender(projectId, addRmProperties);
            eventPublisher = EventPublisher.createWithoutEventPersistence(sender);

            defaultSubscriberStubSettings = buildSubscriberStubSettings(useEmulatorPubSub, emulatorPubSubHost);

            this.projectId = projectId;
        } catch (IOException e) {
            String errorMessage = "Failed to create subscription";
            log.error(errorMessage, e);
            throw new CTPException(CTPException.Fault.SYSTEM_ERROR, e, errorMessage);
        }
    }

    public static synchronized PubSubHelper instance(String projectId, boolean addRmProperties,
        boolean useEmulatorPubSub, String emulatorPubSubHost) throws CTPException {

        if (instance == null) {
            instance = new PubSubHelper(projectId, addRmProperties, useEmulatorPubSub, emulatorPubSubHost);
        }
        return instance;
    }

    public synchronized String createSubscription(EventType eventType) throws CTPException {
        EventTopic eventTopic = EventTopic.forType(eventType);
        String subscriptionId = buildSubscriberId(eventType);
        try {
            SubscriberStub subscriberStub = GrpcSubscriberStub.create(defaultSubscriberStubSettings);
            SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create(subscriberStub);

            verifyAndCreateSubscription(subscriptionAdminClient, projectId, eventTopic.getTopic(), subscriptionId);
            return subscriptionId;
        } catch (IOException e) {
            String errorMessage = "Failed to create subscription";
            log.error(errorMessage, e);
            throw new CTPException(CTPException.Fault.SYSTEM_ERROR, e, errorMessage);
        }
    }

    public synchronized void flushTopic(EventType eventType) throws CTPException {
        deleteSubscription(eventType);
        createSubscription(eventType);
    }

    /**
     * Publish a message to a pubsub topic.
     *
     * @param eventType is the type of the event that is being sent.
     * @param source    states who is sending, or pretending, to set the message.
     * @param channel   holds a channel identifier.
     * @param payload   is the object to be sent.
     * @return the transaction id generated for the published message.
     * @throws CTPException if anything went wrong.
     */
    public synchronized String sendEvent(EventType eventType, Source source, Channel channel, EventPayload payload) throws CTPException {
        try {
            String transactionId = eventPublisher.sendEvent(eventType, source, channel, payload);
            return transactionId;

        } catch (Exception e) {
            String errorMessage = "Failed to send message. Cause: " + e.getMessage();
            log.error(errorMessage, kv("eventType", eventType), kv("source", source),
                kv("channel", channel), e);
            throw new CTPException(CTPException.Fault.SYSTEM_ERROR, errorMessage, e);
        }
    }

    public synchronized String deleteSubscription(EventType eventType) throws CTPException {
        EventTopic eventTopic = EventTopic.forType(eventType);
        String subscriptionId = buildSubscriberId(eventType);
        deleteSubscription(subscriptionId);
        return subscriptionId;
    }

    private void deleteSubscription(String subscriptionId) throws CTPException {
        try {
            SubscriberStub subscriberStub = GrpcSubscriberStub.create(defaultSubscriberStubSettings);
            SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create(subscriberStub);

            if (subscriptionExists(subscriptionAdminClient, projectId, subscriptionId)) {
                ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);
                subscriptionAdminClient.deleteSubscription(subscriptionName);
            }
        } catch (IOException e) {
            String errorMessage = "Failed to delete subscription";
            log.error(errorMessage, e);
            throw new CTPException(CTPException.Fault.SYSTEM_ERROR, e, errorMessage);
        }
    }

    public static void verifyAndCreateSubscription(SubscriptionAdminClient subscriptionAdminClient,
        String projectId, String topic, String subscriptionId) throws IOException {
        if (!subscriptionExists(subscriptionAdminClient, projectId, subscriptionId)) {
            TopicName topicName = TopicName.of(projectId, topic);
            ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);
            subscriptionAdminClient.createSubscription(subscriptionName, topicName, PushConfig.getDefaultInstance(), 10);
        }
    }

    private static boolean subscriptionExists(SubscriptionAdminClient subscriptionAdminClient, String projectId, String subscriptionId) {
        ProjectName pn = ProjectName.of(projectId);
        SubscriptionAdminClient.ListSubscriptionsPagedResponse listSubscriptionsPagedResponse = subscriptionAdminClient.listSubscriptions(pn);
        boolean exists = false;
        for (Subscription subscription : listSubscriptionsPagedResponse.iterateAll()) {
            if (subscription.getName().endsWith("/" + subscriptionId)) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    /**
     * Reads a message from the named queue and convert it to a Java object. This method will wait for
     * up to the specified number of milliseconds for a message to appear on the queue.
     *
     * @param <T>               is the class of object we are expected to recieve.
     * @param eventType         is the name of the queue to read from.
     * @param clazz             is the class that the message should be converted to.
     * @param maxWaitTimeMillis is the maximum amount of time the caller is prepared to wait for the
     *                          message to appear.
     * @return an object of the specified type, or null if no message was found before the timeout
     * expired.
     * @throws CTPException if Rabbit threw an exception when we attempted to read a message.
     */

    public <T> T getMessage(EventType eventType, Class<T> clazz, long maxWaitTimeMillis)  throws CTPException {
        String subscriberName = buildSubscriberId(eventType);

        String message = getMessage(subscriberName,
            maxWaitTimeMillis);

        // Return to caller if nothing read from queue
        if (message == null) {
            log.info("PubSub getMessage. Message is null. Unable to convert to class '" + clazz.getName()
                + "'");
            return null;
        }
        // Use Jackson to convert from a Json message to a Java object
        try {
            log.info("Rabbit getMessage. Converting result into class '" + clazz.getName() + "'");
            return mapper.readValue(message, clazz);

        } catch (IOException e) {
            String errorMessage =
                "Failed to convert message to object of type '" + clazz.getName() + "'";
            log.error(errorMessage, kv("subscription", subscriberName), e);
            throw new CTPException(CTPException.Fault.SYSTEM_ERROR, e, errorMessage);
        }
    }

    /**
     * Reads a message from the named queue. This method will wait for up to the specified number of
     * milliseconds for a message to appear on the queue.
     *
     * @param subscription      is the name of the queue to read from.
     * @param maxWaitTimeMillis is the maximum amount of time the caller is prepared to wait for the
     *                          message to appear.
     * @return a String containing the content of the message body, or null if no message was found
     * before the timeout expired.
     * @throws CTPException if Rabbit threw an exception when we attempted to read a message.
     */
    public String getMessage(String subscription, long maxWaitTimeMillis) throws CTPException {
        final long startTime = System.currentTimeMillis();
        final long timeoutLimit = startTime + maxWaitTimeMillis;

        log.info("PubSub getMessage. Reading from subscription '" + subscription + "'" + " within "
            + maxWaitTimeMillis + "ms");

        // Keep trying to read a message from pubsub, or we timeout waiting
        String messageBody;
        do {
            messageBody = retrieveMessage(subscription, maxWaitTimeMillis);
            if (messageBody != null) {
                log.info("Message read from queue");
                break;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                break;
            }
        } while (messageBody == null && System.currentTimeMillis() < timeoutLimit);

        return messageBody;
    }

    private synchronized String retrieveMessage(String subscription, long wait) throws CTPException {
        try {
            SubscriberStubSettings subscriberStubSettings = buildSubscriberStubSettings(useEmulatorPubSub, emulatorPubSubHost, wait);
            String result = syncPullMessage(subscriberStubSettings, projectId, subscription);
            return result == null ? null : new String(result);
        } catch (IOException e) {
            String errorMessage = "Failed to create subscription";
            log.error(errorMessage, e);
            throw new CTPException(CTPException.Fault.SYSTEM_ERROR, e, errorMessage);
        }
    }

    // ref: https://cloud.google.com/pubsub/docs/pull#synchronous_pull
    private static String syncPullMessage(SubscriberStubSettings subscriberStubSettings, String projectId, String subscription)
        throws CTPException {
        String msg = null;
        try {
            try (SubscriberStub subscriber = GrpcSubscriberStub.create(subscriberStubSettings)) {
                String subscriptionName = ProjectSubscriptionName.format(projectId, subscription);
                PullRequest pullRequest = PullRequest.newBuilder().setMaxMessages(1).setSubscription(subscriptionName).build();

                // Use pullCallable().futureCall to asynchronously perform this operation.
                PullResponse pullResponse = subscriber.pullCallable().call(pullRequest);

                if (!pullResponse.getReceivedMessagesList().isEmpty()) {
                    ReceivedMessage rm = pullResponse.getReceivedMessagesList().get(0);
                    String ackId = rm.getAckId();
                    msg = rm.getMessage().getData().toString("UTF-8");

                    // Acknowledge received messages.
                    AcknowledgeRequest acknowledgeRequest = AcknowledgeRequest.newBuilder().setSubscription(subscriptionName).addAllAckIds(Collections.singleton(ackId)).build();
                    // Use acknowledgeCallable().futureCall to asynchronously perform this operation.
                    subscriber.acknowledgeCallable().call(acknowledgeRequest);
                }
            }
            return msg;
        } catch (IOException e) {
            String errorMessage = "Failed to flush queue '" + subscription + "'";
            log.error(errorMessage, kv("subscription", subscription), e);
            throw new CTPException(CTPException.Fault.SYSTEM_ERROR, e, errorMessage);
        }
    }

    private String buildSubscriberId(EventType eventType) {
        EventTopic eventTopic = EventTopic.forType(eventType);
        if (eventTopic == null) {
            String errorMessage = "Topic for eventType '" + eventType + "' not configured";
            log.error(errorMessage, kv("eventType", eventType));
            throw new UnsupportedOperationException(errorMessage);
        }

        // Use routing key for queue name as well as binding. This gives the queue a 'fake' name, but
        // it saves the Cucumber tests from having to decide on a queue name
        String eventTopicName = eventTopic.getTopic();
        return eventTopicName + "_cc";
    }

    private static SubscriberStubSettings buildSubscriberStubSettings(boolean isForEmulator, String emulatorPubSubHost, long wait)  throws IOException {
        if (isForEmulator)
            return buildEmulatorSubscriberStubSettings(emulatorPubSubHost, wait);
        else
            return buildCloudSubscriberStubSettings(wait);
    }

    private static SubscriberStubSettings buildSubscriberStubSettings(boolean isForEmulator, String emulatorPubSubHost) throws IOException {
        return buildSubscriberStubSettings(isForEmulator, emulatorPubSubHost, DEFAULT_TIMEOUT_MS);
    }

    private static SubscriberStubSettings buildCloudSubscriberStubSettings(long wait) throws IOException {
        Duration timeout = Duration.ofMillis(wait);

        SubscriberStubSettings.Builder builder = SubscriberStubSettings.newBuilder();
        builder.pullSettings().setSimpleTimeoutNoRetries(timeout);
        SubscriberStubSettings subscriberStubSettings = builder
            .setTransportChannelProvider(
                SubscriberStubSettings.defaultGrpcTransportProviderBuilder()
                    .setKeepAliveTime(Duration.ofSeconds(1)).build()).build();
        return subscriberStubSettings;
    }

    private static SubscriberStubSettings buildEmulatorSubscriberStubSettings(String hostPort, long wait) throws IOException {
        ManagedChannel channel = ManagedChannelBuilder.forTarget(hostPort).usePlaintext().build();
        TransportChannelProvider channelProvider = FixedTransportChannelProvider.create(GrpcTransportChannel.create(channel));
        CredentialsProvider credentialsProvider = NoCredentialsProvider.create();

        Duration timeout = Duration.ofMillis(wait);
        SubscriberStubSettings.Builder builder = SubscriberStubSettings.newBuilder();
        builder.pullSettings().setSimpleTimeoutNoRetries(timeout);
        SubscriberStubSettings subscriberStubSettings =
            builder.setTransportChannelProvider(channelProvider)
                .setCredentialsProvider(credentialsProvider)
                .build();

        return subscriberStubSettings;
    }

    public static void main(String[] args) throws Exception {
        //PubSubHelper.asyncPullMessage("local", "event_case-update_cc");
        //        String msg = PubSubHelper.syncPullMessage("local", "event_case-update_cc");
        //        System.out.println("sync:" + msg);

        PubSubHelper pubsub = PubSubHelper.instance("local", false, true, "localhost:8085");
        String subscriptionId = pubsub.createSubscription(EventType.CASE_UPDATE);
        String msg = pubsub.getMessage(EventType.CASE_UPDATE, String.class, 1000);
        System.out.println("sync:" + msg);

        pubsub.flushTopic(EventType.CASE_UPDATE);
        msg = pubsub.getMessage(EventType.CASE_UPDATE, String.class, 1000);
        System.out.println("sync:" + msg);

    }
}
