package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Translations.KEYS;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhatIsYourMobile extends PageObjectBase {

  private String expectedText;
  private String expectedErrorText;

  public WhatIsYourMobile(WebDriver driver, Country country) {
    super(driver, country);
    classPrefix = "WhatIsYourMobile-" + country.name() + ":";
    waitForLoading();
    PageFactory.initElements(driver, this);
    
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
    waitForElement(whatIsYourMobileTitle, classPrefix + "whatIsYourMobileTitle");
    return whatIsYourMobileTitle.getText();
  }

  public void clickContinueButton() {
    waitForElement(continueButton, classPrefix + "continueButton");
    continueButton.click();
  }

  public void addTextToMobileNumBox(String txtToAdd) {
    waitForElement(mobileTextBox, classPrefix + "mobileTextBox");
    mobileTextBox.sendKeys(txtToAdd);
  }

  public String getInvalidMobileErrorText() {
    waitForElement(invalidMobileNumError, classPrefix + "invalidMobileNumError");
    return invalidMobileNumError.getText();
  }
}
