package uk.gov.ons.ctp.integration.rhcucumber.glue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import lombok.SneakyThrows;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.common.event.EventSender;
import uk.gov.ons.ctp.common.event.EventTopic;
import uk.gov.ons.ctp.common.event.model.GenericEvent;
import uk.gov.ons.ctp.common.jackson.CustomObjectMapper;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class NativePubSubEventSender implements EventSender {

  private ObjectMapper objectMapper;
  private boolean addRmProperties;
  private String projectId;

  public NativePubSubEventSender(String projectId, boolean addRmProperties) throws CTPException {
    this.addRmProperties = addRmProperties;
    this.projectId = projectId;
    objectMapper = new CustomObjectMapper();
  }

  @SneakyThrows @Override
  public void sendEvent(EventTopic topic, GenericEvent genericEvent) {

    TopicName topicName = TopicName.of(projectId, topic.getTopic());
    Publisher publisher = null;
    try {
      // Create a publisher instance with default settings bound to the topic
      publisher = Publisher.newBuilder(topicName).build();

      ByteString data =  ByteString.copyFrom(objectMapper.writeValueAsString(genericEvent),"UTF-8");
      PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

      // Once published, returns a server-assigned message id (unique within the topic)
      ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
      String messageId = messageIdFuture.get();
      System.out.println("Published message ID: " + messageId);
    } catch (IOException | ExecutionException | InterruptedException e) {
      e.printStackTrace();
    } finally {
      if (publisher != null) {
        // When finished with the publisher, shutdown to free up resources.
        publisher.shutdown();
        publisher.awaitTermination(1, TimeUnit.MINUTES);
      }
    }
  }
}
