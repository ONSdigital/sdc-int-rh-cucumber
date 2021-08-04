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
public class WhatIsYourPostcodeEng extends PageObjectBase implements WhatIsYourPostcode {

  private String expectedPostcodeText = "What is your postcode?";

  public WhatIsYourPostcodeEng(WebDriver driver) {
    super(driver);
    classPrefix = "WhatIsYourPostcode:";
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

  @Override
  public String getwhatIsYourPostcodeTitleText() {
    return whatIsYourPostcodeTitle.getText();
  }

  @Override
  public void clickContinueButton() {
    waitForElement(continueButton, classPrefix + "continueButton");
    continueButton.click();
  }

  @Override
  public void addTextToPostcodeTextBox(String txtToAdd) {
    waitForElement(postcodeTextBox, classPrefix + "postcodeTextBox");
    postcodeTextBox.sendKeys(txtToAdd);
  }

  @Override
  public String getErrorEnterValidPostcodeText() {
    waitForElement(errorEnterValidPostcode, classPrefix + "errorEnterValidPostcode");
    return errorEnterValidPostcode.getText();
  }
}
