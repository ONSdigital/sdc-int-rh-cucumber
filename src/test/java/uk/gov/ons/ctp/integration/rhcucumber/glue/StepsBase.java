package uk.gov.ons.ctp.integration.rhcucumber.glue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.ons.ctp.common.event.EventPublisher;
import uk.gov.ons.ctp.common.event.EventPublisher.EventType;
import uk.gov.ons.ctp.common.event.model.Address;
import uk.gov.ons.ctp.common.event.model.AddressModifiedEvent;
import uk.gov.ons.ctp.common.event.model.Contact;
import uk.gov.ons.ctp.common.event.model.FulfilmentRequestedEvent;
import uk.gov.ons.ctp.common.event.model.GenericEvent;
import uk.gov.ons.ctp.common.event.model.NewAddressReportedEvent;
import uk.gov.ons.ctp.common.event.model.QuestionnaireLinkedEvent;
import uk.gov.ons.ctp.common.event.model.RespondentAuthenticatedEvent;
import uk.gov.ons.ctp.common.event.model.SurveyLaunchedEvent;
import uk.gov.ons.ctp.common.rabbit.RabbitHelper;
import uk.gov.ons.ctp.common.util.UacUtil;
import uk.gov.ons.ctp.common.util.WebDriverFactory;
import uk.gov.ons.ctp.integration.rhcucumber.data.ExampleData;
import uk.gov.ons.ctp.integration.rhcucumber.repository.RespondentDataRepository;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Pages;

public abstract class StepsBase {
  static final String RABBIT_EXCHANGE = "events";
  static final int RABBIT_TIMEOUT = 2000;
  static final long WAIT_TIMEOUT = 20_000L;

  @Autowired RhStepsContext context;
  @Autowired RespondentDataRepository dataRepo;
  @Autowired WebDriverFactory webDriverFactory;
  @Autowired Pages pages;

  @Value("${keystore}")
  String keystore;

  WebDriver driver;
  RabbitHelper rabbit;

  public void setupForAll() throws Exception {
    dataRepo.deleteCollections();
    rabbit = RabbitHelper.instance(RABBIT_EXCHANGE, true);
    driver = pages.getWebDriver();
  }

  void closeDriver() {
    webDriverFactory.closeWebDriver(driver);
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
    String queueName = rabbit.createQueue(eventType);
    rabbit.flushQueue(queueName);
  }

  void assertNewEventHasFired(EventType eventType) throws Exception {

    final GenericEvent event =
        (GenericEvent)
            rabbit.getMessage(
                EventPublisher.RoutingKey.forType(eventType).getKey(),
                eventClass(eventType),
                RABBIT_TIMEOUT);

    assertNotNull(event);
    assertNotNull(event.getEvent());
  }

  void assertNewRespondantAuthenticatedEventHasFired() throws Exception {

    EventType eventType = EventType.RESPONDENT_AUTHENTICATED;

    RespondentAuthenticatedEvent event =
        (RespondentAuthenticatedEvent)
            rabbit.getMessage(
                EventPublisher.RoutingKey.forType(eventType).getKey(),
                eventClass(eventType),
                RABBIT_TIMEOUT);

    assertNotNull(event);

    context.respondentAuthenticatedHeader = event.getEvent();
    assertNotNull(context.respondentAuthenticatedHeader);

    context.respondentAuthenticatedPayload = event.getPayload();
    assertNotNull(context.respondentAuthenticatedPayload);
  }

  void assertNewSurveyLaunchedEventHasFired() throws Exception {
    EventType eventType = EventType.SURVEY_LAUNCHED;

    context.surveyLaunchedEvent =
        (SurveyLaunchedEvent)
            rabbit.getMessage(
                EventPublisher.RoutingKey.forType(eventType).getKey(),
                eventClass(eventType),
                RABBIT_TIMEOUT);

    assertNotNull(context.surveyLaunchedEvent);

    context.surveyLaunchedHeader = context.surveyLaunchedEvent.getEvent();
    assertNotNull(context.surveyLaunchedHeader);

    context.surveyLaunchedPayload = context.surveyLaunchedEvent.getPayload();
    assertNotNull(context.surveyLaunchedPayload);
  }

  void assertNewFulfilmentEventHasFired() throws Exception {
    EventType eventType = EventType.FULFILMENT_REQUESTED;

    FulfilmentRequestedEvent fulfilmentRequestedEvent =
        (FulfilmentRequestedEvent)
            rabbit.getMessage(
                EventPublisher.RoutingKey.forType(eventType).getKey(),
                eventClass(eventType),
                RABBIT_TIMEOUT);

    context.fulfilmentRequestedCode =
        fulfilmentRequestedEvent.getPayload().getFulfilmentRequest().getFulfilmentCode();

    assertNotNull(fulfilmentRequestedEvent);
    assertNotNull(fulfilmentRequestedEvent.getEvent());
    assertNotNull(fulfilmentRequestedEvent.getPayload());
    assertNotNull(context.fulfilmentRequestedCode);
  }

  Class<?> eventClass(EventType eventType) {
    switch (eventType) {
      case FULFILMENT_REQUESTED:
        return FulfilmentRequestedEvent.class;
      case NEW_ADDRESS_REPORTED:
        return NewAddressReportedEvent.class;
      case RESPONDENT_AUTHENTICATED:
        return RespondentAuthenticatedEvent.class;
      case SURVEY_LAUNCHED:
        return SurveyLaunchedEvent.class;
      case ADDRESS_MODIFIED:
        return AddressModifiedEvent.class;
      case QUESTIONNAIRE_LINKED:
        return QuestionnaireLinkedEvent.class;
      default:
        throw new IllegalArgumentException("Cannot create event for event type: " + eventType);
    }
  }

  // ---- data setup helpers ...

  void constructCaseCreatedEvent() {
    Address address = ExampleData.createNimrodAddress();
    Contact contact = ExampleData.createLadySallyContact();
    context.caseCreatedPayload =
        ExampleData.createCollectionCase(address, contact, context.caseKey);
  }

  void constructCaseCreatedEventWales() {
    Address address = ExampleData.createNimrodAddressWales();
    Contact contact = ExampleData.createLadySallyContact();
    context.caseCreatedPayload =
        ExampleData.createCollectionCase(address, contact, context.caseKey);
  }
}
