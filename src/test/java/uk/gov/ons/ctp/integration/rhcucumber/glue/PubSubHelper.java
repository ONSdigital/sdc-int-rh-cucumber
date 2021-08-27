package uk.gov.ons.ctp.integration.rhcucumber.glue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.stub.GrpcSubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStubSettings;
import com.google.pubsub.v1.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.ons.ctp.common.domain.Channel;
import uk.gov.ons.ctp.common.domain.Source;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.common.event.EventPublisher;
import uk.gov.ons.ctp.common.event.EventTopic;
import uk.gov.ons.ctp.common.event.EventType;
import uk.gov.ons.ctp.common.event.model.CollectionCase;
import uk.gov.ons.ctp.common.event.model.EventPayload;

import java.io.IOException;
import java.util.Collections;

import static uk.gov.ons.ctp.common.log.ScopedStructuredArguments.kv;

@Slf4j public class PubSubHelper {

  private static PubSubHelper instance = null;
  private static ManagedChannel channel;
  private static TransportChannelProvider channelProvider;
  private static CredentialsProvider credentialsProvider;
  private EventPublisher eventPublisher;
  private String projectId;
  private SubscriptionAdminClient subscriptionAdminClient;
  private SubscriberStub subscriber;

  private ObjectMapper mapper = new ObjectMapper();

  private PubSubHelper(String projectId, boolean addRmProperties, boolean useEmulatorPubSub, String emulatorPubSubHost) throws CTPException {
    try {

      System.out.println(emulatorPubSubHost);
      if (useEmulatorPubSub) {
        channel = ManagedChannelBuilder.forTarget(emulatorPubSubHost).usePlaintext().build();
        channelProvider = FixedTransportChannelProvider.create(GrpcTransportChannel.create(channel));
        credentialsProvider = NoCredentialsProvider.create();
      }
      NativePubSubEventSender sender = new NativePubSubEventSender(projectId, addRmProperties, channelProvider, credentialsProvider);
      eventPublisher = EventPublisher.createWithoutEventPersistence(sender);

      SubscriberStubSettings subscriberStubSettings = buildSubscriberStubSettings(useEmulatorPubSub);
      subscriber = GrpcSubscriberStub.create(subscriberStubSettings);

      subscriptionAdminClient = SubscriptionAdminClient.create(subscriber);

      this.projectId = projectId;
    } catch (IOException e) {
      String errorMessage = "Failed to create subscription";
      log.error(errorMessage, e);
      throw new CTPException(CTPException.Fault.SYSTEM_ERROR, e, errorMessage);
    }
  }

  public static synchronized PubSubHelper instance(String projectId, boolean addRmProperties, boolean useEmulatorPubSub, String emulatorPubSubHost)
      throws CTPException {

    if (instance == null) {
      instance = new PubSubHelper(projectId, addRmProperties, useEmulatorPubSub, emulatorPubSubHost);
    }
    return instance;
  }

  public void closeChannel() {
    if(channel != null) channel.shutdown();
  }

  public synchronized String createSubscription(EventType eventType) throws CTPException {
    EventTopic eventTopic = EventTopic.forType(eventType);
    String subscriptionId = buildSubscriberName(eventType);
    try {
      verifyAndCreateSubscription(subscriptionAdminClient, projectId, eventTopic.getTopic(), subscriptionId);
      return subscriptionId;
    } catch (IOException e) {
      String errorMessage = "Failed to create subscription";
      log.error(errorMessage, e);
      throw new CTPException(CTPException.Fault.SYSTEM_ERROR, e, errorMessage);
    }
  }

  public static void verifyAndCreateSubscription(SubscriptionAdminClient subscriptionAdminClient, String projectId, String topic, String subscriptionId) throws IOException {
    if (!subscriptionExists(subscriptionAdminClient, projectId, subscriptionId)) {
      TopicName topicName = TopicName.of(projectId, topic);
      ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);
      // Create a pull subscription with default acknowledgement deadline of 10 seconds.
      // Messages not successfully acknowledged within 10 seconds will get resent by the server.
      Subscription subscription = subscriptionAdminClient
          .createSubscription(subscriptionName, topicName, PushConfig.getDefaultInstance(), 10);
      System.out.println("Created pull subscription: " + subscription.getName());
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

  private String buildSubscriberName(EventType eventType) {
    EventTopic eventTopic = EventTopic.forType(eventType);
    if (eventTopic == null) {
      String errorMessage = "Topic for eventType '" + eventType + "' not configured";
      log.error(errorMessage, kv("eventType", eventType));
      throw new UnsupportedOperationException(errorMessage);
    }

    // Use routing key for queue name as well as binding. This gives the queue a 'fake' name, but
    // it saves the Cucumber tests from having to decide on a queue name
    String eventTopicName = eventTopic.getTopic();
    return eventTopicName + "_rh";
  }

  public void flushTopic(String subscription) {
    boolean isEmpty = false;
    while (!isEmpty) {
      try {
        String msg = getMessageNoWait(subscription);
        isEmpty = true;
      } catch (CTPException e) {
        log.error("Error flushing {}", e, subscription);
        isEmpty = true;
      }
    }
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
  public synchronized String sendEvent(EventType eventType, Source source, Channel channel,
      EventPayload payload) throws CTPException {
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

    log.info("PubSub getMessage. Reading from queue '" + subscription + "'" + " within "
        + maxWaitTimeMillis + "ms");

    // Keep trying to read a message from pubsub, or we timeout waiting
    String messageBody;
    do {
      messageBody = getMessageNoWait(subscription);
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
  public <T> T getMessage(EventType eventType, Class<T> clazz, long maxWaitTimeMillis)
      throws CTPException {
    String subscriberName = buildSubscriberName(eventType);
    return getMessage(subscriberName, clazz, maxWaitTimeMillis);
  }

  public <T> T getMessage(String subscription, Class<T> clazz, long maxWaitTimeMillis)
      throws CTPException {
    String message = getMessage(subscription, maxWaitTimeMillis);

    // Return to caller if nothing read from queue
    if (message == null) {
      log.info(
          "Rabbit getMessage. Message is null. Unable to convert to class '" + clazz.getName()
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
      log.error(errorMessage, kv("subscritption", subscription), e);
      throw new CTPException(CTPException.Fault.SYSTEM_ERROR, e, errorMessage);
    }
  }

  /**
   * Read the next message from a subscription.
   *
   * @param subscription holds the name of the queue to attempt the read from.
   * @return a String with the content of the message body, or null if there was no message to read.
   * @throws CTPException if Rabbit threw an exception during the message get.
   */
  private synchronized String getMessageNoWait(String subscription) throws CTPException {
    String result = syncPullMessage(projectId, subscription);
    return result == null ? null : new String(result);
  }

  // ref: https://cloud.google.com/pubsub/docs/pull#synchronous_pull
  private static String syncPullMessage(String projectId, String subscription) throws CTPException {
    String msg = null;
    try {
      SubscriberStubSettings subscriberStubSettings = buildSubscriberStubSettings(true);
      try (SubscriberStub subscriber = GrpcSubscriberStub.create(subscriberStubSettings)) {
        String subscriptionName = ProjectSubscriptionName.format(projectId, subscription);
        PullRequest pullRequest = PullRequest.newBuilder().setMaxMessages(1).setSubscription(subscriptionName).build();

        // Use pullCallable().futureCall to asynchronously perform this operation.

        PullResponse pullResponse = subscriber.pullCallable().call(pullRequest);
        String ackId = null;

        if (!pullResponse.getReceivedMessagesList().isEmpty()) {
          ReceivedMessage rm = pullResponse.getReceivedMessagesList().get(0);
          ackId = rm.getAckId();
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

  private static SubscriberStubSettings buildSubscriberStubSettings(boolean isForEmulator) throws IOException {
    if (isForEmulator)
      return buildEmulatorSubscriberStubSettings();
    else
      return buildCloudSubscriberStubSettings();
  }

  private static SubscriberStubSettings buildCloudSubscriberStubSettings()
      throws IOException {
    SubscriberStubSettings subscriberStubSettings = SubscriberStubSettings.newBuilder()
        .setTransportChannelProvider(
            SubscriberStubSettings.defaultGrpcTransportProviderBuilder()
                .setMaxInboundMessageSize(20 * 1024 * 1024) // 20MB (maximum message size).
                .build()).build();
    return subscriberStubSettings;
  }

  private static SubscriberStubSettings buildEmulatorSubscriberStubSettings() throws IOException {
    SubscriberStubSettings subscriberStubSettings = SubscriberStubSettings.newBuilder()
        .setTransportChannelProvider(channelProvider).setCredentialsProvider(credentialsProvider).build();

    return subscriberStubSettings;
  }

  public static void main(String[] args) throws Exception {
    //PubSubHelper.instance("local", false);
    //String hostport = System.getenv("PUBSUB_EMULATOR_HOST");
    //PubSubHelper.verifyAndCreateSubscription("local", "event_case-update", "donkey");
    //  PubSubHelper localPubSubHelper = new PubSubHelper("local", false);
    // send event to event_case-update
    // localPubSubHelper.sendEvent()

    //retrieve message
    //  String msg = localPubSubHelper.getMessageNoWait("event_case-update_rh");
    //  System.out.println("msg: " + msg);
  }
}
