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
public class PleaseSupplyYourAddressWales extends PageObjectBase
    implements PleaseSupplyYourAddress {

  private String expectedText = "Beth yw eich cod post?";
  private String expectedSelectionText = "Dewiswch eich cyfeiriad";

  public PleaseSupplyYourAddressWales(WebDriver driver) {
    super(driver);
    classPrefix = "PleaseSupplyYourAddressWales:";
    waitForLoading();
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement pleaseSupplyYourAddressTitle;

  @FindBy(xpath = WebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement continueButton;

  @FindBy(xpath = WebPageConstants.XPATH_TEXTBOX_ADDRESS)
  private WebElement postcodeTextBox;

  @FindBy(xpath = WebPageConstants.XPATH_HIGHLIGHTED_ERROR_NO1)
  private WebElement errorEnterValidPostcode;

  public String getPleaseSupplyYourAddressTitleText() {
    return pleaseSupplyYourAddressTitle.getText();
  }

  public void clickContinueButton() {
    waitForElement(continueButton, classPrefix + "continueButton");
    continueButton.click();
  }

  public void addTextToPostcodeTextBox(String txtToAdd) {
    waitForElement(postcodeTextBox, classPrefix + "postcodeTextBox");
    postcodeTextBox.sendKeys(txtToAdd);
  }
}
