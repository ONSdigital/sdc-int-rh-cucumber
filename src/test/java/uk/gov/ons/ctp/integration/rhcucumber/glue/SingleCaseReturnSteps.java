package uk.gov.ons.ctp.integration.rhcucumber.glue;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import uk.gov.ons.ctp.common.FixtureHelper;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.common.event.EventPublisher.Channel;
import uk.gov.ons.ctp.common.event.EventPublisher.EventType;
import uk.gov.ons.ctp.common.event.EventPublisher.Source;
import uk.gov.ons.ctp.common.event.model.Address;
import uk.gov.ons.ctp.common.event.model.CollectionCase;
import uk.gov.ons.ctp.integration.eqlaunch.crypto.JweDecryptor;
import uk.gov.ons.ctp.integration.eqlaunch.crypto.KeyStore;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.ConfirmAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.SelectYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.StartPage;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.WhatIsYourPostcode;

public class SingleCaseReturnSteps extends StepsBase {
  private final EventType caseEventType = EventType.CASE_CREATED;
  private final EventType uacEventType = EventType.UAC_CREATED;
  private String eventQueueName;
  private String postcode;
  private String uprnValue;
  private String currentURL;
  private List<CollectionCase> collectionCases;

  @Before("@SingleCaseSetup")
  public void initialDataSetup() throws Exception {
    super.setupForAll();

    eventQueueName = rabbit.createQueue(caseEventType);
    rabbit.flushQueue(eventQueueName);

    String uacQueueName = rabbit.createQueue(uacEventType);
    rabbit.flushQueue(uacQueueName);

    // Create Collection cases list from Fixtures
    collectionCases = FixtureHelper.loadPackageFixtures(CollectionCase[].class);

    Address commonAddress = collectionCases.get(0).getAddress();
    postcode = commonAddress.getPostcode().trim();
    uprnValue = commonAddress.getUprn();
  }

  @After("@SingleCaseTeardown")
  public void teardown() {
    super.closeDriver();
  }

  // TESTS //
  @Given("Test cases and a UAC are created {string} {string} {string} {string} {string} {string}")
  public void testCasesAndAUACAreCreated(
      String caseType0,
      String caseType1,
      String caseType2,
      String addressInvalid0,
      String addressInvalid1,
      String addressInvalid2)
      throws Exception {

    rabbit.flushQueue(eventQueueName);

    // create a single UAC
    context.caseKey = null;
    constructUacUpdatedEvent();
    context.uacPayload.setFormType("H");
    rabbit.sendEvent(uacEventType, Source.SAMPLE_LOADER, Channel.RM, context.uacPayload);

    // Set up values from feature table
    List<Boolean> addressInvalidBooleanList =
        Arrays.asList(
            Boolean.parseBoolean(addressInvalid0),
            Boolean.parseBoolean(addressInvalid1),
            Boolean.parseBoolean(addressInvalid2));
    List<String> caseTypeList = Arrays.asList(caseType0, caseType1, caseType2);

    // Create the collection case values and send to Firestore
    for (CollectionCase collectionCase : collectionCases) {
      final int index = collectionCases.indexOf(collectionCase);

      collectionCase.setAddressInvalid(addressInvalidBooleanList.get(index));
      collectionCase.setCaseType(caseTypeList.get(index));

      rabbit.sendEvent(caseEventType, Source.SAMPLE_LOADER, Channel.RM, collectionCase);
    }
  }

  @When("A valid UAC is entered")
  public void aValidUACIsEntered() {
    final StartPage startPage = pages.getStartPage(Country.ENG);
    verifyCorrectOnsLogoUsed(startPage.getOnsLogo(), Country.ENG);

    // enter UAC
    startPage.clickUacBox();
    startPage.enterUac(context.uac);
    startPage.clickAccessSurveyButton();
  }

  @And("A valid postcode is entered")
  public void aValidPostcodeIsEntered() {

    final WhatIsYourPostcode postcodePage = pages.getWhatIsYourPostcode(Country.ENG);

    // enter postcode
    postcodePage.addTextToPostcodeTextBox(postcode);
    postcodePage.clickContinueButton();
  }

  @And("The correct address is selected")
  public void theCorrectAddressIsSelected() {

    final SelectYourAddress selectAddress = pages.getSelectYourAddress(Country.ENG);

    // select valid address
    final WebElement selectedAddressElement = driver.findElement(By.id(uprnValue));
    selectedAddressElement.click();
    selectAddress.clickContinueButton();
  }

  @And("The selected address is confirmed")
  public void theSelectedAddressIsConfirmed() {
    final ConfirmAddress confirmAddress = pages.getConfirmAddress(Country.ENG);

    // select YES option
    confirmAddress.clickOptionYes();
    // click continue
    try {
      confirmAddress.clickContinueButton();
    } catch (WebDriverException e) {
      System.out.println("EQ Not available");
    }
    currentURL = driver.getCurrentUrl();
  }

  @And("The resulting link token contains the correct case ID {string}")
  public void theResultingLinkTokenContainsTheCorrectCaseID(String expectedCaseIndex)
      throws CTPException, JsonProcessingException {

    String splitter = "token=";
    String token = currentURL.split(splitter)[1];
    final JweDecryptor decryptor = new JweDecryptor(new KeyStore(keystore));

    String decryptedEqToken = decryptor.decrypt(token);

    @SuppressWarnings("unchecked")
    HashMap<String, String> decryptedKeyValueMap =
        new ObjectMapper().readValue(decryptedEqToken, HashMap.class);
    String resultingCaseId = decryptedKeyValueMap.get("case_id");

    int index = Integer.parseInt(expectedCaseIndex);
    assertEquals(
        "The correct Case ID should be returned",
        collectionCases.get(index).getId(),
        resultingCaseId);
  }
}
