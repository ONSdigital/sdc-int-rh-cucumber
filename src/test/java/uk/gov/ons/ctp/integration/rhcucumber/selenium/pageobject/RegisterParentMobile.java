package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Translations.KEYS;

@Getter
@Slf4j
public class RegisterParentMobile extends PageObjectBase {

  private String expectedText;
  private String expectedErrorText;

  public RegisterParentMobile(WebDriver driver, Country country) {
    super(PageId.REGISTER_PARENT_MOBILE, driver, country);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement registerParentMobileTitle;

  @FindBy(xpath = WebPageConstants.XPATH_TEXTBOX_MOBILE_PHONE_NUMBER)
  private WebElement mobileTextBox;

  @FindBy(xpath = WebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement continueButton;

  @FindBy(xpath = WebPageConstants.XPATH_HIGHLIGHTED_ERROR_NO1)
  private WebElement invalidMobileNumError;

  public String getRegisterParentMobileTitle() {
    waitForElement(registerParentMobileTitle, "whatIsYourMobileTitle");
    return registerParentMobileTitle.getText();
  }

  public void clickContinueButton() {
    waitForElement(continueButton, "continueButton");
    continueButton.click();
  }

  public void addTextToMobileNumBox(String txtToAdd) {
    waitForElement(mobileTextBox, "mobileTextBox");
    mobileTextBox.sendKeys(txtToAdd);
  }

  public String getInvalidMobileErrorText() {
    waitForElement(invalidMobileNumError, "invalidMobileNumError");
    return invalidMobileNumError.getText();
  }
}
