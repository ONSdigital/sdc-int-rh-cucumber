package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.PageObjectBase;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;

@Getter
public class ConsentToSIS2Survey extends PageObjectBase {

  private String expectedText = "Is this mobile number correct?";

  public ConsentToSIS2Survey(WebDriver driver, Country country) {
    super(PageId.CONFIRM_CONSENT, driver, country);
  }

  @FindBy(xpath = SISWebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = SISWebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement consentToSIS2SurveyTitle;

  @FindBy(xpath = SISWebPageConstants.XPATH_ACCEPT_BUTTON)
  private WebElement acceptButton;

  public String getConsentGivenToSis2SurveyTitleText() {
    waitForElement(consentToSIS2SurveyTitle, classPrefix + "consentToSIS2SurveyTitle");
    return consentToSIS2SurveyTitle.getText();
  }

  public void clickAcceptButton() {
    waitForElement(acceptButton, "acceptButton");
    acceptButton.click();
  }
}
