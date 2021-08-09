package uk.gov.ons.ctp.integration.rhcucumber.glue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uk.gov.ons.ctp.common.event.EventPublisher;
import uk.gov.ons.ctp.common.event.EventPublisher.Channel;
import uk.gov.ons.ctp.common.event.EventPublisher.EventType;
import uk.gov.ons.ctp.common.event.EventPublisher.Source;
import uk.gov.ons.ctp.common.event.model.Address;
import uk.gov.ons.ctp.common.event.model.AddressModifiedEvent;
import uk.gov.ons.ctp.common.event.model.Contact;
import uk.gov.ons.ctp.common.event.model.FulfilmentRequestedEvent;
import uk.gov.ons.ctp.common.event.model.GenericEvent;
import uk.gov.ons.ctp.common.event.model.NewAddressReportedEvent;
import uk.gov.ons.ctp.common.event.model.QuestionnaireLinkedEvent;
import uk.gov.ons.ctp.common.event.model.RespondentAuthenticatedEvent;
import uk.gov.ons.ctp.common.event.model.SurveyLaunchedEvent;
import uk.gov.ons.ctp.common.event.model.SurveyLaunchedPayload;
import uk.gov.ons.ctp.common.event.model.UAC;
import uk.gov.ons.ctp.common.util.Wait;
import uk.gov.ons.ctp.integration.eqlaunch.crypto.JweDecryptor;
import uk.gov.ons.ctp.integration.eqlaunch.crypto.KeyStore;
import uk.gov.ons.ctp.integration.rhcucumber.data.ExampleData;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.CensusQuestionnaire;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.WouldYouLikeToCompleteCensusInEnglish;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.ChangeYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.ChooseLanguage;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.ConfirmAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.ConfirmAddressForNewUac;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.ConfirmAddressToLinkUac;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.HouseholdInterstitial;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.IsThisMobileNumCorrect;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.LimitExceedPage;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PleaseSupplyYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.RegisterYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.SelectDeliveryMethodTextOrPost;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.SelectYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.SentAccessCode;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.StartPage;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.WebFormPage;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.WhatIsYourMobile;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.WhatIsYourName;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.WhatIsYourPostcode;

public class RhSteps extends StepsBase {
  private Wait wait;
  private Country country;

  @Autowired private RateLimiterMock rateLimiterMock;

  public static final List<String> HASH_KEYS_EXPECTED =
      Arrays.asList(
          "account_service_log_out_url",
          "account_service_url",
          "case_id",
          "case_type",
          "channel",
          "collection_exercise_sid",
          "display_address",
          "eq_id",
          "exp",
          "form_type",
          "iat",
          "jti",
          "language_code",
          "period_id",
          "questionnaire_id",
          "region_code",
          "response_id",
          "ru_ref",
          "survey",
          "tx_id",
          "user_id");

  @Before("@Setup")
  public void setupNoCountry() throws Exception {
    super.setupForAll();
    wait = new Wait(driver);
    context.caseCollection = "case";
    context.caseKey = "c45de4dc-3c3b-11e9-b210-d663bd873d13";
    context.uacCollection = "uac";
  }

  @Before("@SetUpRHEng")
  public void setupEng() throws Exception {
    initTest(Country.ENG);
  }

  @Before("@SetUpRHWales")
  public void setUpWales() throws Exception {
    initTest(Country.WALES);
  }

  @Before("@EnableRateLimit")
  public void enableRateLimit() throws Exception {
    callRateLimiterMock(true);
  }

  private void initTest(Country country) throws Exception {
    setupNoCountry();
    setupTest(country);
  }

  private void setupTest(Country country) throws Exception {
    this.country = country;
    pages.getStartPage(country);
  }

  @After("@TearDown")
  public void deleteDriver() {
    super.closeDriver();
  }

  @After("@TearDownRH2")
  public void deleteDriverRH2() {
    super.closeDriver();
  }

  @After("@DisableRateLimit")
  public void disableRateLimit() throws Exception {
    callRateLimiterMock(false);
  }

  private void callRateLimiterMock(boolean flag) throws Exception {
    Map<String, String> headerParams = new HashMap<>();
    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
    queryParams.add("enabled", Boolean.toString(flag));
    rateLimiterMock
        .rateLimiterMock()
        .postResource("limit", null, String.class, headerParams, queryParams, "");
  }

  @Given("RM constructs a case_created event and a uac_updated event")
  public void rm_constructs_a_case_created_event_and_a_uac_updated_event() throws Exception {
    constructCaseCreatedEvent();
    constructUacUpdatedEvent();
  }

  @Given("RM constructs a different case_created event and a uac_updated event")
  public void rm_constructs_a_different_case_created_event_and_a_uac_updated_event()
      throws Exception {
    constructCaseCreatedEventWithDifferentAddress();
    constructUacUpdatedEvent();
  }

  @Given("RM constructs a case_created event and a uac_updated event where Region is W")
  public void rm_constructs_a_case_created_event_and_a_uac_updated_event_where_Region_is_W()
      throws Exception {
    constructCaseCreatedEventWales();
    constructUacUpdatedEvent();
  }

  @Given("RM constructs a different case_created event and a uac_updated event where Region is W")
  public void
      rm_constructs_a_different_case_created_event_and_a_uac_updated_event_where_Region_is_W()
          throws Exception {
    constructCaseCreatedEventWithDifferentAddressWales();
    constructUacUpdatedEvent();
  }

  @Given("RM sends the case_created and uac {string} events to RH via RabbitMQ")
  public void rm_sends_the_case_created_and_uac_events_to_RH_via_RabbitMQ(String uacEventType)
      throws Exception {

    rabbit.sendEvent(
        EventType.CASE_CREATED, Source.CASE_SERVICE, Channel.RM, context.caseCreatedPayload);

    EventType eventType;

    switch (uacEventType) {
      case "UAC_CREATED":
        eventType = EventType.UAC_CREATED;
        break;
      case "UAC_UPDATED":
        eventType = EventType.UAC_UPDATED;
        break;
      default:
        throw new IllegalArgumentException("Bad test passing uac event type: " + uacEventType);
    }

    rabbit.sendEvent(eventType, Source.SAMPLE_LOADER, Channel.RM, context.uacPayload);
  }

  @Given("RM sends the case_created and a uac_updated events to RH via RabbitMQ")
  public void rm_sends_the_case_created_and_a_uac_updated_events_to_RH_via_RabbitMQ()
      throws Exception {
    rm_sends_the_case_created_and_uac_events_to_RH_via_RabbitMQ(EventType.UAC_UPDATED.name());
  }

  @Then("a valid uac exists in Firestore ready for use by a respondent")
  public void a_valid_uac_exists_in_Firestore_ready_for_use_by_a_respondent() throws Exception {
    assertTrue(dataRepo.waitForObject(context.caseCollection, context.caseKey, WAIT_TIMEOUT));
    assertTrue(dataRepo.waitForObject(context.uacCollection, context.uacKey, WAIT_TIMEOUT));
  }

  @Given("I am a respondent and I am on the RH Start Page")
  public void iAmARespondentAndIAmOnTheRHStartPage() {
    pages.getStartPage(country);
  }

  @Given("The respondent confirms a valid {} address {string}")
  public void the_respondent_confirms_the_address(Country country, final String postCode)
      throws Exception {
    this.country = country;
    StartPage startPage = pages.getStartPage(country);
    startPage.clickRequestNewCodeLink();
    confirm_your_address(country, postCode);
  }

  @And("the respondent selects the delivery channel as \"Text message\"")
  public void the_respondent_selects_the_delivery_channel_as_Text_message() {
    pages.getSelectDeliveryMethodTextOrPost(country).clickOptionText();
    pages.getSelectDeliveryMethodTextOrPost(country).clickContinueButton();
  }

  @And("the respondents enters their valid UK mobile telephone number")
  public void the_respondent_enters_their_valid_UK_mobile_telephone_number() {
    WhatIsYourMobile whatIsYourMobile = pages.getWhatIsYourMobile(country);
    whatIsYourMobile.addTextToMobileNumBox("07700 900345");
    whatIsYourMobile.clickContinueButton();
  }

  @When("the respondents selects continue \"text\"")
  public void the_respondents_selects_continue_text() {
    IsThisMobileNumCorrect isThisMobileNumCorrect = pages.getIsThisMobileNumCorrect(country);
    isThisMobileNumCorrect.clickOptionYes();
    isThisMobileNumCorrect.clickContinueButton();
  }

  @Then("RHSVC publishes a new address event")
  public void RHSVC_publishes_a_new_address_event() throws Exception {
    assertNewEventHasFired(EventType.NEW_ADDRESS_REPORTED);
  }

  @And("RHSVC publishes a UAC fulfilment request")
  public void RHSVC_publishes_a_UAC_fulfilment_request() throws Exception {
    assertNewEventHasFired(EventType.FULFILMENT_REQUESTED);
  }

  @And("the respondent selects the delivery channel as \"Post\"")
  public void the_respondent_selects_the_delivery_channel_as_Post() {
    SelectDeliveryMethodTextOrPost selectDeliveryMethodTextOrPost =
        pages.getSelectDeliveryMethodTextOrPost(country);
    selectDeliveryMethodTextOrPost.clickOptionPost();
    selectDeliveryMethodTextOrPost.clickContinueButton();
  }

  @And("the respondent enters their first and last name")
  public void the_respondent_enters_their_first_and_last_name() {
    WhatIsYourName whatIsYourName = pages.getWhatIsYourName(country);
    whatIsYourName.addTextToFirstNameTextBox("ray");
    whatIsYourName.addTextToLastNameTextBox("vis");
    whatIsYourName.clickContinueButton();
  }

  @When("the respondent confirms the address by selecting \"Yes, send the access code by post\"")
  public void the_respondent_confirms_the_address_by_selecting_Yes_send_the_access_code_by_post() {
    pages.getNewHouseholdAccessCode(country).selectYesSendByPost();
  }

  @And("selects continue")
  public void selects_continue() {
    pages.getNewHouseholdAccessCode(country).clickContinueButton();
  }

  @Given("I am a respondent and I am on the RH Start Page {}")
  public void i_am_a_respondent_and_I_am_on_the_RH_Start_Page(Country country) throws Exception {
    setupTest(country);
  }

  @Then("I am now on the Alternative Language RH Start Page {}")
  public void i_am_now_on_the_Alt_RH_Start_Page(Country country) {
    this.country = country;
    verifyCorrectOnsLogoUsed(pages.getStartPage(country).getOnsLogo(), country);
  }

  @Given("I enter the valid UAC into the text box")
  public void i_enter_the_valid_UAC_into_the_text_box() {
    pages.getStartPage().enterUac(context.uac);
  }

  @Given("I click the “Access survey” button")
  public void i_click_the_Access_survey_button() {
    pages.getStartPage().clickAccessSurveyButton();
  }

  @Then("I am presented with a page to confirm my address")
  public void i_am_presented_with_a_page_to_confirm_my_address() {
    ConfirmAddress confirmAddress = pages.getConfirmAddress(country);
    verifyCorrectOnsLogoUsed(confirmAddress.getOnsLogo(), country);

    assertEquals(
        "address confirmation title has incorrect text",
        pages.getConfirmAddress().getExpectedConfirmText(),
        pages.getConfirmAddress().getConfirmAddressTitleText());
  }

  @Then("a blank uac error {string} appears")
  public void a_blank_uac_error_appears(String errorMessage) {
    assertEquals(
        "The error message shown has incorrect text",
        errorMessage,
        pages.getStartPage().getErrorEnterAccessCodeText());
  }

  @Given("I enter an invalid UAC {string} into the text box")
  public void i_enter_an_invalid_UAC_into_the_text_box(String uac) {
    pages.getStartPage().enterUac(uac);
  }

  @Then("an invalid uac error {string} appears")
  public void an_invalid_uac_error_appears(String errorMessage) {

    assertEquals(
        "The error message shown has incorrect text",
        errorMessage,
        pages.getStartPage().getErrorEnterValidCodeText());
  }

  @Given("I select the “Yes, this address is correct” option")
  public void i_select_the_Yes_this_address_is_correct_option() {
    pages.getConfirmAddress(country).clickOptionYes();
  }

  @Given("I click the “Continue” button")
  public void i_click_the_Continue_button() {
    wait.forLoading(1);
    try {
      pages.getConfirmAddress(country).clickContinueButton();
    } catch (WebDriverException e) {
      // tolerate no EQ deployment for testing
      context.errorMessageContainingCallToEQ = e.getMessage();
    }
  }

  @Then("I am presented with a page asking text or post")
  public void i_am_presented_with_a_page_asking_text_or_post() {
    SelectDeliveryMethodTextOrPost selectDeliveryMethodTextOrPost =
        pages.getSelectDeliveryMethodTextOrPost(country);
    verifyCorrectOnsLogoUsed(selectDeliveryMethodTextOrPost.getOnsLogo(), country);

    assertEquals(
        "select delivery method title has incorrect text",
        pages.getSelectDeliveryMethodTextOrPost().getExpectedSelectDeliveryMethodTextOrPostText(),
        pages.getSelectDeliveryMethodTextOrPost().getSelectDeliveryMethodTextOrPostTitleText());
  }

  @Given("I select 'Text message' and click 'Continue'")
  public void i_select_the_text_message_option() {
    pages.getSelectDeliveryMethodTextOrPost(country).clickOptionText();
    pages.getSelectDeliveryMethodTextOrPost(country).clickContinueButton();
  }

  @Then("I am presented with a page to choose a language")
  public void i_am_presented_with_a_page_to_choose_a_language() {
    wait.forLoading();
    ChooseLanguage chooseLanguagePage = pages.getChooseLanguage();
    verifyCorrectOnsLogoUsed(chooseLanguagePage.getOnsLogo(), country);

    assertEquals(
        "choose language title has incorrect text",
        "Would you like to complete the census in English?",
        chooseLanguagePage.getChooseLanguageTitleText());
  }

  @Given("I select the option “Yes, continue in English”")
  public void i_select_the_option_Yes_continue_in_English() {
    pages.getChooseLanguage().clickOptionYes();
  }

  @Given("I click the continue button")
  public void i_click_the_continue_button() {

    try {
      pages.getChooseLanguage().clickContinueButton();
    } catch (WebDriverException e) {
      // tolerate no EQ deployment for testing
      context.errorMessageContainingCallToEQ = e.getMessage();
    }
  }

  @Then("I am directed to the Census Questionnaire")
  public void i_am_directed_to_the_Census_Questionnaire() {
    context.eqExists = eqExists(context);
  }

  @Given("an empty queue exists for sending Respondent Authenticated events")
  public void an_empty_queue_exists_for_sending_Respondent_Authenticated_events() throws Exception {
    emptyEventQueue(EventType.RESPONDENT_AUTHENTICATED);
  }

  @Given("an empty queue exists for sending Survey Launched events")
  public void an_empty_queue_exists_for_sending_Survey_Launched_events() throws Exception {
    emptyEventQueue(EventType.SURVEY_LAUNCHED);
  }

  @Given("an empty queue exists for sending Address Modified events")
  public void an_empty_queue_exists_for_sending_Address_Modified_events() throws Exception {
    emptyEventQueue(EventType.ADDRESS_MODIFIED);
  }

  @Given("an empty queue exists for sending Fulfilment Requested events")
  public void an_empty_queue_exists_for_sending_Fulfilment_Requested_events() throws Exception {
    emptyEventQueue(EventType.FULFILMENT_REQUESTED);
  }

  @Given("an empty queue exists for sending questionnaire linked events")
  public void an_empty_queue_exists_for_sending_questionnaire_linked_events() throws Exception {
    emptyEventQueue(EventType.QUESTIONNAIRE_LINKED);
  }

  @Then("a Respondent Authenticated event is sent to RM")
  public void a_Respondent_Authenticated_event_is_sent_to_RM() throws Exception {
    assertNewRespondantAuthenticatedEventHasFired();
  }

  @Then("a Survey Launched event is sent to RM")
  public void a_Survey_Launched_event_is_sent_to_RM() throws Exception {
    assertNewSurveyLaunchedEventHasFired();
  }

  @Then("a FulfilmentRequested event is sent to RM")
  public void a_FulfilmentRequested_event_is_sent_to_RM() throws Exception {
    assertNewEventHasFired(EventType.FULFILMENT_REQUESTED);
  }

  @Then("the respondentAuthenticatedHeader contains a Type with value {string}")
  public void the_respondentAuthenticatedHeader_contains_a_Type_with_value(String expectedType) {
    EventType type = context.respondentAuthenticatedHeader.getType();
    String strType = type.name();
    assertEquals(
        "The RespondentAuthenticatedEvent contains a incorrect value of 'type'",
        expectedType,
        strType);
  }

  @Then("the surveyLaunchedHeader contains a Type with value {string}")
  public void the_surveyLaunchedHeader_contains_a_Type_with_value(String expectedType) {
    EventType type = context.surveyLaunchedHeader.getType();
    String strType = type.name();
    assertEquals(
        "The SurveyLaunchedEvent contains a incorrect value of 'type'", expectedType, strType);
  }

  @Then("the respondentAuthenticatedHeader contains a Source with value {string}")
  public void the_respondentAuthenticatedHeader_contains_a_Source_with_value(
      String expectedSource) {
    Source source = context.respondentAuthenticatedHeader.getSource();
    String strSource = source.name();
    assertEquals(
        "The RespondentAuthenticatedEvent contains a incorrect value of 'source'",
        expectedSource,
        strSource);
  }

  @Then("the surveyLaunchedHeader contains a Source with value {string}")
  public void the_surveyLaunchedHeader_contains_a_Source_with_value(String expectedSource) {
    Source source = context.surveyLaunchedHeader.getSource();
    String strSource = source.name();
    assertEquals(
        "The SurveyLaunchedEvent contains a incorrect value of 'source'",
        expectedSource,
        strSource);
  }

  @Then("the respondentAuthenticatedHeader contains a Channel with value {string}")
  public void the_respondentAuthenticatedHeader_contains_a_Channel_with_value(
      String expectedChannel) {
    Channel channel = context.respondentAuthenticatedHeader.getChannel();
    String strChannel = channel.name();
    assertEquals(
        "The ResondentAuthenticatedEvent contains a incorrect value of 'channel'",
        expectedChannel,
        strChannel);
  }

  @Then("the surveyLaunchedHeader contains a Channel with value {string}")
  public void the_surveyLaunchedHeader_contains_a_Channel_with_value(String expectedChannel) {
    Channel channel = context.surveyLaunchedHeader.getChannel();
    String strChannel = channel.name();
    assertEquals(
        "The SurveyLaunchedEvent contains a incorrect value of 'channel'",
        expectedChannel,
        strChannel);
  }

  @Then("the respondentAuthenticatedHeader contains a DateTime value that is not null")
  public void the_respondentAuthenticatedHeader_contains_a_DateTime_value_that_is_not_null() {
    Date dateTime = context.respondentAuthenticatedHeader.getDateTime();
    assertNotNull(dateTime);
  }

  @Then("the surveyLaunchedHeader contains a DateTime value that is not null")
  public void the_surveyLaunchedHeader_contains_a_DateTime_value_that_is_not_null() {
    Date dateTime = context.surveyLaunchedHeader.getDateTime();
    assertNotNull(dateTime);
  }

  @Then("the respondentAuthenticatedHeader contains a TransactionId value that is not null")
  public void the_respondentAuthenticatedHeader_contains_a_TransactionId_value_that_is_not_null() {
    String transactionId = context.respondentAuthenticatedHeader.getTransactionId();
    assertNotNull(transactionId);
  }

  @Then("the surveyLaunchedHeader contains a TransactionId value that is not null")
  public void the_surveyLaunchedHeader_contains_a_TransactionId_value_that_is_not_null() {
    String transactionId = context.surveyLaunchedHeader.getTransactionId();
    assertNotNull(transactionId);
  }

  @Then("the respondentAuthenticatedPayload contains a Response")
  public void the_respondentAuthenticatedPayload_contains_a_Response() {
    context.respondentAuthenticatedResponse = context.respondentAuthenticatedPayload.getResponse();
    assertNotNull(context.respondentAuthenticatedResponse);
  }

  @Then("the surveyLaunchedPayload contains a Response")
  public void the_surveyLaunchedPayload_contains_a_Response() {
    context.surveyLaunchedResponse = context.surveyLaunchedPayload.getResponse();
    assertNotNull(context.surveyLaunchedResponse);
  }

  @Then("the respondentAuthenticatedResponse contains a QuestionnaireId value that is not null")
  public void
      the_respondentAuthenticatedResponse_contains_a_QuestionnaireId_value_that_is_not_null() {
    String questionnaireId = context.respondentAuthenticatedResponse.getQuestionnaireId();
    assertNotNull(questionnaireId);
  }

  @Then("the surveyLaunchedResponse contains a QuestionnaireId value that is not null")
  public void the_surveyLaunchedResponse_contains_a_QuestionnaireId_value_that_is_not_null() {
    String questionnaireId = context.surveyLaunchedResponse.getQuestionnaireId();
    assertNotNull(questionnaireId);
  }

  @Then("the surveyLaunchedResponse contains a CaseId value that may be null")
  public void the_surveyLaunchedResponse_contains_a_CaseId_value_that_may_be_null() {
    // UUID caseId = context.surveyLaunchedResponse.getCaseId();
  }

  @Then("the surveyLaunchedResponse contains an AgentId value that may be null")
  public void the_surveyLaunchedResponse_contains_an_AgentId_value_that_may_be_null() {
    // String agentId = context.surveyLaunchedResponse.getAgentId();
  }

  @Given("I click on request a new access code")
  public void i_click_on_request_a_new_access_code() {
    pages.getStartPage().clickRequestNewCodeLink();
  }

  @Then("I am presented with a page to enter my postcode on")
  public void i_am_presented_with_a_page_to_enter_my_postcode_on() {
    WhatIsYourPostcode whatIsYourPostcode = pages.getWhatIsYourPostcode(country);
    WebElement logo = whatIsYourPostcode.getOnsLogo();
    verifyCorrectOnsLogoUsed(logo, country);

    assertEquals(
        "What is your postcode - title has incorrect text",
        whatIsYourPostcode.getExpectedPostcodeText(),
        whatIsYourPostcode.getwhatIsYourPostcodeTitleText());
  }

  @Given("I enter the valid UK postcode {string}")
  public void i_enter_the_valid_UK_postcode(String validPostcode) {
    pages.getWhatIsYourPostcode().addTextToPostcodeTextBox(validPostcode);
  }

  @Given("select Continue")
  public void select_Continue() {
    pages.getWhatIsYourPostcode().clickContinueButton();
  }

  @Given("I enter the invalid UK postcode {string} into the postcode textbox")
  public void i_enter_the_invalid_UK_postcode_into_the_postcode_textbox(String invalidPostcode) {
    pages.getWhatIsYourPostcode().addTextToPostcodeTextBox(invalidPostcode);
  }

  @Then("an invalid postcode error {string} appears")
  public void an_invalid_postcode_error_appears(String errorMessage) {
    assertEquals(
        "The invalid postcode error message shown has incorrect text",
        errorMessage,
        pages.getWhatIsYourPostcode(country).getErrorEnterValidPostcodeText());
  }

  @Then("I am presented with a page to select an address from")
  public void i_am_presented_with_a_page_to_select_an_address_from() {

    SelectYourAddress selectYourAddress = pages.getSelectYourAddress(country);
    wait.forLoading();
    verifyCorrectOnsLogoUsed(selectYourAddress.getOnsLogo(), country);

    assertEquals(
        "Select Your Address - title has incorrect text",
        selectYourAddress.getExpectedSelectionText(),
        selectYourAddress.getSelectYourAddressTitleText());
  }

  @Then("a number of addresses is displayed")
  public void a_number_of_addresses_is_displayed() {
    Document document = Jsoup.parse(pages.getWebDriver().getPageSource());
    SelectYourAddress selectYourAddress = pages.getSelectYourAddress(country);
    Elements radioButtons =
        document.body().getElementsByAttributeValue("name", "form-pick-address");
    Element lastItem = radioButtons.attr("id", "xxxx").first();
    radioButtons.remove(lastItem); // last item has ID xxxx

    int numberOfAddresses = radioButtons.size();
    assertTrue(radioButtons.size() > 0);

    String expectedSelectionListText =
        selectYourAddress.getExpectedSelectionListText1()
            + numberOfAddresses
            + selectYourAddress.getExpectedSelectionListText2()
            + " "
            + selectYourAddress.getExpectedPostcode();

    String pageText =
        document
            .body()
            .getElementsByAttributeValue("class", "question__description")
            .first()
            .text();
    assertEquals(
        "The text shown for the number of addresses message is incorrect",
        expectedSelectionListText,
        pageText);
  }

  @Given("I select the address {string} and click continue")
  public void i_select_the_address_and_click_continue(final String address) {
    SelectYourAddress selectYourAddress = pages.getSelectYourAddress(country);
    selectYourAddress.selectFirstBulletPoint();
    selectYourAddress.clickContinueButton();
  }

  @Then("I am presented with a page displaying the address {string}")
  public void i_am_presented_with_a_page_displaying_the_address(String addressToBeDisplayed) {
    wait.forLoading();
    ConfirmAddressForNewUac confirmAddressForNewUac = pages.getConfirmAddressForNewUac(country);
    verifyCorrectOnsLogoUsed(confirmAddressForNewUac.getOnsLogo(), country);

    assertEquals(
        "Address confirmation for new uac - title has incorrect text",
        confirmAddressForNewUac.getExpectedConfirmText(),
        confirmAddressForNewUac.getConfirmAddressTitleText());

    assertEquals(
        "Address confirmation for new uac - address displayed is incorrect",
        confirmAddressForNewUac.getExpectedAddress(),
        confirmAddressForNewUac.getAddressToConfirmText());
  }

  @Given("I select the “No, I need to change it” option")
  public void i_select_the_No_I_need_to_change_it_option() {
    pages.getConfirmAddress(country).clickOptionNo();
  }

  @Then("I am presented with a page to change my address")
  public void i_am_presented_with_a_page_to_change_my_address() {
    wait.forLoading();
    ChangeYourAddress changeYourAddress = pages.getChangeYourAddress(country);
    verifyCorrectOnsLogoUsed(changeYourAddress.getOnsLogo(), country);

    assertEquals(
        "change your address title has incorrect text",
        "Change your address",
        changeYourAddress.getChangeYourAddressTitleText());
  }

  @Given("I change the address")
  public void i_change_the_address() {
    String inputText;
    switch (country) {
      case WALES:
        inputText = " in the middle of our Welsh street";
        break;
      default:
        inputText = " in the middle of our street";
    }
    pages.getChangeYourAddress(country).addTextToBuildingAndStreetTextBox(inputText);
  }

  @Given("I click on the “Save and Continue” button")
  public void i_click_on_the_Save_and_Continue_button() {

    try {
      pages.getChangeYourAddress(country).clickSaveAndContinueButton();
    } catch (WebDriverException e) {
      // tolerate no EQ deployment for testing
    }
  }

  @Then("an AddressModified event is sent to RM")
  public void an_AddressModified_event_is_sent_to_RM() throws Exception {
    assertNewEventHasFired(EventType.ADDRESS_MODIFIED);
  }

  @When("I select the ‘YES, This address is correct’ option and click continue")
  public void i_select_the_YES_This_address_is_correct_option_and_click_continue() {
    ConfirmAddressForNewUac confirmAddressForNewUac = pages.getConfirmAddressForNewUac(country);
    confirmAddressForNewUac.clickOptionYes();
    confirmAddressForNewUac.clickContinueButton();
  }

  @Then("I am presented with a page asking for my mobile number")
  public void i_am_presented_with_a_page_asking_for_my_mobile_number() {
    wait.forLoading();
    WhatIsYourMobile whatIsYourMobile = pages.getWhatIsYourMobile(country);
    verifyCorrectOnsLogoUsed(whatIsYourMobile.getOnsLogo(), country);

    assertEquals(
        "What is Your Mobile - title has incorrect text - " + country.name(),
        whatIsYourMobile.getExpectedText(),
        whatIsYourMobile.getWhatIsYourMobileTitleText());
  }

  @When("I select the ‘No, I need to change the address’ option and click continue")
  public void i_select_the_No_I_need_to_change_the_address_option_and_click_continue() {
    ConfirmAddressForNewUac confirmAddressForNewUac = pages.getConfirmAddressForNewUac(country);
    confirmAddressForNewUac.clickOptionNo();
    confirmAddressForNewUac.clickContinueButton();
  }

  @Given("I leave all the fields blank and do not select any options")
  public void i_leave_all_the_fields_blank_and_do_not_select_any_options() {
    // nothing
  }

  @Given("I enter my mobile number and click continue")
  public void i_enter_my_mobile_number_and_click_continue() {
    WhatIsYourMobile whatIsYourMobile = pages.getWhatIsYourMobile(country);
    whatIsYourMobile.addTextToMobileNumBox("07700 900345");
    whatIsYourMobile.clickContinueButton();
  }

  @Given("I am presented with a page to confirm my mobile number on")
  public void i_am_presented_with_a_page_to_confirm_my_mobile_number_on() {
    IsThisMobileNumCorrect isThisMobileNumCorrect = pages.getIsThisMobileNumCorrect(country);
    verifyCorrectOnsLogoUsed(isThisMobileNumCorrect.getOnsLogo(), country);

    assertEquals(
        "Is this mobile number correct - title has incorrect text",
        isThisMobileNumCorrect.getExpectedText(),
        isThisMobileNumCorrect.getIsMobileCorrectTitleText());
  }

  @When("I select the Yes option to send the text now")
  public void i_select_the_Yes_option_to_send_the_text_now() {
    IsThisMobileNumCorrect isThisMobileNumCorrect = pages.getIsThisMobileNumCorrect();
    isThisMobileNumCorrect.clickOptionYes();
    isThisMobileNumCorrect.clickContinueButton();
  }

  @When("I select the No option \\(because the number displayed is not correct)")
  public void i_select_the_No_option_because_the_number_displayed_is_not_correct() {
    IsThisMobileNumCorrect isThisMobileNumCorrect = pages.getIsThisMobileNumCorrect();
    isThisMobileNumCorrect.clickOptionNo();
    isThisMobileNumCorrect.clickContinueButton();
  }

  @Then("the first line of the address is {string}")
  public void the_first_line_of_the_address_is(String expectedFirstLine) {

    ConfirmAddress confirmAddress = pages.getConfirmAddress();
    confirmAddress.setAddressTextFields();

    String firstLineAddressFound = confirmAddress.getFirstLineAddress();
    assertEquals(
        "The first line of the address found is incorrect - " + country.name(),
        expectedFirstLine,
        firstLineAddressFound);
  }

  @Then("the second line of the address is {string}")
  public void the_second_line_of_the_address_is(String expectedSecondLine) {
    ConfirmAddress confirmAddress = pages.getConfirmAddress();
    String secondLineAddressFound = confirmAddress.getSecondLineAddress();
    assertEquals(
        "The second line of the address found is incorrect - " + country.name(),
        expectedSecondLine,
        secondLineAddressFound);
  }

  @Then("the third line of the address is {string}")
  public void the_third_line_of_the_address_is(String expectedThirdLine) {
    ConfirmAddress confirmAddress = pages.getConfirmAddress();
    String thirdLineAddressFound = confirmAddress.getThirdLineAddress();
    assertEquals(
        "The third line of the address found is incorrect - " + country.name(),
        expectedThirdLine,
        thirdLineAddressFound);
  }

  @Then("the town name is {string}")
  public void the_town_name_is(String expectedTownName) {
    ConfirmAddress confirmAddress = pages.getConfirmAddress();
    String townNameFound = confirmAddress.getTownName();
    assertEquals(
        "The town name found is incorrect - " + country.name(), expectedTownName, townNameFound);
  }

  @Then("the postcode is {string}")
  public void the_postcode_is(String expectedPostcode) {
    ConfirmAddress confirmAddress = pages.getConfirmAddress();
    String postcodeFound = confirmAddress.getPostcode();
    assertEquals(
        "The postcode found is incorrect - " + country.name(), expectedPostcode, postcodeFound);
  }

  @When("I select the Alternative language option")
  public void i_select_the_Alternative_language_option() {
    pages.getStartPage().clickAlternativeLanguageLink();
  }

  @Then("I am presented with a message saying that I have been sent an access code")
  public void i_am_presented_with_a_message_saying_that_I_have_been_sent_an_access_code() {
    SentAccessCode sentAccessCode = pages.getSentAccessCode(country);

    verifyCorrectOnsLogoUsed(sentAccessCode.getOnsLogo(), country);
    assertEquals(
        "Sent Access Code - title has incorrect text - " + country.name(),
        sentAccessCode.getExpectedText(),
        sentAccessCode.getSentAccessCodeTitleText());
  }

  @Then("I am presented with a button {string}")
  public void i_am_presented_with_a_button(String expectedLabel) {
    SentAccessCode sentAccessCode = pages.getSentAccessCode(country);
    String buttonLabelFound = sentAccessCode.getStartSurveyButtonText();
    assertEquals(
        "button label found is incorrect - " + country.name(), expectedLabel, buttonLabelFound);
  }

  @Then("I am also presented with a link to request a new access code")
  public void i_am_also_presented_with_a_link_to_request_a_new_code() {
    SentAccessCode sentAccessCode = pages.getSentAccessCode();
    assertEquals(
        "link text found is incorrect",
        sentAccessCode.getExpectedRequestCodeText(),
        sentAccessCode.getRequestNewCodeLinkText());
  }

  @When("I enter an invalid mobile number and click continue")
  public void i_enter_an_invalid_mobile_number_and_click_continue() {
    WhatIsYourMobile whatIsYourMobile = pages.getWhatIsYourMobile(country);
    whatIsYourMobile.addTextToMobileNumBox("1234567");
    whatIsYourMobile.clickContinueButton();
  }

  @Then("an invalid mobile number error message is displayed")
  public void an_invalid_mobile_number_error_message_is_displayed() {
    WhatIsYourMobile whatIsYourMobile = pages.getWhatIsYourMobile(country);
    assertEquals(
        "invalid mobile error message found is incorrect",
        whatIsYourMobile.getExpectedErrorText(),
        whatIsYourMobile.getInvalidMobileErrorText());
  }

  private void constructCaseCreatedEvent() {
    Address address = ExampleData.createNimrodAddress();
    Contact contact = ExampleData.createLadySallyContact();
    context.caseCreatedPayload =
        ExampleData.createCollectionCase(address, contact, context.caseKey);
  }

  private void constructCaseCreatedEventWales() {
    Address address = ExampleData.createNimrodAddressWales();
    Contact contact = ExampleData.createLadySallyContact();
    context.caseCreatedPayload =
        ExampleData.createCollectionCase(address, contact, context.caseKey);
  }

  private void constructCaseCreatedEventWithDifferentAddress() {
    Address address = ExampleData.createMyHouseAddress();
    Contact contact = ExampleData.createSirLanceContact();
    context.caseCreatedPayload =
        ExampleData.createCollectionCase(address, contact, context.caseKey);
  }

  private void constructCaseCreatedEventWithDifferentAddressWales() {
    Address address = ExampleData.createMyHouseAddressWales();
    Contact contact = ExampleData.createSirLanceContact();
    context.caseCreatedPayload =
        ExampleData.createCollectionCase(address, contact, context.caseKey);
  }

  private void constructUacEventWithNoCaseId(final String formType) {
    context.uacPayload.setUacHash(context.uacKey);
    context.uacPayload.setActive("true");
    context.uacPayload.setQuestionnaireId("3110000010");
    context.uacPayload.setFormType(formType);
  }

  @And("The Token Is Successfully Decrypted")
  public void the_token_is_successfully_decrypted() throws IOException, Exception {
    String returnURL;
    String splitter;
    if (context.eqExists) {
      returnURL = driver.getCurrentUrl();
      splitter = "token=";
    } else {
      returnURL = context.errorMessageContainingCallToEQ;
      splitter = "token%3D";
    }

    if (!returnURL.contains(splitter)) {
      fail("Return URL must contain encrypted token - URL is: " + returnURL);
    }

    String hhEqToken = returnURL.split(splitter)[1].split("&")[0];
    final JweDecryptor decryptor = new JweDecryptor(new KeyStore(keystore));

    String decryptedEqToken = decryptor.decrypt(hhEqToken);

    @SuppressWarnings("unchecked")
    HashMap<String, String> result1 = new ObjectMapper().readValue(decryptedEqToken, HashMap.class);
    List<String> hashKeysFound = new ArrayList<>(result1.keySet());

    assertEquals(
        "Must have the correct number of hash keys",
        HASH_KEYS_EXPECTED.size(),
        hashKeysFound.size());

    List<String> sortedExpectedKeys =
        HASH_KEYS_EXPECTED.stream().sorted().collect(Collectors.toList());
    List<String> sortedFoundKeys = hashKeysFound.stream().sorted().collect(Collectors.toList());
    assertEquals(
        "Expected Keys and Found keys must match",
        sortedExpectedKeys.toString(),
        sortedFoundKeys.toString());

    assertEquals(
        "Must have the correct address",
        "England House, England Street",
        result1.get("display_address").trim());
    assertEquals("Must have the correct channel", "rh", result1.get("channel"));
    assertEquals("Must have the correct case type", "HH", result1.get("case_type"));
    assertEquals("Must have the correct eq id", "CENSUS", result1.get("eq_id").toUpperCase());
    assertEquals("Must have the correct UPRN value", "10023122451", result1.get("ru_ref"));
    assertEquals("Must have the correct language_code value", "en", result1.get("language_code"));
    assertEquals(
        "Must have the correct collection_exercise_sid value",
        "4a6c6e0a-6384-4da8-8c3c-7c56a801f792",
        result1.get("collection_exercise_sid"));
    assertEquals("Must have the correct survey value", "CENSUS", result1.get("survey"));
  }

  @Given("RM constructs a uac where the formType is {string}")
  public void rm_constructs_a_uac_where_the_formType_is(String formType) throws Exception {
    context.uacPayload = new UAC();
    context.uac = validUac();
    context.uacKey = uacHash(context.uac);
    constructUacEventWithNoCaseId(formType);
  }

  @When("RM sends the {string} event to RH via RabbitMQ")
  public void rm_sends_the_event_to_RH_via_RabbitMQ(String uacEventType) throws Exception {
    EventType eventType;
    switch (uacEventType) {
      case "UAC_CREATED":
        eventType = EventType.UAC_CREATED;
        break;
      case "UAC_UPDATED":
        eventType = EventType.UAC_UPDATED;
        break;
      default:
        throw new IllegalArgumentException("Bad test passing uac event type: " + uacEventType);
    }

    rabbit.sendEvent(eventType, Source.SAMPLE_LOADER, Channel.RM, context.uacPayload);
  }

  @Then("a valid uac exists in Firestore but there is no associated case in Firestore")
  public void a_valid_uac_exists_in_Firestore_but_there_is_no_associated_case_in_Firestore()
      throws Exception {
    assertTrue(dataRepo.waitForObject(context.uacCollection, context.uacKey, WAIT_TIMEOUT));
    assertNull(context.uacPayload.getCaseId());
  }

  @Then("I am prompted to supply my address")
  public void i_am_prompted_to_supply_my_address() {
    PleaseSupplyYourAddress pleaseSupplyYourAddress = pages.getPleaseSupplyYourAddress(country);
    WebElement logo = pleaseSupplyYourAddress.getOnsLogo();

    verifyCorrectOnsLogoUsed(logo, country);
    assertEquals(
        "What is your postcode? - title has incorrect text - " + country.name(),
        pleaseSupplyYourAddress.getExpectedText(),
        pleaseSupplyYourAddress.getPleaseSupplyYourAddressTitleText());
  }

  @Given("I enter a postcode {string}, select a valid address, and confirm it")
  public void i_enter_a_postcode_select_a_valid_address_and_confirm_it(String validPostcode) {
    PleaseSupplyYourAddress pleaseSupplyYourAddress = pages.getPleaseSupplyYourAddress();
    String expectedAddress;
    switch (validPostcode) {
      case "EX2 6GA":
        expectedAddress = "2 Gate Reach\nExeter\nEX2 6GA";
        break;
      case "SA38 9NP":
        expectedAddress = "2 Bryn Deri Close\nAdpar\nNewcastle Emlyn\nSA38 9NP";
        break;
      case "BT1 1GH":
        expectedAddress = "Commonwealth House\n" + "35 Castle Street\n" + "Belfast\n" + "BT1 1GH";
        break;
      default:
        expectedAddress = "";
    }

    pleaseSupplyYourAddress.addTextToPostcodeTextBox(validPostcode);
    pleaseSupplyYourAddress.clickContinueButton();

    wait.forLoading();
    SelectYourAddress selectYourAddress = pages.getSelectYourAddress(country);

    verifyCorrectOnsLogoUsed(selectYourAddress.getOnsLogo(), country);

    assertEquals(
        "Select Your Address - title has incorrect text",
        selectYourAddress.getExpectedSelectionText(),
        selectYourAddress.getSelectYourAddressTitleText());

    selectYourAddress.selectSecondBulletPoint();
    selectYourAddress.clickContinueButton();

    wait.forLoading();
    ConfirmAddressToLinkUac confirmAddressToLinkUac = pages.getConfirmAddressToLinkUac(country);

    verifyCorrectOnsLogoUsed(confirmAddressToLinkUac.getOnsLogo(), country);

    assertEquals(
        "Address confirmation for new uac - title has incorrect text",
        confirmAddressToLinkUac.getExpectedConfirmText(),
        confirmAddressToLinkUac.getConfirmAddressTitleText());

    assertEquals(
        "Address confirmation for new uac - address displayed is incorrect",
        expectedAddress,
        confirmAddressToLinkUac.getAddressToConfirmText());

    confirmAddressToLinkUac.clickOptionYes();
    try {
      confirmAddressToLinkUac.clickContinueButton();
    } catch (WebDriverException e) {
      // tolerate no EQ deployment for testing
      context.errorMessageContainingCallToEQ = e.getMessage();
    }
  }

  @When("the UAC is successfully linked, to either a new or existing case, in Firestore")
  public void the_UAC_is_successfully_linked_to_either_a_new_or_existing_case_in_Firestore()
      throws Exception {
    Optional<UAC> uacOptional = dataRepo.readUAC(context.uacKey);
    if (uacOptional.isEmpty()) {
      fail("Failed to retrieve UAC");
    }
    UAC uac = uacOptional.get();
    assertNotNull(uac.getCaseId());
    assertTrue(dataRepo.waitForObject(context.caseCollection, uac.getCaseId(), WAIT_TIMEOUT));
  }

  @Then(
      "a questionnaire linked event is published, which contains the QID and caseId, for the case to be created by RM")
  public void
      a_questionnaire_linked_event_is_published_which_contains_the_QID_and_caseId_for_the_case_to_be_created_by_RM()
          throws Exception {

    assertNewEventHasFired(EventType.QUESTIONNAIRE_LINKED);
  }

  @And("the respondent sees the Household Interstitial page and clicks continue")
  public void the_respondent_sees_the_household_interstitial_page_and_clicks_continue() {
    wait.forLoading();
    HouseholdInterstitial householdInterstitial = pages.getHouseholdInterstitial(country);

    verifyCorrectOnsLogoUsed(householdInterstitial.getOnsLogo(), country);

    assertEquals(
        "Request a new household access code - title has incorrect text",
        householdInterstitial.getExpectedText(),
        householdInterstitial.getHouseholdInterstitialTitleText());

    pages.getHouseholdInterstitial(country).clickContinueButton();
  }

  @Then(
      "the decrypted token contains region_code {string} and form_type {string} and language_code {string}")
  public void the_decrypted_token_contains_region_code_and_form_type_and_language_code(
      String expectedRegionCode, String expectedFormType, String expectedLanguageCode)
      throws Exception {
    String returnURL;
    String splitter;
    if (context.eqExists) {
      returnURL = driver.getCurrentUrl();
      splitter = "token=";
    } else {
      returnURL = context.errorMessageContainingCallToEQ;
      splitter = "token%3D";
    }

    if (!returnURL.contains(splitter)) {
      fail("Return URL must contain encrypted token - URL is: " + returnURL);
    }

    String hhEqToken = returnURL.split(splitter)[1].split("&")[0];
    final JweDecryptor decryptor = new JweDecryptor(new KeyStore(keystore));

    String decryptedEqToken = decryptor.decrypt(hhEqToken);

    @SuppressWarnings("unchecked")
    HashMap<String, String> decryptedKeyValueMap =
        new ObjectMapper().readValue(decryptedEqToken, HashMap.class);

    List<String> hashKeysFound = new ArrayList<>(decryptedKeyValueMap.keySet());

    assertEquals(
        "Must have the correct number of hash keys",
        HASH_KEYS_EXPECTED.size(),
        hashKeysFound.size());

    List<String> sortedExpectedKeys =
        HASH_KEYS_EXPECTED.stream().sorted().collect(Collectors.toList());
    List<String> sortedFoundKeys = hashKeysFound.stream().sorted().collect(Collectors.toList());
    assertEquals(
        "Expected Keys and Found keys must match",
        sortedExpectedKeys.toString(),
        sortedFoundKeys.toString());

    assertEquals(
        "Must have a region code value of " + expectedRegionCode,
        expectedRegionCode,
        decryptedKeyValueMap.get("region_code"));
    assertEquals(
        "Must have a region code value of " + expectedFormType,
        expectedFormType,
        decryptedKeyValueMap.get("form_type"));
    assertEquals(
        "Must have a language code value of " + expectedLanguageCode,
        expectedLanguageCode,
        decryptedKeyValueMap.get("language_code"));
  }

  @Given(
      "I choose Yes and click Continue button on the page asking if I would like to complete the census in English")
  public void
      i_choose_Yes_and_click_button_on_the_page_asking_if_I_would_like_to_complete_the_census_in_English() {
    wait.forLoading();

    WouldYouLikeToCompleteCensusInEnglish wouldYou =
        pages.getWouldYouLikeToCompleteCensusInEnglish();

    verifyCorrectOnsLogoUsed(wouldYou.getOnsLogo(), country);

    wouldYou.clickOptionYes();

    try {
      wouldYou.clickContinueButton();
    } catch (Exception e) {
      // tolerate no EQ deployment for testing
      context.errorMessageContainingCallToEQ = e.getMessage();
    }
  }

  @And("I enter a postcode {string}")
  public void i_enter_postcode(String postcode) {
    PleaseSupplyYourAddress pleaseSupplyYourAddress = pages.getPleaseSupplyYourAddress(country);
    pleaseSupplyYourAddress.addTextToPostcodeTextBox(postcode);
    pleaseSupplyYourAddress.clickContinueButton();
  }

  @And("select \"I can't find my address\", and click \"Continue\"")
  public void select_cant_find_my_address_and_confirm_it() {
    SelectYourAddress selectYourAddress = pages.getSelectYourAddress(country);
    selectYourAddress.selectCannotFindAddressBulletPoint();
    selectYourAddress.clickContinueButton();
  }

  @Then(
      "I am presented with a page to call the Census Customer Contact Centre with the correct telephone number")
  public void i_am_presented_with_a_page_telling_me_to_call_Census_Customer_Contact_centre() {
    RegisterYourAddress registerYourAddress = pages.getRegisterYourAddress(country);
    String pageTitle = registerYourAddress.getTitleText();
    String textWithPhoneNumber = registerYourAddress.getTextWithPhoneNumber();

    assertEquals(registerYourAddress.getExpectedTitleText(), pageTitle);
    assertEquals(registerYourAddress.getExpectedTextWithPhoneNumber(), textWithPhoneNumber);
  }

  @Given("I click on \"request a new access code\" in the start page {}")
  public void i_click_onrequest_a_new_access_code_in_the_start_page(Country country) {
    this.country = country;
    StartPage startPage = pages.getStartPage(country);
    startPage.clickRequestNewCodeLink();
  }

  public static boolean eqExists(final RhStepsContext context) {
    boolean eqExists = false;
    if (context.errorMessageContainingCallToEQ == null) {
      try {
        new CensusQuestionnaire().clickCensusLogo();
        eqExists = true;
      } catch (TimeoutException toe) {
        // tolerate no EQ deployment for testing
      }
    } else {
      String devTextToFind = "/session/%3Ftoken";
      String localTextToFind = "/session%3Ftoken";
      assertTrue(
          context.errorMessageContainingCallToEQ.contains(devTextToFind)
              || context.errorMessageContainingCallToEQ.contains(localTextToFind));
    }
    return eqExists;
  }

  @Given(
      "SETUP-1 - a valid uac exists in Firestore but there is no associated case in Firestore {string} {string} {}")
  public void setupAValidUacExistsInFirestoreButThereIsNoAssociatedCaseInFirestore(
      String formType, String eventType, Country country) throws Exception {
    setupTest(country);
    rm_constructs_a_uac_where_the_formType_is(formType);
    rm_sends_the_event_to_RH_via_RabbitMQ(eventType);
    a_valid_uac_exists_in_Firestore_but_there_is_no_associated_case_in_Firestore();
  }

  @Given(
      "SETUP-2 - a valid uac exists in Firestore and there is an associated case in Firestore {}")
  public void setupAValidUacExistsInFirestoreAndThereIsAnAssociatedCaseInFirestore(Country country)
      throws Exception {
    setupAValidUacExistsInFirestoreAndThereIsAnAssociatedCaseInFirestore(
        country, EventType.UAC_UPDATED.name());
  }

  @Given(
      "SETUP-3 - a valid uac exists in Firestore and there is an associated case in Firestore {} eventType {string}")
  public void setupAValidUacExistsInFirestoreAndThereIsAnAssociatedCaseInFirestore(
      Country country, String uacEventType) throws Exception {
    setupTest(country);
    rm_constructs_a_case_created_event_and_a_uac_updated_event();
    rm_sends_the_case_created_and_uac_events_to_RH_via_RabbitMQ(uacEventType);
    a_valid_uac_exists_in_Firestore_ready_for_use_by_a_respondent();
  }

  @Given("SETUP-4 - a valid uac and associated case exist in Firestore with region {}")
  public void setupAValidUacExistsInFirestoreAndThereIsAnAssociatedCaseInFirestoreWithRegion(
      Country country) throws Exception {
    setupTest(country);
    if (country == Country.WALES) {
      rm_constructs_a_case_created_event_and_a_uac_updated_event_where_Region_is_W();
    } else {
      rm_constructs_a_case_created_event_and_a_uac_updated_event();
    }
    rm_sends_the_case_created_and_uac_events_to_RH_via_RabbitMQ(EventType.UAC_UPDATED.name());
    a_valid_uac_exists_in_Firestore_ready_for_use_by_a_respondent();
  }

  @And("the respondentAuthenticatedHeader contains the correct values")
  public void theRespondentAuthenticatedHeaderContainsTheCorrectValues() {
    the_respondentAuthenticatedHeader_contains_a_Type_with_value("RESPONDENT_AUTHENTICATED");
    the_respondentAuthenticatedHeader_contains_a_Source_with_value("RESPONDENT_HOME");
    the_respondentAuthenticatedHeader_contains_a_Channel_with_value("RH");
    the_respondentAuthenticatedHeader_contains_a_DateTime_value_that_is_not_null();
    the_respondentAuthenticatedHeader_contains_a_TransactionId_value_that_is_not_null();
    the_respondentAuthenticatedPayload_contains_a_Response();
    the_respondentAuthenticatedResponse_contains_a_QuestionnaireId_value_that_is_not_null();
    // the_respondentAuthenticatedResponse_contains_a_CaseId_value_that_may_be_null();
  }

  @And("the surveyLaunchedHeader contains the correct values")
  public void theSurveyLaunchedHeaderContainsTheCorrectValues() {
    the_surveyLaunchedHeader_contains_a_Type_with_value("SURVEY_LAUNCHED");
    the_surveyLaunchedHeader_contains_a_Source_with_value("RESPONDENT_HOME");
    the_surveyLaunchedHeader_contains_a_Channel_with_value("RH");
    the_surveyLaunchedHeader_contains_a_DateTime_value_that_is_not_null();
    the_surveyLaunchedHeader_contains_a_TransactionId_value_that_is_not_null();
    the_surveyLaunchedPayload_contains_a_Response();
    the_surveyLaunchedResponse_contains_a_QuestionnaireId_value_that_is_not_null();
    the_surveyLaunchedResponse_contains_a_CaseId_value_that_may_be_null();
    the_surveyLaunchedResponse_contains_an_AgentId_value_that_may_be_null();
  }

  @And("I am a respondent and I am on the AD Start Page with {string}")
  public void iAmARespondentAndIAmOnTheADStartPageWith(String locationId) {
    pages.getADStartPage(country, locationId);
  }

  @And("the surveyLaunched contains the {string} and {string}")
  public void theSurveyLaunchedContainsThe(String channel, String locationId) {

    the_surveyLaunchedHeader_contains_a_Channel_with_value(channel);
    SurveyLaunchedPayload payload = context.surveyLaunchedEvent.getPayload();
    assertEquals(
        "The SurveyLaunchedPayload contains a incorrect value of location Id'",
        locationId,
        payload.getResponse().getAgentId());
  }

  @Given("I am a respondent and I am on the Web form Page")
  public void iAmARespondentAndIAmOnTheWebFormPageCountry() {
    pages.getWebFormStartPage();
  }

  @And("then I entered all the required details and click Send Message on the Web form")
  public void theIHaveEnteredAllTheRequiredDetailsOnTheWebForm() throws Exception {
    driver = pages.getWebDriver();
    WebFormPage webformPage = pages.getWebFormStartPage();
    webformPage.addEmailAddress("johnwick@covid19.com");
    webformPage.addName("John Wick");
    webformPage.addTextToMoreDetailTextBox("Some random text");
    webformPage.clickOptionCountry();
    webformPage.clickOptionQueryType();
    webformPage.clickSendMessage();
  }

  @Then("the endpoint sends the email")
  public void theEndpointSendsTheEmailToGovUKNotify() {
    WebFormPage webformPage = pages.getWebFormSuccessPage();
    assertEquals(
        "Thank you for contacting us - title has incorrect text",
        webformPage.getExpectedContactUsText(),
        webformPage.getContactUsText());
    assertEquals(
        "You message has been sent - message has incorrect text",
        webformPage.getExpectedSentText(),
        webformPage.getMessageSentText());
  }

  @Given("the respondent selects continue on the confirm your mobile page {string} {}")
  public void the_respondent_selects_continue_on_confirm_your_mobile_page(
      String postcode, Country country) throws Exception {
    emptyEventQueue(EventType.FULFILMENT_REQUESTED);

    the_respondent_confirms_the_address(country, postcode);
    the_respondent_sees_the_household_interstitial_page_and_clicks_continue();
    the_respondent_selects_the_delivery_channel_as_Text_message();
    the_respondent_enters_their_valid_UK_mobile_telephone_number();
    the_respondents_selects_continue_text();
  }

  @Given("the respondent selects continue on the confirm postal address page {string} {}")
  public void the_respondent_selects_continue_on_confirm_postal_address_page(
      String postcode, Country country) throws Exception {
    emptyEventQueue(EventType.FULFILMENT_REQUESTED);
    the_respondent_confirms_the_address(country, postcode);
    the_respondent_sees_the_household_interstitial_page_and_clicks_continue();
    the_respondent_selects_the_delivery_channel_as_Post();
    the_respondent_enters_their_first_and_last_name();
    the_respondent_confirms_the_address_by_selecting_Yes_send_the_access_code_by_post();
    selects_continue();
  }

  @When("RHSVC publishes a fulfilment request")
  public void RHSVC_publishes_a_fulfilment_request() throws Exception {
    assertNewFulfilmentEventHasFired();
  }

  @Then("check the fulfilment code is {string}")
  public void check_the_fulfilment_code_is(String expectedFulfilmentCode) {
    assertEquals(
        "FulfilmentCode is not " + expectedFulfilmentCode,
        expectedFulfilmentCode,
        context.fulfilmentRequestedCode);
  }

  @And("limit exceed message will displayed")
  public void limitExccedMessageWillDisplayed() {
    LimitExceedPage limitExceedPage = pages.getLimitExceedPage();
    assertEquals(
        "Limit Exceed Message - Message is incorrect",
        limitExceedPage.getExpectedMessage(),
        limitExceedPage.getMessage());
  }

  private void confirm_your_address(Country country, String postCode) throws Exception {
    emptyEventQueue(EventType.NEW_ADDRESS_REPORTED);
    emptyEventQueue(EventType.FULFILMENT_REQUESTED);

    WhatIsYourPostcode whatIsYourPostcode = pages.getWhatIsYourPostcode(country);
    whatIsYourPostcode.addTextToPostcodeTextBox(postCode);
    whatIsYourPostcode.clickContinueButton();

    SelectYourAddress selectYourAddress = pages.getSelectYourAddress(country);
    selectYourAddress.selectFirstBulletPoint();
    selectYourAddress.clickContinueButton();

    ConfirmAddress confirmAddress = pages.getConfirmAddress(country);
    confirmAddress.clickOptionYes();
    confirmAddress.clickContinueButton();
  }

  private void emptyEventQueue(EventType eventType) throws Exception {
    String queueName = rabbit.createQueue(eventType);
    rabbit.flushQueue(queueName);
  }

  private void assertNewEventHasFired(EventType eventType) throws Exception {

    final GenericEvent event =
        (GenericEvent)
            rabbit.getMessage(
                EventPublisher.RoutingKey.forType(eventType).getKey(),
                eventClass(eventType),
                RABBIT_TIMEOUT);

    assertNotNull(event);
    assertNotNull(event.getEvent());
  }

  private void assertNewRespondantAuthenticatedEventHasFired() throws Exception {

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

  private void assertNewSurveyLaunchedEventHasFired() throws Exception {
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

  private void assertNewFulfilmentEventHasFired() throws Exception {
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

  private Class<?> eventClass(EventType eventType) {
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
}
