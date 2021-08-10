package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.WhatIsYourAddress;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhatIsYourAddressWales extends PageObjectBase implements WhatIsYourAddress {

  private String expectedTitleText =
      "What is your address?"; // TODO: Wales translation, when RHUI ready.

  public WhatIsYourAddressWales(WebDriver driver) {
    super(driver);
    classPrefix = "WhatIsYourPostcodeWales:";
    waitForLoading();
    PageFactory.initElements(driver, this);
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
    waitForElement(continueButton, classPrefix + "continueButton");
    continueButton.click();
  }

  public void addTextToAddressTextBox(String txtToAdd) {
    waitForElement(addressTextBox, classPrefix + "addressTextBox");
    addressTextBox.sendKeys(txtToAdd);
  }

  public String getErrorEnterValidPostcodeText() {
    waitForElement(errorEnterValidPostcode, classPrefix + "errorEnterValidPostcode");
    return errorEnterValidPostcode.getText();
  }
}
