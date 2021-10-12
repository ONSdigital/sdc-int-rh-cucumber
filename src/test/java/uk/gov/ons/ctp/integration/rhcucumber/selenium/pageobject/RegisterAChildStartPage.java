package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;

@Getter
@Slf4j
public class RegisterAChildStartPage extends PageObjectBase {

  public RegisterAChildStartPage(final WebDriver driver, Country country) {
    super(PageId.REGISTER_A_CHILD, driver, country);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement registerAChildStartPageTitle;

  @FindBy(xpath = WebPageConstants.XPATH_REGISTER_NOW_BUTTON)
  private WebElement registerNow;

  public String getRegisterAChildStartPageTitle() {
    waitForElement(registerAChildStartPageTitle, "registerAChildStartPageTitle");
    return registerAChildStartPageTitle.getText();
  }

  public void clickRegisterNow() {
    waitForElement(registerNow, "registerNow");
    registerNow.click();
  }
}
