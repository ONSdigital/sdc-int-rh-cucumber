package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Translations;

@Getter
@Slf4j
public class RegisterSIS2StartPage extends PageObjectBase {

  public RegisterSIS2StartPage(final WebDriver driver, final String urlPrefix, Country country) {
    super(PageId.REGISTER_SIS2, driver, country);
    startURL = urlPrefix + translate(Translations.KEYS.REGISTRATION_SIS2_START_PAGE);
    log.info(startURL);
    driver.get(startURL);
    waitForLoading();
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_LINK_SIS2_SURVEY)
  private WebElement loadSIS2Survey;

  public void clickLoadSISLink() {
    waitForElement(loadSIS2Survey, "loadSIS2Survey");
    loadSIS2Survey.click();
  }
}
