package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;

@Getter
public class WhatIsYourAddress extends PageObjectBase {

  private String expectedTitleText =
      "What is your address?"; // TODO: Wales translation, when RHUI ready.

  public WhatIsYourAddress(WebDriver driver, Country country) {
    super(PageId.WHAT_IS_YOUR_ADDRESS, driver, country);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement whatIsYourPostcodeTitle;

  @FindBy(xpath = WebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement continueButton;

  @FindBy(xpath = WebPageConstants.XPATH_TEXTBOX_ADDRESS)
  private WebElement addressTextBox;

  @FindBy(xpath = WebPageConstants.XPATH_HIGHLIGHTED_ERROR_NO1)
  private WebElement errorEnterValidPostcode;

  public String getwhatIsYourAddressTitleText() {
    return whatIsYourPostcodeTitle.getText();
  }

  public void clickContinueButton() {
    waitForElement(continueButton, "continueButton");
    continueButton.click();
  }

  public void addTextToAddressTextBox(String txtToAdd) {
    waitForElement(addressTextBox, "addressTextBox");
    addressTextBox.sendKeys(txtToAdd);
  }

  public String getErrorEnterValidPostcodeText() {
    waitForElement(errorEnterValidPostcode, "errorEnterValidPostcode");
    return errorEnterValidPostcode.getText();
  }
}
