package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.PageObjectBase;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.SISWebPageConstants;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;

@Getter
public class RegisterAChildConfirmationPage extends PageObjectBase {

  public RegisterAChildConfirmationPage(final WebDriver driver, Country country) {
    super(PageId.REGISTRATION_OF_CHILD_CONFIRMATION, driver, country);
  }

  @FindBy(xpath = SISWebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = SISWebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement confirmationOfChildRegistration;

  public WebElement getOnsLogo() {
    waitForElement(onsLogo, "onsLogo");
    return onsLogo;
  }

  public String getConfirmationOfChildRegistrationPageTitle() {
    waitForElement(confirmationOfChildRegistration, "confirmationOfChildRegistration");
    return confirmationOfChildRegistration.getText();
  }
}
