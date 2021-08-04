package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.WhatIsYourPostcode;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhatIsYourPostcodeWales extends PageObjectBase implements WhatIsYourPostcode {

  private String expectedPostcodeText = "Beth yw eich cod post?";

  public WhatIsYourPostcodeWales(WebDriver driver) {
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

  @FindBy(xpath = WebPageConstants.XPATH_TEXTBOX_POSTCODE)
  private WebElement postcodeTextBox;

  @FindBy(xpath = WebPageConstants.XPATH_HIGHLIGHTED_ERROR_NO1)
  private WebElement errorEnterValidPostcode;

  public String getwhatIsYourPostcodeTitleText() {
    return whatIsYourPostcodeTitle.getText();
  }

  public void clickContinueButton() {
    waitForElement(continueButton, classPrefix + "continueButton");
    continueButton.click();
  }

  public void addTextToPostcodeTextBox(String txtToAdd) {
    waitForElement(postcodeTextBox, classPrefix + "postcodeTextBox");
    postcodeTextBox.sendKeys(txtToAdd);
  }

  public String getErrorEnterValidPostcodeText() {
    waitForElement(errorEnterValidPostcode, classPrefix + "errorEnterValidPostcode");
    return errorEnterValidPostcode.getText();
  }

  public void clickErrorEnterValidPostcode() {
    waitForElement(errorEnterValidPostcode, classPrefix + "errorEnterValidPostcode");
    errorEnterValidPostcode.click();
  }
}
