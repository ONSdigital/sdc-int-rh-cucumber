package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.ChangeYourAddress;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeYourAddressEng extends PageObjectBase implements ChangeYourAddress {

  public ChangeYourAddressEng(WebDriver driver) {
    super(driver);
    classPrefix = "ChangeYourAddress:";
    waitForLoading();
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement changeYourAddressTitle;

  @FindBy(xpath = WebPageConstants.XPATH_SAVE_AND_CONTINUE_BUTTON)
  private WebElement saveAndContinueButton;

  @FindBy(xpath = WebPageConstants.XPATH_TEXTBOX_POSTCODE)
  private WebElement postcodeTextBox;

  @FindBy(xpath = WebPageConstants.XPATH_HIGHLIGHTED_ERROR_NO1)
  private WebElement errorEnterValidPostcode;

  @FindBy(xpath = WebPageConstants.XPATH_ADDRESS_LINE1)
  private WebElement buildingAndStreetTextBox;

  @Override
  public String getChangeYourAddressTitleText() {
    waitForElement(changeYourAddressTitle, classPrefix + "changeYourAddressTitle");
    return changeYourAddressTitle.getText();
  }

  @Override
  public void clickSaveAndContinueButton() {
    waitForElement(saveAndContinueButton, classPrefix + "saveAndContinueButton");
    saveAndContinueButton.click();
  }

  @Override
  public void addTextToBuildingAndStreetTextBox(String txtToAdd) {
    waitForElement(buildingAndStreetTextBox, classPrefix + "buildingAndStreetTextBox");
    buildingAndStreetTextBox.sendKeys(txtToAdd);
  }
}
