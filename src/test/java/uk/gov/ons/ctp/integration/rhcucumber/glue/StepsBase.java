package uk.gov.ons.ctp.integration.rhcucumber.glue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    String expectedLogoText;
    switch (country) {
      case WALES:
        expectedLogoText = "ons-logo-pos-cy.svg";
        break;
      case ENG:
        expectedLogoText = "ons-logo-pos-en.svg";
        break;
      default:
        expectedLogoText = "";
    }
    assertNotNull(logo);
    String fullLogoName = logo.getAttribute("src");
    String actualLogoName = extractLogoName(fullLogoName);
    assertEquals(
        "name found for logo is incorrect - " + country.name(), expectedLogoText, actualLogoName);
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
}
