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
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.HouseholdInterstitial;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.IsThisMobileNumCorrect;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.PleaseSupplyYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.RegisterYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.SelectDeliveryMethodTextOrPost;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.SelectYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.SentAccessCode;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.StartPage;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.WhatIsYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.WhatIsYourMobile;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.WhatIsYourName;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.ConsentToSIS2Survey;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.RegisterAChildConfirmationPage;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.RegisterAChildStartPage;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.RegisterChildDOB;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.RegisterChildName;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.RegisterChildSchool;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.RegisterParentMobile;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.RegisterParentName;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.ReviewChildDetail;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.SIS2HowToTakePart;
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

  @Then("I am directed to the Questionnaire")
  public void clickForQuestionnaire() {
    EqValidator.clickThoughToEq(driver, context);
  }

  @Given("an empty queue exists for sending Respondent Authentication events")
  public void emptyEventQueueForRespondentAuthentication() throws Exception {
    emptyEventQueue(TopicType.UAC_AUTHENTICATION);
  }

  @Given("an empty queue exists for sending EQ Launched events")
  public void emptyEventQueuForEqLaunched() throws Exception {
    emptyEventQueue(TopicType.EQ_LAUNCH);
  }

  @Then("a Respondent Authentication event is sent to RM")
  public void verifyRespondentAuthenticationEventSent() throws Exception {
    assertNewRespondantAuthenticationEventHasFired();
  }

  @Then("an EQ Launched event is sent to RM")
  public void verifyEqLaunchedEventSent() throws Exception {
    assertNewEqLaunchedEventHasFired();
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

  @And("The Token Is Successfully Decrypted")
  public void theEqTokenIsValid() throws Exception {
    EqValidator.verifyTokenSuccessfullyDecrypted(context, driver, keystore);
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

    // Hack in some buffer time for the survey to be created
    // TODO there must be a better way
    Thread.sleep(1000);

    pubSub.sendEvent(
        TopicType.COLLECTION_EXERCISE_UPDATE,
        Source.SAMPLE_LOADER,
        Channel.RM,
        context.collectionExercise);

    // Hack in some buffer time for the collex to be created
    // TODO there must be a better way
    Thread.sleep(1000);

    pubSub.sendEvent(
        TopicType.CASE_UPDATE, Source.CASE_SERVICE, Channel.RM, context.caseCreatedPayload);
    pubSub.sendEvent(eventType, Source.SAMPLE_LOADER, Channel.RM, context.uacPayload);
  }

  private void verifyUacProcessed() throws Exception {
    assertTrue(dataRepo.waitForObject(context.caseCollection, context.caseKey, WAIT_TIMEOUT));
    assertTrue(dataRepo.waitForObject(context.uacCollection, context.uacKey, WAIT_TIMEOUT));
   }

  @And("the respondentAuthenticationHeader contains the correct values")
  public void theRespondentAuthenticationHeaderContainsTheCorrectValues() {
    assertEquals(EventTopic.UAC_AUTHENTICATION, context.respondentAuthenticationHeader.getTopic());
    assertEquals(Source.RESPONDENT_HOME.name(), context.respondentAuthenticationHeader.getSource());
    assertEquals(Channel.RH, context.respondentAuthenticationHeader.getChannel());
    assertNotNull(context.respondentAuthenticationHeader.getDateTime());
    assertNotNull(context.respondentAuthenticationHeader.getMessageId());
    assertNotNull(context.uacAuthenticationPayload.getUacAuthentication());
    assertNotNull(context.uacAuthenticationPayload.getUacAuthentication().getQid());
  }

  @And("the eqLaunchedHeader contains the correct values")
  public void theEqLaunchedHeaderContainsTheCorrectValues() {
    assertEquals(EventTopic.EQ_LAUNCH, context.eqLaunchedHeader.getTopic());
    assertEquals(Source.RESPONDENT_HOME.name(), context.eqLaunchedHeader.getSource());
    assertEquals(Channel.RH, context.eqLaunchedHeader.getChannel());
    assertNotNull(context.eqLaunchedHeader.getDateTime());
    assertNotNull(context.eqLaunchedHeader.getMessageId());
    eqLaunchedPayloadHasResponse();
    assertNotNull(context.eqLaunched.getQid());
  }

  private void eqLaunchedPayloadHasResponse() {
    context.eqLaunched = context.eqLaunchedPayload.getEqLaunch();
    assertNotNull(context.eqLaunched);
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
        .addTextToMobileNumBox(context.familyInformation.getMobileNumber());
    pages.getRegisterParentMobile().clickContinueButton();
  }

  @Given("The number is correct and I select the “Yes, my mobile number is correct“ button")
  public void theNumberIsCorrectISelectTheButton() {
    String expectedMobileNumber = pages.getIsThisMobileNumCorrect().displayedMobileNumber();
    assertEquals(context.familyInformation.getMobileNumber(), expectedMobileNumber);
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
