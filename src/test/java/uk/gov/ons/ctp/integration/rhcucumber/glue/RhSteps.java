package uk.gov.ons.ctp.integration.rhcucumber.glue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import uk.gov.ons.ctp.common.event.EventPublisher.Channel;
import uk.gov.ons.ctp.common.event.EventPublisher.EventType;
import uk.gov.ons.ctp.common.event.EventPublisher.Source;
import uk.gov.ons.ctp.common.util.Wait;
import uk.gov.ons.ctp.integration.eqlaunch.crypto.JweDecryptor;
import uk.gov.ons.ctp.integration.eqlaunch.crypto.KeyStore;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.SocialQuestionnaire;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.ConfirmAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.ConfirmAddressForNewUac;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.HouseholdInterstitial;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.IsThisMobileNumCorrect;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PleaseSupplyYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.RegisterYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.SelectDeliveryMethodTextOrPost;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.SelectYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.SentAccessCode;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.StartPage;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.WhatIsYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.WhatIsYourMobile;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.WhatIsYourName;

public class RhSteps extends StepsBase {
  private Wait wait;
  private Country country;

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

  private void initTest(Country country) throws Exception {
    setupNoCountry();
    setupTest(country);
  }

  private void setupTest(Country country) {
    this.country = country;
    pages.getStartPage(country);
  }

  @After("@TearDown")
  public void deleteDriver() {
    super.closeDriver();
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
    confirmYourAddress(country, postCode);
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
  public void i_am_a_respondent_and_I_am_on_the_RH_Start_Page(Country country) {
    setupTest(country);
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

  @Then("I am directed to the Questionnaire")
  public void i_am_directed_to_the_Questionnaire() {
    context.eqExists = eqExists();
  }

  @Given("an empty queue exists for sending Respondent Authenticated events")
  public void an_empty_queue_exists_for_sending_Respondent_Authenticated_events() throws Exception {
    emptyEventQueue(EventType.RESPONDENT_AUTHENTICATED);
  }

  @Given("an empty queue exists for sending Survey Launched events")
  public void an_empty_queue_exists_for_sending_Survey_Launched_events() throws Exception {
    emptyEventQueue(EventType.SURVEY_LAUNCHED);
  }

  @Given("an empty queue exists for sending Fulfilment Requested events")
  public void an_empty_queue_exists_for_sending_Fulfilment_Requested_events() throws Exception {
    emptyEventQueue(EventType.FULFILMENT_REQUESTED);
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

  @Given("I click on request a new access code")
  public void i_click_on_request_a_new_access_code() {
    pages.getStartPage().clickRequestNewCodeLink();
  }

  @Then("I am presented with a page to enter my postcode on")
  public void i_am_presented_with_a_page_to_enter_my_postcode_on() {
    WhatIsYourAddress page = pages.getWhatIsYourAddress(country);
    verifyCorrectOnsLogoUsed(page.getOnsLogo(), country);
    assertEquals(
        "What is your postcode - title has incorrect text",
        page.getExpectedTitleText(),
        page.getwhatIsYourAddressTitleText());
  }

  @Given("I enter the valid UK postcode {string}")
  public void i_enter_the_valid_UK_postcode(String validPostcode) {
    pages.getWhatIsYourAddress().addTextToAddressTextBox(validPostcode);
  }

  @Given("select Continue")
  public void select_Continue() {
    pages.getWhatIsYourAddress().clickContinueButton();
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
    SelectYourAddress page = pages.getSelectYourAddress(country);
    page.selectFirstBulletPoint();
    page.clickContinueButton();
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

  @When("I select the ‘YES, This address is correct’ option and click continue")
  public void i_select_the_YES_This_address_is_correct_option_and_click_continue() {
    ConfirmAddressForNewUac page = pages.getConfirmAddressForNewUac(country);
    page.clickOptionYes();
    page.clickContinueButton();
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
    ConfirmAddressForNewUac page = pages.getConfirmAddressForNewUac(country);
    page.clickOptionNo();
    page.clickContinueButton();
  }

  @Given("I enter my mobile number and click continue")
  public void i_enter_my_mobile_number_and_click_continue() {
    WhatIsYourMobile page = pages.getWhatIsYourMobile(country);
    page.addTextToMobileNumBox("07700 900345");
    page.clickContinueButton();
  }

  @Given("I am presented with a page to confirm my mobile number on")
  public void i_am_presented_with_a_page_to_confirm_my_mobile_number_on() {
    IsThisMobileNumCorrect page = pages.getIsThisMobileNumCorrect(country);
    verifyCorrectOnsLogoUsed(page.getOnsLogo(), country);

    assertEquals(
        "Is this mobile number correct - title has incorrect text",
        page.getExpectedText(),
        page.getIsMobileCorrectTitleText());
  }

  @When("I select the Yes option to send the text now")
  public void i_select_the_Yes_option_to_send_the_text_now() {
    IsThisMobileNumCorrect page = pages.getIsThisMobileNumCorrect();
    page.clickOptionYes();
    page.clickContinueButton();
  }

  @When("I select the No option \\(because the number displayed is not correct)")
  public void i_select_the_No_option_because_the_number_displayed_is_not_correct() {
    IsThisMobileNumCorrect page = pages.getIsThisMobileNumCorrect();
    page.clickOptionNo();
    page.clickContinueButton();
  }

  @Then("I am presented with a message saying that I have been sent an access code")
  public void i_am_presented_with_a_message_saying_that_I_have_been_sent_an_access_code() {
    SentAccessCode page = pages.getSentAccessCode(country);

    verifyCorrectOnsLogoUsed(page.getOnsLogo(), country);
    assertEquals(
        "Sent Access Code - title has incorrect text - " + country.name(),
        page.getExpectedText(),
        page.getSentAccessCodeTitleText());
  }

  @Then("I am presented with a button {string}")
  public void i_am_presented_with_a_button(String expectedLabel) {
    SentAccessCode page = pages.getSentAccessCode(country);
    String buttonLabelFound = page.getStartSurveyButtonText();
    assertEquals(
        "button label found is incorrect - " + country.name(), expectedLabel, buttonLabelFound);
  }

  @Then("I am also presented with a link to request a new access code")
  public void i_am_also_presented_with_a_link_to_request_a_new_code() {
    SentAccessCode page = pages.getSentAccessCode();
    assertEquals(
        "link text found is incorrect",
        page.getExpectedRequestCodeText(),
        page.getRequestNewCodeLinkText());
  }

  @When("I enter an invalid mobile number and click continue")
  public void i_enter_an_invalid_mobile_number_and_click_continue() {
    WhatIsYourMobile page = pages.getWhatIsYourMobile(country);
    page.addTextToMobileNumBox("1234567");
    page.clickContinueButton();
  }

  @Then("an invalid mobile number error message is displayed")
  public void an_invalid_mobile_number_error_message_is_displayed() {
    WhatIsYourMobile page = pages.getWhatIsYourMobile(country);
    assertEquals(
        "invalid mobile error message found is incorrect",
        page.getExpectedErrorText(),
        page.getInvalidMobileErrorText());
  }

  @And("The Token Is Successfully Decrypted")
  public void the_token_is_successfully_decrypted() throws Exception {
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

  @And("the respondent sees the Household Interstitial page and clicks continue")
  public void the_respondent_sees_the_household_interstitial_page_and_clicks_continue() {
    wait.forLoading();
    HouseholdInterstitial page = pages.getHouseholdInterstitial(country);
    verifyCorrectOnsLogoUsed(page.getOnsLogo(), country);
    assertEquals(
        "Request a new household access code - title has incorrect text",
        page.getExpectedText(),
        page.getHouseholdInterstitialTitleText());

    pages.getHouseholdInterstitial(country).clickContinueButton();
  }

  @And("I enter a postcode {string}")
  public void i_enter_postcode(String postcode) {
    PleaseSupplyYourAddress page = pages.getPleaseSupplyYourAddress(country);
    page.addTextToPostcodeTextBox(postcode);
    page.clickContinueButton();
  }

  @And("select \"I can't find my address\", and click \"Continue\"")
  public void select_cant_find_my_address_and_confirm_it() {
    SelectYourAddress page = pages.getSelectYourAddress(country);
    page.selectCannotFindAddressBulletPoint();
    page.clickContinueButton();
  }

  @Then(
      "I am presented with a page to call the Customer Contact Centre with the correct telephone number")
  public void i_am_presented_with_a_page_telling_me_to_call_Customer_Contact_centre() {
    RegisterYourAddress page = pages.getRegisterYourAddress(country);
    String pageTitle = page.getTitleText();
    String textWithPhoneNumber = page.getTextWithPhoneNumber();

    assertEquals(page.getExpectedTitleText(), pageTitle);
    assertEquals(page.getExpectedTextWithPhoneNumber(), textWithPhoneNumber);
  }

  @Given("I click on \"request a new access code\" in the start page {}")
  public void i_click_onrequest_a_new_access_code_in_the_start_page(Country country) {
    this.country = country;
    StartPage page = pages.getStartPage(country);
    page.clickRequestNewCodeLink();
  }

  private boolean eqExists() {
    boolean eqExists = false;
    if (context.errorMessageContainingCallToEQ == null) {
      try {
        new SocialQuestionnaire().clickSocialLogo();
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
    prepareCaseAndUacEvents();
    inboundCaseAndUacEvents(uacEventType);
    verifyUacProcessed();
  }

  @Given("SETUP-4 - a valid uac and associated case exist in Firestore with region {}")
  public void setupAValidUacExistsInFirestoreAndThereIsAnAssociatedCaseInFirestoreWithRegion(
      Country country) throws Exception {
    setupTest(country);
    if (country == Country.WALES) {
      prepareWelshCaseAndUacEvents();
    } else {
      prepareCaseAndUacEvents();
    }
    inboundCaseAndUacEvents(EventType.UAC_UPDATED.name());
    verifyUacProcessed();
  }

  private void prepareCaseAndUacEvents() {
    constructCaseCreatedEvent();
    constructUacUpdatedEvent();
  }

  private void prepareWelshCaseAndUacEvents() {
    constructCaseCreatedEventWales();
    constructUacUpdatedEvent();
  }

  private void inboundCaseAndUacEvents(String uacEventType) throws Exception {

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

  private void verifyUacProcessed() throws Exception {
    assertTrue(dataRepo.waitForObject(context.caseCollection, context.caseKey, WAIT_TIMEOUT));
    assertTrue(dataRepo.waitForObject(context.uacCollection, context.uacKey, WAIT_TIMEOUT));
  }

  @And("the respondentAuthenticatedHeader contains the correct values")
  public void theRespondentAuthenticatedHeaderContainsTheCorrectValues() {
    respondentAuthenticatedHeaderContainsType("RESPONDENT_AUTHENTICATED");
    respondentAuthenticatedHeaderContainsSource("RESPONDENT_HOME");
    respondentAuthenticatedHeaderContainsChannel("RH");
    assertNotNull(context.respondentAuthenticatedHeader.getDateTime());
    assertNotNull(context.respondentAuthenticatedHeader.getTransactionId());
    respondentAuthenticatedPayloadHasResponse();
    assertNotNull(context.respondentAuthenticatedResponse.getQuestionnaireId());
  }

  @And("the surveyLaunchedHeader contains the correct values")
  public void theSurveyLaunchedHeaderContainsTheCorrectValues() {
    surveyLaunchedHeaderContainsType("SURVEY_LAUNCHED");
    surveyLaunchedHeaderContainsSource("RESPONDENT_HOME");
    surveyLaunchedHeaderContainsChannel("RH");
    assertNotNull(context.surveyLaunchedHeader.getDateTime());
    assertNotNull(context.surveyLaunchedHeader.getTransactionId());
    surveyLaunchedPayloadHasResponse();
    assertNotNull(context.surveyLaunchedResponse.getQuestionnaireId());
  }

  private void respondentAuthenticatedHeaderContainsType(String expectedType) {
    EventType type = context.respondentAuthenticatedHeader.getType();
    String strType = type.name();
    assertEquals(
        "The RespondentAuthenticatedEvent contains a incorrect value of 'type'",
        expectedType,
        strType);
  }

  private void surveyLaunchedHeaderContainsType(String expectedType) {
    EventType type = context.surveyLaunchedHeader.getType();
    String strType = type.name();
    assertEquals(
        "The SurveyLaunchedEvent contains a incorrect value of 'type'", expectedType, strType);
  }

  private void respondentAuthenticatedHeaderContainsSource(String expectedSource) {
    Source source = context.respondentAuthenticatedHeader.getSource();
    String strSource = source.name();
    assertEquals(
        "The RespondentAuthenticatedEvent contains a incorrect value of 'source'",
        expectedSource,
        strSource);
  }

  private void surveyLaunchedHeaderContainsSource(String expectedSource) {
    Source source = context.surveyLaunchedHeader.getSource();
    String strSource = source.name();
    assertEquals(
        "The SurveyLaunchedEvent contains a incorrect value of 'source'",
        expectedSource,
        strSource);
  }

  private void respondentAuthenticatedHeaderContainsChannel(String expectedChannel) {
    Channel channel = context.respondentAuthenticatedHeader.getChannel();
    String strChannel = channel.name();
    assertEquals(
        "The ResondentAuthenticatedEvent contains a incorrect value of 'channel'",
        expectedChannel,
        strChannel);
  }

  private void surveyLaunchedHeaderContainsChannel(String expectedChannel) {
    Channel channel = context.surveyLaunchedHeader.getChannel();
    String strChannel = channel.name();
    assertEquals(
        "The SurveyLaunchedEvent contains a incorrect value of 'channel'",
        expectedChannel,
        strChannel);
  }

  private void respondentAuthenticatedPayloadHasResponse() {
    context.respondentAuthenticatedResponse = context.respondentAuthenticatedPayload.getResponse();
    assertNotNull(context.respondentAuthenticatedResponse);
  }

  private void surveyLaunchedPayloadHasResponse() {
    context.surveyLaunchedResponse = context.surveyLaunchedPayload.getResponse();
    assertNotNull(context.surveyLaunchedResponse);
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

  private void confirmYourAddress(Country country, String postCode) throws Exception {
    emptyEventQueue(EventType.NEW_ADDRESS_REPORTED);
    emptyEventQueue(EventType.FULFILMENT_REQUESTED);

    WhatIsYourAddress whatIsYourPostcode = pages.getWhatIsYourAddress(country);
    whatIsYourPostcode.addTextToAddressTextBox(postCode);
    whatIsYourPostcode.clickContinueButton();

    SelectYourAddress selectYourAddress = pages.getSelectYourAddress(country);
    selectYourAddress.selectFirstBulletPoint();
    selectYourAddress.clickContinueButton();

    ConfirmAddress confirmAddress = pages.getConfirmAddress(country);
    confirmAddress.clickOptionYes();
    confirmAddress.clickContinueButton();
  }
}
