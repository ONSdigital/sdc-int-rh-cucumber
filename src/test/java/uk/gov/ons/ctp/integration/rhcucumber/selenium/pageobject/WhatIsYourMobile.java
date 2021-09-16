package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import lombok.Getter;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Translations.KEYS;

@Getter
public class WhatIsYourMobile extends PageObjectBase {

  private String expectedText;
  private String expectedErrorText;

  public WhatIsYourMobile(WebDriver driver, Country country) {
    super(PageId.WHAT_IS_YOUR_MOBILE, driver, country);
    
    expectedText = translate(KEYS.WHAT_IS_YOUR_MOBILE_EXPECTED_TEXT);
    expectedText = translate(KEYS.WHAT_IS_YOUR_MOBILE_EXPECTED_ERROR_TEXT);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement whatIsYourMobileTitle;

  @FindBy(xpath = WebPageConstants.XPATH_TEXTBOX_MOBILE_PHONE_NUMBER)
  private WebElement mobileTextBox;

  @FindBy(xpath = WebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement continueButton;

  @FindBy(xpath = WebPageConstants.XPATH_HIGHLIGHTED_ERROR_NO1)
  private WebElement invalidMobileNumError;

  public String getWhatIsYourMobileTitleText() {
    waitForElement(whatIsYourMobileTitle, "whatIsYourMobileTitle");
    return whatIsYourMobileTitle.getText();
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
