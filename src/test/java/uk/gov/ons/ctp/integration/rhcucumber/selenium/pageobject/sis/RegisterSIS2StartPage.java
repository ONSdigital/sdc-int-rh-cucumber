package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.PageObjectBase;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.SISWebPageConstants;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Translations;

@Getter
public class RegisterSIS2StartPage extends PageObjectBase {

  public RegisterSIS2StartPage(final WebDriver driver, final String urlPrefix, Country country) {
    super(PageId.REGISTER_SIS2, driver, country);
    startURL = urlPrefix + translate(Translations.KEYS.REGISTRATION_SIS2_START_PAGE);
    driver.get(startURL);
    waitForLoading();
  }

  @FindBy(xpath = SISWebPageConstants.XPATH_LINK_SIS2_SURVEY)
  private WebElement loadSIS2Survey;

  public void clickLoadSISLink() {
    waitForElement(loadSIS2Survey, "loadSIS2Survey");
    loadSIS2Survey.click();
  }
}
