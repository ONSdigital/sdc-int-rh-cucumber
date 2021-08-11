package uk.gov.ons.ctp.integration.rhcucumber.glue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriverException;
import uk.gov.ons.ctp.common.event.EventPublisher.Channel;
import uk.gov.ons.ctp.common.event.EventPublisher.EventType;
import uk.gov.ons.ctp.common.event.EventPublisher.Source;
import uk.gov.ons.ctp.common.util.Wait;
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
  public void confirmAddress(Country country, final String postCode) throws Exception {
    this.country = country;
    StartPage startPage = pages.getStartPage(country);
    startPage.clickRequestNewCodeLink();
    confirmYourAddress(country, postCode);
  }

  @And("the respondent selects the delivery channel as \"Text message\"")
  public void selectTextMessageOption() {
    pages.getSelectDeliveryMethodTextOrPost(country).clickOptionText();
    pages.getSelectDeliveryMethodTextOrPost(country).clickContinueButton();
  }

  @And("the respondents enters their valid UK mobile telephone number")
  public void enterValidMobilePhoneNumber() {
    WhatIsYourMobile whatIsYourMobile = pages.getWhatIsYourMobile(country);
    whatIsYourMobile.addTextToMobileNumBox("07700 900345");
    whatIsYourMobile.clickContinueButton();
  }

  @When("the respondents selects continue \"text\"")
  public void selectContinueWithText() {
    IsThisMobileNumCorrect isThisMobileNumCorrect = pages.getIsThisMobileNumCorrect(country);
    isThisMobileNumCorrect.clickOptionYes();
    isThisMobileNumCorrect.clickContinueButton();
  }

  @Then("RHSVC publishes a new address event")
  public void verifyNewAddressEventPublished() throws Exception {
    assertNewEventHasFired(EventType.NEW_ADDRESS_REPORTED);
  }

  @And("RHSVC publishes a UAC fulfilment request")
  public void verifyUacFulfimentRequestPublished() throws Exception {
    assertNewEventHasFired(EventType.FULFILMENT_REQUESTED);
  }

  @And("the respondent selects the delivery channel as \"Post\"")
  public void selectPostOption() {
    SelectDeliveryMethodTextOrPost selectDeliveryMethodTextOrPost =
        pages.getSelectDeliveryMethodTextOrPost(country);
    selectDeliveryMethodTextOrPost.clickOptionPost();
    selectDeliveryMethodTextOrPost.clickContinueButton();
  }

  @And("the respondent enters their first and last name")
  public void enterFullName() {
    WhatIsYourName whatIsYourName = pages.getWhatIsYourName(country);
    whatIsYourName.addTextToFirstNameTextBox("ray");
    whatIsYourName.addTextToLastNameTextBox("vis");
    whatIsYourName.clickContinueButton();
  }

  @When("the respondent confirms the address by selecting \"Yes, send the access code by post\"")
  public void confirmSendByPost() {
    pages.getNewHouseholdAccessCode(country).selectYesSendByPost();
  }

  @And("selects continue")
  public void selectContinueToGetHouseholdAccessCode() {
    pages.getNewHouseholdAccessCode(country).clickContinueButton();
  }

  @Given("I am a respondent and I am on the RH Start Page {}")
  public void selectTheStartPage(Country country) {
    setupTest(country);
  }

  @Given("I enter the valid UAC into the text box")
  public void enterValidUac() {
    pages.getStartPage().enterUac(context.uac);
  }

  @Given("I click the “Access survey” button")
  public void clickAccessSurveyButton() {
    pages.getStartPage().clickAccessSurveyButton();
  }

  @Then("I am presented with a page to confirm my address")
  public void verifyConfirmMyAddress() {
    ConfirmAddress confirmAddress = pages.getConfirmAddress(country);
    verifyCorrectOnsLogoUsed(confirmAddress.getOnsLogo(), country);

    assertEquals(
        "address confirmation title has incorrect text",
        pages.getConfirmAddress().getExpectedConfirmText(),
        pages.getConfirmAddress().getConfirmAddressTitleText());
  }

  @Then("a blank uac error {string} appears")
  public void verifyBlankUacError(String errorMessage) {
    assertEquals(
        "The error message shown has incorrect text",
        errorMessage,
        pages.getStartPage().getErrorEnterAccessCodeText());
  }

  @Given("I enter an invalid UAC {string} into the text box")
  public void enterInvalidUac(String uac) {
    pages.getStartPage().enterUac(uac);
  }

  @Then("an invalid uac error {string} appears")
  public void verifyInvalidUacError(String errorMessage) {
    assertEquals(
        "The error message shown has incorrect text",
        errorMessage,
        pages.getStartPage().getErrorEnterValidCodeText());
  }

  @Given("I select the “Yes, this address is correct” option")
  public void confirmAddressIsCorrect() {
    pages.getConfirmAddress(country).clickOptionYes();
  }

  @Given("I click the “Continue” button")
  public void clickContinueAfterConfirmAddress() {
    wait.forLoading(1);
    try {
      pages.getConfirmAddress(country).clickContinueButton();
    } catch (WebDriverException e) {
      // tolerate no EQ deployment for testing
      context.errorMessageContainingCallToEQ = e.getMessage();
    }
  }

  @Then("I am presented with a page asking text or post")
  public void pageAsksForTextOrPost() {
    SelectDeliveryMethodTextOrPost selectDeliveryMethodTextOrPost =
        pages.getSelectDeliveryMethodTextOrPost(country);
    verifyCorrectOnsLogoUsed(selectDeliveryMethodTextOrPost.getOnsLogo(), country);

    assertEquals(
        "select delivery method title has incorrect text",
        pages.getSelectDeliveryMethodTextOrPost().getExpectedSelectDeliveryMethodTextOrPostText(),
        pages.getSelectDeliveryMethodTextOrPost().getSelectDeliveryMethodTextOrPostTitleText());
  }

  @Given("I select 'Text message' and click 'Continue'")
  public void selectTextMessage() {
    pages.getSelectDeliveryMethodTextOrPost(country).clickOptionText();
    pages.getSelectDeliveryMethodTextOrPost(country).clickContinueButton();
  }

  @Then("I am directed to the Questionnaire")
  public void clickForQuestionnaire() {
    EqValidator.clickThoughToEq(context);
  }

  @Given("an empty queue exists for sending Respondent Authenticated events")
  public void emptyEventQueueForRespondentAuthenticated() throws Exception {
    emptyEventQueue(EventType.RESPONDENT_AUTHENTICATED);
  }

  @Given("an empty queue exists for sending Survey Launched events")
  public void emptyEventQueuForSurveyLaunched() throws Exception {
    emptyEventQueue(EventType.SURVEY_LAUNCHED);
  }

  @Given("an empty queue exists for sending Fulfilment Requested events")
  public void emptyEventQueueForFulfilmentRequested() throws Exception {
    emptyEventQueue(EventType.FULFILMENT_REQUESTED);
  }

  @Then("a Respondent Authenticated event is sent to RM")
  public void verifyRespondentAuthenticatedEventSent() throws Exception {
    assertNewRespondantAuthenticatedEventHasFired();
  }

  @Then("a Survey Launched event is sent to RM")
  public void verifySurveyLaunchedEventSent() throws Exception {
    assertNewSurveyLaunchedEventHasFired();
  }

  @Then("a FulfilmentRequested event is sent to RM")
  public void verifyFulfilmentRequestedEventSent() throws Exception {
    assertNewEventHasFired(EventType.FULFILMENT_REQUESTED);
  }

  @Given("I click on request a new access code")
  public void clickRequestNewAccessCode() {
    pages.getStartPage().clickRequestNewCodeLink();
  }

  @Then("I am presented with a page to enter my postcode on")
  public void verifypageForPostcodeEntry() {
    WhatIsYourAddress page = pages.getWhatIsYourAddress(country);
    verifyCorrectOnsLogoUsed(page.getOnsLogo(), country);
    assertEquals(
        "What is your postcode - title has incorrect text",
        page.getExpectedTitleText(),
        page.getwhatIsYourAddressTitleText());
  }

  @Given("I enter the valid UK postcode {string}")
  public void enterValidPostcode(String validPostcode) {
    pages.getWhatIsYourAddress().addTextToAddressTextBox(validPostcode);
  }

  @Given("select Continue")
  public void selectContinueOnAddressPage() {
    pages.getWhatIsYourAddress().clickContinueButton();
  }

  @Then("I am presented with a page to select an address from")
  public void verifyPageToSelectAddress() {
    SelectYourAddress page = pages.getSelectYourAddress(country);
    wait.forLoading();
    verifyCorrectOnsLogoUsed(page.getOnsLogo(), country);
    assertEquals(
        "Select Your Address - title has incorrect text",
        page.getExpectedSelectionText(),
        page.getSelectYourAddressTitleText());
  }

  @Then("a number of addresses is displayed")
  public void severalAddressesAreDisplayed() {
    Document document = Jsoup.parse(pages.getWebDriver().getPageSource());
    SelectYourAddress page = pages.getSelectYourAddress(country);
    Elements radioButtons =
        document.body().getElementsByAttributeValue("name", "form-pick-address");
    Element lastItem = radioButtons.attr("id", "xxxx").first();
    radioButtons.remove(lastItem); // last item has ID xxxx

    int numberOfAddresses = radioButtons.size();
    assertTrue(radioButtons.size() > 0);

    String expectedSelectionListText =
        page.getExpectedSelectionListText1()
            + numberOfAddresses
            + page.getExpectedSelectionListText2()
            + " "
            + page.getExpectedPostcode();

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
  public void selectAddressAndContinue(final String address) {
    SelectYourAddress page = pages.getSelectYourAddress(country);
    page.selectFirstBulletPoint();
    page.clickContinueButton();
  }

  @Then("I am presented with a page displaying the address {string}")
  public void addressIsDisplayed(String addressToBeDisplayed) {
    wait.forLoading();
    ConfirmAddressForNewUac page = pages.getConfirmAddressForNewUac(country);
    verifyCorrectOnsLogoUsed(page.getOnsLogo(), country);

    assertEquals(
        "Address confirmation for new uac - title has incorrect text",
        page.getExpectedConfirmText(),
        page.getConfirmAddressTitleText());

    assertEquals(
        "Address confirmation for new uac - address displayed is incorrect",
        page.getExpectedAddress(),
        page.getAddressToConfirmText());
  }

  @When("I select the ‘YES, This address is correct’ option and click continue")
  public void confirmAddressAndContinue() {
    ConfirmAddressForNewUac page = pages.getConfirmAddressForNewUac(country);
    page.clickOptionYes();
    page.clickContinueButton();
  }

  @Then("I am presented with a page asking for my mobile number")
  public void pageAsksForMobileNumber() {
    wait.forLoading();
    WhatIsYourMobile page = pages.getWhatIsYourMobile(country);
    verifyCorrectOnsLogoUsed(page.getOnsLogo(), country);
    assertEquals(
        "What is Your Mobile - title has incorrect text - " + country.name(),
        page.getExpectedText(),
        page.getWhatIsYourMobileTitleText());
  }

  @When("I select the ‘No, I need to change the address’ option and click continue")
  public void rejectAddress() {
    ConfirmAddressForNewUac page = pages.getConfirmAddressForNewUac(country);
    page.clickOptionNo();
    page.clickContinueButton();
  }

  @Given("I enter my mobile number and click continue")
  public void enterMobileNumberAndContinue() {
    WhatIsYourMobile page = pages.getWhatIsYourMobile(country);
    page.addTextToMobileNumBox("07700 900345");
    page.clickContinueButton();
  }

  @Given("I am presented with a page to confirm my mobile number on")
  public void pageRequestMobileNumber() {
    IsThisMobileNumCorrect page = pages.getIsThisMobileNumCorrect(country);
    verifyCorrectOnsLogoUsed(page.getOnsLogo(), country);

    assertEquals(
        "Is this mobile number correct - title has incorrect text",
        page.getExpectedText(),
        page.getIsMobileCorrectTitleText());
  }

  @When("I select the Yes option to send the text now")
  public void confirmCorrectMobileNumber() {
    IsThisMobileNumCorrect page = pages.getIsThisMobileNumCorrect();
    page.clickOptionYes();
    page.clickContinueButton();
  }

  @When("I select the No option \\(because the number displayed is not correct)")
  public void rejectMobileNumber() {
    IsThisMobileNumCorrect page = pages.getIsThisMobileNumCorrect();
    page.clickOptionNo();
    page.clickContinueButton();
  }

  @Then("I am presented with a message saying that I have been sent an access code")
  public void verifyMessageThatAccessCodeIsSent() {
    SentAccessCode page = pages.getSentAccessCode(country);

    verifyCorrectOnsLogoUsed(page.getOnsLogo(), country);
    assertEquals(
        "Sent Access Code - title has incorrect text - " + country.name(),
        page.getExpectedText(),
        page.getSentAccessCodeTitleText());
  }

  @Then("I am presented with a button {string}")
  public void surveyButtonPresentedAfterAccessCodeIsSent(String expectedLabel) {
    SentAccessCode page = pages.getSentAccessCode(country);
    String buttonLabelFound = page.getStartSurveyButtonText();
    assertEquals(
        "button label found is incorrect - " + country.name(), expectedLabel, buttonLabelFound);
  }

  @Then("I am also presented with a link to request a new access code")
  public void verifyLinkToRequestNewAccessCode() {
    SentAccessCode page = pages.getSentAccessCode();
    assertEquals(
        "link text found is incorrect",
        page.getExpectedRequestCodeText(),
        page.getRequestNewCodeLinkText());
  }

  @When("I enter an invalid mobile number and click continue")
  public void invalidMobileNumberEntered() {
    WhatIsYourMobile page = pages.getWhatIsYourMobile(country);
    page.addTextToMobileNumBox("1234567");
    page.clickContinueButton();
  }

  @Then("an invalid mobile number error message is displayed")
  public void verifyErrorMessageForInvalidMobileNumber() {
    WhatIsYourMobile page = pages.getWhatIsYourMobile(country);
    assertEquals(
        "invalid mobile error message found is incorrect",
        page.getExpectedErrorText(),
        page.getInvalidMobileErrorText());
  }

  @And("The Token Is Successfully Decrypted")
  public void theEqTokenIsValid() throws Exception {
    EqValidator.verifyTokenSuccessfullyDecrypted(context, driver, keystore);
  }

  @And("the respondent sees the Household Interstitial page and clicks continue")
  public void continueFromHouseholdInterstitial() {
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
  public void enterPostcode(String postcode) {
    PleaseSupplyYourAddress page = pages.getPleaseSupplyYourAddress(country);
    page.addTextToPostcodeTextBox(postcode);
    page.clickContinueButton();
  }

  @And("select \"I can't find my address\", and click \"Continue\"")
  public void cannotFindAddress() {
    SelectYourAddress page = pages.getSelectYourAddress(country);
    page.selectCannotFindAddressBulletPoint();
    page.clickContinueButton();
  }

  @Then(
      "I am presented with a page to call the Customer Contact Centre with the correct telephone number")
  public void verifyPageToCallContactCentre() {
    RegisterYourAddress page = pages.getRegisterYourAddress(country);
    String pageTitle = page.getTitleText();
    String textWithPhoneNumber = page.getTextWithPhoneNumber();

    assertEquals(page.getExpectedTitleText(), pageTitle);
    assertEquals(page.getExpectedTextWithPhoneNumber(), textWithPhoneNumber);
  }

  @Given("I click on \"request a new access code\" in the start page {}")
  public void requestNewAccessCode(Country country) {
    this.country = country;
    StartPage page = pages.getStartPage(country);
    page.clickRequestNewCodeLink();
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
    assertNotNull(context.respondentAuthenticatedPayload.getResponse());
    assertNotNull(context.respondentAuthenticatedPayload.getResponse().getQuestionnaireId());
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

  private void surveyLaunchedPayloadHasResponse() {
    context.surveyLaunchedResponse = context.surveyLaunchedPayload.getResponse();
    assertNotNull(context.surveyLaunchedResponse);
  }

  @Given("the respondent selects continue on the confirm your mobile page {string} {}")
  public void confirmYourMobile(String postcode, Country country) throws Exception {
    emptyEventQueue(EventType.FULFILMENT_REQUESTED);

    confirmAddress(country, postcode);
    continueFromHouseholdInterstitial();
    selectTextMessageOption();
    enterValidMobilePhoneNumber();
    selectContinueWithText();
  }

  @Given("the respondent selects continue on the confirm postal address page {string} {}")
  public void confirmPostalAddress(String postcode, Country country) throws Exception {
    emptyEventQueue(EventType.FULFILMENT_REQUESTED);
    confirmAddress(country, postcode);
    continueFromHouseholdInterstitial();
    selectPostOption();
    enterFullName();
    confirmSendByPost();
    selectContinueToGetHouseholdAccessCode();
  }

  @When("RHSVC publishes a fulfilment request")
  public void verifyFulfilmentRequestSent() throws Exception {
    assertNewFulfilmentEventHasFired();
  }

  @Then("check the fulfilment code is {string}")
  public void verifyFulfilmentCode(String expectedFulfilmentCode) {
    assertEquals(
        "FulfilmentCode is not " + expectedFulfilmentCode,
        expectedFulfilmentCode,
        context.fulfilmentRequestedCode);
  }

  private void confirmYourAddress(Country country, String postCode) throws Exception {
    emptyEventQueue(EventType.NEW_ADDRESS_REPORTED);
    emptyEventQueue(EventType.FULFILMENT_REQUESTED);

    WhatIsYourAddress postcodePage = pages.getWhatIsYourAddress(country);
    postcodePage.addTextToAddressTextBox(postCode);
    postcodePage.clickContinueButton();

    SelectYourAddress selectAddressPage = pages.getSelectYourAddress(country);
    selectAddressPage.selectFirstBulletPoint();
    selectAddressPage.clickContinueButton();

    ConfirmAddress confirmAddressPage = pages.getConfirmAddress(country);
    confirmAddressPage.clickOptionYes();
    confirmAddressPage.clickContinueButton();
  }
}
