package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PleaseSupplyYourAddress;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PleaseSupplyYourAddressEng extends PageObjectBase implements PleaseSupplyYourAddress {

  private String expectedText = "What is your postcode?";
  private String expectedSelectionText = "Select your address";

  public PleaseSupplyYourAddressEng(WebDriver driver) {
    super(driver);
    classPrefix = "PleaseSupplyYourAddressEng:";
    waitForLoading();
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement pleaseSupplyYourAddressTitle;

  @FindBy(xpath = WebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement continueButton;

  @FindBy(xpath = WebPageConstants.XPATH_TEXTBOX_POSTCODE)
  private WebElement postcodeTextBox;

  @FindBy(xpath = WebPageConstants.XPATH_HIGHLIGHTED_ERROR_NO1)
  private WebElement errorEnterValidPostcode;

  @Override
  public String getPleaseSupplyYourAddressTitleText() {
    return pleaseSupplyYourAddressTitle.getText();
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
}
