package uk.gov.ons.ctp.integration.rhcucumber.glue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.ProjectSubscriptionName;
import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.common.domain.Channel;
import uk.gov.ons.ctp.common.domain.Source;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.common.event.EventPublisher;
import uk.gov.ons.ctp.common.event.EventTopic;
import uk.gov.ons.ctp.common.event.EventType;
import uk.gov.ons.ctp.common.event.model.EventPayload;

import static uk.gov.ons.ctp.common.log.ScopedStructuredArguments.kv;

@Slf4j
public class PubSubHelper {

    private static PubSubHelper instance = null;
    private EventPublisher eventPublisher;
    private String projectId;

    private ObjectMapper mapper = new ObjectMapper();

    private PubSubHelper(String projectId, boolean addRmProperties)
        throws CTPException {

        NativePubSubEventSender sender =
            new NativePubSubEventSender(projectId, addRmProperties);
        eventPublisher = EventPublisher.createWithoutEventPersistence(sender);

        this.projectId = projectId;
    }


    public static synchronized PubSubHelper instance(String projectId, boolean addRmProperties) throws CTPException {

        if (instance == null) {
            instance = new PubSubHelper(projectId, addRmProperties);
        }
        return instance;
    }

    public void flushTopic(EventTopic eventTopic) {
        String projectId = "local";
        String subscriptionId = "cucumber-sub";
        String topic = eventTopic.getTopic();
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);
        
    }

    /**
     * Publish a message to a pubsub topic.
     *
     * @param eventType is the type of the event that is being sent.
     * @param source states who is sending, or pretending, to set the message.
     * @param channel holds a channel identifier.
     * @param payload is the object to be sent.
     * @return the transaction id generated for the published message.
     * @throws CTPException if anything went wrong.
     */
    public synchronized String sendEvent(
        EventType eventType, Source source, Channel channel, EventPayload payload)
        throws CTPException {
        try {
            String transactionId = eventPublisher.sendEvent(eventType, source, channel, payload);
            return transactionId;

        } catch (Exception e) {
            String errorMessage = "Failed to send message. Cause: " + e.getMessage();
            log.error(
                errorMessage,
                kv("eventType", eventType),
                kv("source", source),
                kv("channel", channel),
                e);
            throw new CTPException(CTPException.Fault.SYSTEM_ERROR, errorMessage, e);
        }
    }

    /**
     * Reads a message from the named queue. This method will wait for up to the specified number of
     * milliseconds for a message to appear on the queue.
     *
     * @param subscription is the name of the queue to read from.
     * @param maxWaitTimeMillis is the maximum amount of time the caller is prepared to wait for the
     *     message to appear.
     * @return a String containing the content of the message body, or null if no message was found
     *     before the timeout expired.
     * @throws CTPException if Rabbit threw an exception when we attempted to read a message.
     */
    public String getMessage(String subscription, long maxWaitTimeMillis) throws CTPException {
        final long startTime = System.currentTimeMillis();
        final long timeoutLimit = startTime + maxWaitTimeMillis;

        log.info(
            "PubSub getMessage. Reading from queue '"
                + subscription
                + "'"
                + " within "
                + maxWaitTimeMillis
                + "ms");

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
     * @param <T> is the class of object we are expected to recieve.
     * @param queueName is the name of the queue to read from.
     * @param clazz is the class that the message should be converted to.
     * @param maxWaitTimeMillis is the maximum amount of time the caller is prepared to wait for the
     *     message to appear.
     * @return an object of the specified type, or null if no message was found before the timeout
     *     expired.
     * @throws CTPException if Rabbit threw an exception when we attempted to read a message.
     */
    public <T> T getMessage(String queueName, Class<T> clazz, long maxWaitTimeMillis)
        throws CTPException {
        String message = getMessage(queueName, maxWaitTimeMillis);

        // Return to caller if nothing read from queue
        if (message == null) {
            log.info(
                "Rabbit getMessage. Message is null. Unable to convert to class '"
                    + clazz.getName()
                    + "'");
            return null;
        }

        // Use Jackson to convert from a Json message to a Java object
        try {
            log.info("Rabbit getMessage. Converting result into class '" + clazz.getName() + "'");
            return mapper.readValue(message, clazz);

        } catch (IOException e) {
            String errorMessage = "Failed to convert message to object of type '" + clazz.getName() + "'";
            log.error(errorMessage, kv("queueName", queueName), e);
            throw new CTPException(Fault.SYSTEM_ERROR, e, errorMessage);
        }
    }

    /**
     * Read the next message from a subscription.
     *
     * @param queueName holds the name of the queue to attempt the read from.
     * @return a String with the content of the message body, or null if there was no message to read.
     * @throws CTPException if Rabbit threw an exception during the message get.
     */
    private synchronized String getMessageNoWait(String subscription) throws CTPException {
        // Attempt to read a message from the queue
        GetResponse result;
        try {
            result = channel.basicGet(subscription, true);
        } catch (IOException e) {
            channel = null;
            String errorMessage = "Failed to flush queue '" + subscription + "'";
            log.error(errorMessage, kv("queueName", subscription), e);
            throw new CTPException(Fault.SYSTEM_ERROR, e, errorMessage);
        }

        return result == null ? null : new String(result.getBody());
    }
}
