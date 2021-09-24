package uk.gov.ons.ctp.integration.rhcucumber.glue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.ons.ctp.common.event.EventType;
import uk.gov.ons.ctp.common.event.model.FulfilmentEvent;
import uk.gov.ons.ctp.common.event.model.GenericEvent;
import uk.gov.ons.ctp.common.event.model.SurveyLaunchEvent;
import uk.gov.ons.ctp.common.event.model.UacAuthenticateEvent;
import uk.gov.ons.ctp.common.pubsub.PubSubHelper;
import uk.gov.ons.ctp.common.util.UacUtil;
import uk.gov.ons.ctp.common.util.WebDriverFactory;
import uk.gov.ons.ctp.integration.rhcucumber.data.ExampleData;
import uk.gov.ons.ctp.integration.rhcucumber.repository.RespondentDataRepository;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Pages;

public abstract class StepsBase {
  static final long PUBSUB_TIMEOUT_MS = 20000;
  static final long WAIT_TIMEOUT = 20_000L;

  @Autowired GlueContext context;
  @Autowired RespondentDataRepository dataRepo;
  @Autowired WebDriverFactory webDriverFactory;
  @Autowired Pages pages;

  @Value("${keystore}")
  String keystore;

  @Value("${pubsub.projectid}")
  private String pubsubProjectId;

  @Value("${pubsub.emulator.host}")
  private String emulatorPubSubHost;

  @Value("${pubsub.emulator.use}")
  private boolean useEmulatorPubSub;

  WebDriver driver;
  PubSubHelper pubSub;

  public void setupForAll() throws Exception {
    dataRepo.deleteCollections();
    pubSub = PubSubHelper.instance(pubsubProjectId, false, useEmulatorPubSub, emulatorPubSubHost);
    driver = pages.getWebDriver();
  }

  void closeDriver() {
    webDriverFactory.closeWebDriver(driver);
  }

  void destroyPubSub() {
    PubSubHelper.destroy();
  }

  String validUac() {
    return "SRBS3P28KTH28RQ8";
  }

  String uacHash(String uac) {
    return UacUtil.getSha256Hash(uac);
  }

  void verifyCorrectOnsLogoUsed(WebElement logo, Country country) {
    String expectedLogoTextId;
    switch (country) {
      case WALES:
        expectedLogoTextId = "ons-logo-cy-alt";
        break;
      case ENG:
        expectedLogoTextId = "ons-logo-en-alt";
        break;
      default:
        expectedLogoTextId = "";
    }
    assertNotNull(logo);
    String actualLogoTextId = extractLogoName(logo.getAttribute("id"));
    assertEquals(
        "name found for logo is incorrect - " + country.name(),
        expectedLogoTextId,
        actualLogoTextId);
  }

  String extractLogoName(String sourceFound) {
    int fileNameStart = sourceFound.lastIndexOf("/");
    String logoName = sourceFound.substring(fileNameStart + 1);
    return logoName;
  }

  void constructUacUpdatedEvent() {
    context.uac = validUac();
    context.uacKey = uacHash(context.uac);
    context.uacPayload = ExampleData.createUac(context.uacKey, context.caseKey);
  }

  // - event validation helpers ...

  void emptyEventQueue(EventType eventType) throws Exception {
    pubSub.flushSubscription(eventType);
  }

  void assertNewEventHasFired(EventType eventType) throws Exception {

    final GenericEvent event =
        (GenericEvent) pubSub.getMessage(eventType, eventClass(eventType), PUBSUB_TIMEOUT_MS);

    assertNotNull(event);
    assertNotNull(event.getEvent());
  }

  void assertNewRespondantAuthenticatedEventHasFired() throws Exception {

    EventType eventType = EventType.UAC_AUTHENTICATE;

    UacAuthenticateEvent event =
        (UacAuthenticateEvent)
            pubSub.getMessage(eventType, eventClass(eventType), PUBSUB_TIMEOUT_MS);

    assertNotNull(event);

    context.respondentAuthenticatedHeader = event.getEvent();
    assertNotNull(context.respondentAuthenticatedHeader);

    context.respondentAuthenticatedPayload = event.getPayload();
    assertNotNull(context.respondentAuthenticatedPayload);
  }

  void assertNewSurveyLaunchedEventHasFired() throws Exception {

    EventType eventType = EventType.SURVEY_LAUNCH;

    context.surveyLaunchedEvent =
        (SurveyLaunchEvent) pubSub.getMessage(eventType, eventClass(eventType), PUBSUB_TIMEOUT_MS);

    assertNotNull(context.surveyLaunchedEvent);

    context.surveyLaunchedHeader = context.surveyLaunchedEvent.getEvent();
    assertNotNull(context.surveyLaunchedHeader);

    context.surveyLaunchedPayload = context.surveyLaunchedEvent.getPayload();
    assertNotNull(context.surveyLaunchedPayload);
  }

  void assertNewFulfilmentEventHasFired() throws Exception {
    EventType eventType = EventType.FULFILMENT;

    FulfilmentEvent fulfilmentRequestedEvent =
        (FulfilmentEvent) pubSub.getMessage(eventType, eventClass(eventType), PUBSUB_TIMEOUT_MS);

    context.fulfilmentRequestedCode =
        fulfilmentRequestedEvent.getPayload().getFulfilmentRequest().getFulfilmentCode();

    assertNotNull(fulfilmentRequestedEvent);
    assertNotNull(fulfilmentRequestedEvent.getEvent());
    assertNotNull(fulfilmentRequestedEvent.getPayload());
    assertNotNull(context.fulfilmentRequestedCode);
  }

  Class<?> eventClass(EventType eventType) {
    switch (eventType) {
      case FULFILMENT:
        return FulfilmentEvent.class;
      case UAC_AUTHENTICATE:
        return UacAuthenticateEvent.class;
      case SURVEY_LAUNCH:
        return SurveyLaunchEvent.class;
      default:
        throw new IllegalArgumentException("Cannot create event for event type: " + eventType);
    }
  }
}
