package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.WhatIsYourMobile;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhatIsYourMobileWales extends PageObjectBase implements WhatIsYourMobile {

  private String expectedText = "Beth yw eich rhif ffôn symudol?";
  private String expectedErrorText =
      "Rhowch rif ffôn symudol yn y Deyrnas Unedig mewn fformat dilys, er enghraifft, 07700 900345 neu +44 7700 900345";

  public WhatIsYourMobileWales(WebDriver driver) {
    super(driver);
    classPrefix = "WhatIsYourMobileWales:";
    waitForLoading();
    PageFactory.initElements(driver, this);
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
