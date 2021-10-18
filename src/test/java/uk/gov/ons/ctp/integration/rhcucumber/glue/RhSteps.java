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
import uk.gov.ons.ctp.common.domain.Channel;
import uk.gov.ons.ctp.common.domain.Source;
import uk.gov.ons.ctp.common.event.EventTopic;
import uk.gov.ons.ctp.common.event.TopicType;
import uk.gov.ons.ctp.common.util.Wait;
import uk.gov.ons.ctp.integration.rhcucumber.data.ExampleData;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.ConfirmAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.ConfirmAddressForNewUac;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.ConsentToSIS2Survey;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.HouseholdInterstitial;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.IsThisMobileNumCorrect;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.PleaseSupplyYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.RegisterAChildConfirmationPage;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.RegisterAChildStartPage;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.RegisterChildDOB;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.RegisterChildName;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.RegisterChildSchool;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.RegisterParentMobile;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.RegisterParentName;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.RegisterYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.ReviewChildDetail;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.SIS2HowToTakePart;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.SelectDeliveryMethodTextOrPost;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.SelectYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.SentAccessCode;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.StartPage;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.WhatIsYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.WhatIsYourMobile;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.WhatIsYourName;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;

public class RhSteps extends StepsBase {
  private Wait wait;
  private Country country;
  private String currentPage;

  @Before("@Setup")
  public void setupNoCountry() throws Exception {
    super.setupForAll();
    wait = new Wait(driver);
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

  public void setUpFamilyInformation() {
    context.familyInformation = ExampleData.constructFamilyInformation();
  }

  private String childsFullName() {
     return String.format(
        "%1$s %2$s %3$s",
        context.familyInformation.getChildFirstName(),
        context.familyInformation.getChildMiddleNames(),
        context.familyInformation.getChildLastName());
  }

  @After("@TearDown")
  public void deleteDriver() {
    super.closeDriver();
    super.destroyPubSub();
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
    // TODO we will revisit these when the features are made to work, when Address typeahead is
    // working in cucumber tests
    // assertNewEventHasFired(EventType.NEW_ADDRESS_REPORTED);
  }

  @And("RHSVC publishes a UAC fulfilment request")
  public void verifyUacFulfimentRequestPublished() throws Exception {
    assertNewEventHasFired(TopicType.FULFILMENT);
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
    currentPage = "ConfirmSocialSurveyAddress";
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
    //  Social Survey
    if (currentPage.equals("ConfirmSocialSurveyAddress")) {
      wait.forLoading(1);
      try {
        pages.getConfirmAddress(country).clickContinueButton();
      } catch (WebDriverException e) {
        // tolerate no EQ deployment for testing
        context.errorMessageContainingCallToEQ = e.getMessage();
      }
    //  SIS2
    } else {
      switch (currentPage) {
        case "RegisterParentName":
          pages.getRegisterParentName().clickContinueButton();
          break;
        case "RegisterParentMobile":
          pages.getIsThisMobileNumCorrect().clickContinueButton();
          break;
        case "RegisterChildSchool":
          pages.getRegisterChildSchool().clickContinueButton();
          break;
        case "RegisterChildDOB":
          pages.getRegisterChildDOB().clickContinueButton();
          break;
        default:
          throw new IllegalStateException(
              String.format("Failed to find the “Conitnue“ button for page %s", currentPage));
      }
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
    EqValidator.clickThoughToEq(driver, context);
  }

  @Given("an empty queue exists for sending Respondent Authenticated events")
  public void emptyEventQueueForRespondentAuthenticated() throws Exception {
    emptyEventQueue(TopicType.UAC_AUTHENTICATE);
  }

  @Given("an empty queue exists for sending Survey Launched events")
  public void emptyEventQueuForSurveyLaunched() throws Exception {
    emptyEventQueue(TopicType.SURVEY_LAUNCH);
  }

  @Given("an empty queue exists for sending Fulfilment Requested events")
  public void emptyEventQueueForFulfilmentRequested() throws Exception {
    emptyEventQueue(TopicType.FULFILMENT);
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
    assertNewEventHasFired(TopicType.FULFILMENT);
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

  @Given("I select the first address and click continue")
  public void selectAddressAndContinue() {
    SelectYourAddress page = pages.getSelectYourAddress(country);
    page.selectFirstBulletPoint();
    page.clickContinueButton();
  }

  @Then("I am presented with a page displaying the expected address")
  public void addressIsDisplayed() {
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
    page.addTextToMobileNumBox(ExampleData.VALID_MOBILE_NO);
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
    page.addTextToMobileNumBox(ExampleData.INVALID_MOBILE_NO);
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
        country, TopicType.UAC_UPDATE.name());
  }

  @Given(
      "SETUP-3 - a valid uac exists in Firestore and there is an associated case in Firestore {} eventType {string}")
  public void setupAValidUacExistsInFirestoreAndThereIsAnAssociatedCaseInFirestore(
      Country country, String uacEventType) throws Exception {
    setupTest(country);
    prepareInitialRequiredEvents();
    sendRequiredInboundEvents(TopicType.valueOf(uacEventType));
    verifyUacProcessed();
  }

  @Given("SETUP-4 - a valid uac and associated case exist in Firestore with region {}")
  public void setupAValidUacExistsInFirestoreAndThereIsAnAssociatedCaseInFirestoreWithRegion(
      Country country) throws Exception {
    setupTest(country);
    if (country == Country.WALES) {
      prepareWelshInitialRequiredEvents();
    } else {
      prepareInitialRequiredEvents();
    }
    sendRequiredInboundEvents(TopicType.UAC_UPDATE);
    verifyUacProcessed();
  }

  private void prepareInitialRequiredEvents() {
    context.collectionExercise = ExampleData.createCollectionExercise();
    context.surveyUpdatePayload = ExampleData.createSurveyUpdate();
    context.caseCreatedPayload = ExampleData.createCaseUpdate(context.caseKey);
    constructUacUpdatedEvent();
  }

  private void prepareWelshInitialRequiredEvents() {
    context.collectionExercise = ExampleData.createCollectionExercise();
    context.surveyUpdatePayload = ExampleData.createSurveyUpdate();
    context.caseCreatedPayload = ExampleData.createWelshCaseUpdate(context.caseKey);
    constructUacUpdatedEvent();
  }

  private void sendRequiredInboundEvents(TopicType eventType) throws Exception {
    pubSub.sendEvent(
        TopicType.SURVEY_UPDATE, Source.SAMPLE_LOADER, Channel.RM, context.surveyUpdatePayload);
    pubSub.sendEvent(
        TopicType.COLLECTION_EXERCISE_UPDATE, Source.SAMPLE_LOADER, Channel.RM, context.collectionExercise);
    pubSub.sendEvent(
        TopicType.CASE_UPDATE, Source.CASE_SERVICE, Channel.RM, context.caseCreatedPayload);
    pubSub.sendEvent(eventType, Source.SAMPLE_LOADER, Channel.RM, context.uacPayload);
  }

  private void verifyUacProcessed() throws Exception {
    assertTrue(dataRepo.waitForObject(context.caseCollection, context.caseKey, WAIT_TIMEOUT));
    assertTrue(dataRepo.waitForObject(context.uacCollection, context.uacKey, WAIT_TIMEOUT));
  }

  @And("the respondentAuthenticatedHeader contains the correct values")
  public void theRespondentAuthenticatedHeaderContainsTheCorrectValues() {
    assertEquals(EventTopic.UAC_AUTHENTICATE, context.respondentAuthenticatedHeader.getTopic());
    assertEquals(Source.RESPONDENT_HOME.name(), context.respondentAuthenticatedHeader.getSource());
    assertEquals(Channel.RH, context.respondentAuthenticatedHeader.getChannel());
    assertNotNull(context.respondentAuthenticatedHeader.getDateTime());
    assertNotNull(context.respondentAuthenticatedHeader.getMessageId());
    assertNotNull(context.respondentAuthenticatedPayload.getResponse());
    assertNotNull(context.respondentAuthenticatedPayload.getResponse().getQuestionnaireId());
  }

  @And("the surveyLaunchedHeader contains the correct values")
  public void theSurveyLaunchedHeaderContainsTheCorrectValues() {
    assertEquals(EventTopic.SURVEY_LAUNCH, context.surveyLaunchedHeader.getTopic());
    assertEquals(Source.RESPONDENT_HOME.name(), context.surveyLaunchedHeader.getSource());
    assertEquals(Channel.RH, context.surveyLaunchedHeader.getChannel());
    assertNotNull(context.surveyLaunchedHeader.getDateTime());
    assertNotNull(context.surveyLaunchedHeader.getMessageId());
    surveyLaunchedPayloadHasResponse();
    assertNotNull(context.surveyLaunchedResponse.getQuestionnaireId());
  }

  private void surveyLaunchedPayloadHasResponse() {
    context.surveyLaunchedResponse = context.surveyLaunchedPayload.getResponse();
    assertNotNull(context.surveyLaunchedResponse);
  }

  @Given("the respondent selects continue on the confirm your mobile page {string} {}")
  public void confirmYourMobile(String postcode, Country country) throws Exception {
    emptyEventQueue(TopicType.FULFILMENT);

    confirmAddress(country, postcode);
    continueFromHouseholdInterstitial();
    selectTextMessageOption();
    enterValidMobilePhoneNumber();
    selectContinueWithText();
  }

  @Given("the respondent selects continue on the confirm postal address page {string} {}")
  public void confirmPostalAddress(String postcode, Country country) throws Exception {
    emptyEventQueue(TopicType.FULFILMENT);
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
    // TODO we will revisit these when the features are made to work, when Address typeahead is
    // working in cucumber tests
    //    emptyEventQueue(EventType.NEW_ADDRESS_REPORTED);
    emptyEventQueue(TopicType.FULFILMENT);

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

  @Given("I am on the take part in a survey page")
  public void iAmOnTheTakePartInASurveyPage() {
    setUpFamilyInformation();
    this.country = Country.ENG;
    pages.getRegisterSis2StartPage(country);
  }

  @And("I click on “COVID-19 Schools Infection Survey“ link")
  public void clickCovid19SchoolsInfectSurveyLink() {
    pages.getRegisterSis2StartPage().clickLoadSISLink();
  }

  @Then("I am presented with a page on how to take part")
  public void verifyHowToTakePartPage() {
    SIS2HowToTakePart sis2HowToTakePart = pages.getSisPage(country);
    verifyCorrectOnsLogoUsed(sis2HowToTakePart.getOnsLogo(), country);
    assertEquals("COVID-19 Schools Infection Survey (SIS)", pages.getSisPage().getSis2TitleText());
  }

  @And("I click on “Register for the survey“ button")
  public void clickRegisterForTheSurveyButton() {
    pages.getSisPage().clickRegisterForSurveyButton();
  }

  @Then("I am presented with a page to register the child")
  public void iAmPresentedWithAPageToRegisterTheChild() {
    RegisterAChildStartPage registerAChildStartPage = pages.getRegisterAChildStartPage(country);
    verifyCorrectOnsLogoUsed(registerAChildStartPage.getOnsLogo(), country);
    assertEquals(
        "Register a child", pages.getRegisterAChildStartPage().getRegisterAChildStartPageTitle());
  }

  @And("I click on “Register now“ button")
  public void iClickOnRegisterNowButton() {
    pages.getRegisterAChildStartPage().clickRegisterNow();
  }

  @Then("I am presented with a page to add my name")
  public void iAmPresentedWithAPageToAddMyName() {
    currentPage = "RegisterParentName";
    RegisterParentName registerParentName = pages.getRegisterParentName(country);
    verifyCorrectOnsLogoUsed(registerParentName.getOnsLogo(), country);
    assertEquals(
        "What is the parent/guardian's name?",
        pages.getRegisterParentName().getRegisterParentNameTitleText());
  }

  @And("I enter my first, middle and last name")
  public void iEnterMyFirstMiddleAndLastName() {
    pages.getRegisterParentName().enterFirstName(context.familyInformation.getFirstName());
    pages.getRegisterParentName().enterMiddleName("Joe");
    pages.getRegisterParentName().enterLastName(context.familyInformation.getLastName());
  }

  @Then("I am presented with a page to enter my mobile number")
  public void iAmPresentedWithAPageToEnterMyMobileNumber() {
    currentPage = "RegisterParentMobile";
    RegisterParentMobile registerParentMobile = pages.getRegisterParentMobile(country);
    verifyCorrectOnsLogoUsed(registerParentMobile.getOnsLogo(), country);
    assertEquals(
        "What is your mobile number?",
        pages.getRegisterParentMobile().getRegisterParentMobileTitle());
  }

  @And("I enter a valid mobile number and click “Continue”")
  public void iEnterAValidMobileNumber() {
    pages
        .getRegisterParentMobile()
        .addTextToMobileNumBox(context.familyInformation.getParentMobileNumber());
    pages.getRegisterParentMobile().clickContinueButton();
  }

  @Given("The number is correct and I select the “Yes, my mobile number is correct“ button")
  public void theNumberIsCorrectISelectTheButton() {
    String expectedMobileNumber = pages.getIsThisMobileNumCorrect().displayedMobileNumber();
    assertEquals(context.familyInformation.getParentMobileNumber(), expectedMobileNumber);
    pages.getIsThisMobileNumCorrect().clickOptionYes();
  }

  @Then("I am presented with a page to confirm my consent")
  public void iAmPresentedWithAPageToConfirmMyConsent() {
    ConsentToSIS2Survey consentToSIS2Survey = pages.getConsentToSIS2Survey(country);
    verifyCorrectOnsLogoUsed(consentToSIS2Survey.getOnsLogo(), country);
    assertEquals("Confirm consent", consentToSIS2Survey.getConsentGivenToSis2SurveyTitleText());
  }

  @And("I click on the “I accept“ option")
  public void iClickOnTheAcceptOptionToGiveConsent() {
    pages.getConsentToSIS2Survey().clickAcceptButton();
  }

  @Then("I am presented with a page to add my childs name")
  public void iAmPresentedWithAPageToAddMyChildsFirstMiddleAndLastName() {
    currentPage = "RegisterChildName";
    RegisterChildName registerChildName = pages.getRegisterChildName(country);
    verifyCorrectOnsLogoUsed(registerChildName.getOnsLogo(), country);
    assertEquals(
        "Who would you like to register?", registerChildName.getRegisterChildNameTitleText());
  }

  @And("I enter my childs first, middle and last name")
  public void iEnterMyChildsFirstMiddleAndLastName() {
    pages.getRegisterChildName().enterFirstName(context.familyInformation.getChildFirstName());
    pages.getRegisterChildName().enterMiddleName(context.familyInformation.getChildMiddleNames());
    pages.getRegisterChildName().enterLastName(context.familyInformation.getLastName());
  }

  @And("I then click the “Save and continue“ button")
  public void iClickTheSaveAndContinueButton() {
    if (currentPage.equals("RegisterChildName")) {
      pages.getRegisterChildName().clickSaveAndContinueButton();
    } else {
      pages.getReviewChildDetail().clickSaveAndContinueButton();
    }
  }

  @Then("I am presented with a page to add my childs school")
  public void iAmPresentedWithAPageToAddMyChildsSchool() {
    currentPage = "RegisterChildSchool";
    RegisterChildSchool registerChildSchool = pages.getRegisterChildSchool(country);
    verifyCorrectOnsLogoUsed(registerChildSchool.getOnsLogo(), country);
    String schoolsTitle = String.format("What school does %s attend?", childsFullName());
    assertEquals(schoolsTitle, registerChildSchool.getRegisterSchoolNameTitle());
  }

  @And("I enter my childs school name {string}")
  public void iEnterMyChildsSchoolName(String schoolName) {
    pages.getRegisterChildSchool().enterSchoolName(schoolName);
  }

  @Then("I am presented with a page to add my childs date of birth")
  public void iAmPresentedWithAPageToAddMyChildsDateOfBirth() {
    currentPage = "RegisterChildDOB";
    RegisterChildDOB registerChildDOB = pages.getRegisterChildDOB(country);
    verifyCorrectOnsLogoUsed(registerChildDOB.getOnsLogo(), country);
    String childsDOBTitle = String.format("What is the date of birth of %s?", childsFullName());
    assertEquals(childsDOBTitle, registerChildDOB.getRegisterChildDOBTitle());
  }

  @And("I enter my childs date of birth")
  public void iEnterMyChildsDateOfBirth() {
    pages
        .getRegisterChildDOB()
        .enterDOBDay(String.valueOf(context.familyInformation.getChildDob().getDayOfMonth()));
    pages
        .getRegisterChildDOB()
        .enterDOBMonth(String.valueOf(context.familyInformation.getChildDob().getMonthValue()));
    pages
        .getRegisterChildDOB()
        .enterDOBYear(String.valueOf(context.familyInformation.getChildDob().getYear()));
  }

  @Then("I am presented with a page to review my childs details")
  public void iAmPresentedWithAPageToReviewMyChildsDetails() {
    currentPage = "ReviewChildDetail";
    ReviewChildDetail reviewChildDetail = pages.getReviewChildDetail(country);
    verifyCorrectOnsLogoUsed(reviewChildDetail.getOnsLogo(), country);
    assertEquals("Check answers", reviewChildDetail.getReviewChildDetailTitleText());
  }

  @Then("I am presented with a page to confirm child registration")
  public void iAmPresentedWithAPageToConfirmChildRegistration() {
    RegisterAChildConfirmationPage registerAChildConfirmationPage =
        pages.getRegisterAChildConfirmationPage(country);
    verifyCorrectOnsLogoUsed(registerAChildConfirmationPage.getOnsLogo(), country);
    assertEquals(
        "Your child has been registered for the survey",
        registerAChildConfirmationPage.getConfirmationOfChildRegistrationPageTitle());
  }
}
