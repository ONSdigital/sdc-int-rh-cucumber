package uk.gov.ons.ctp.integration.rhcucumber.glue;

import com.google.pubsub.v1.ProjectSubscriptionName;
import uk.gov.ons.ctp.common.event.EventTopic;

public class PubSubHelper {
    public static PubSubHelper instance() {
        return new PubSubHelper();
    }

    public void flushTopic(EventTopic eventTopic) {
        String projectId = "local";
        String subscriptionId = "cucumber-sub";
        String topic = eventTopic.getTopic();
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);
        
    }
}
