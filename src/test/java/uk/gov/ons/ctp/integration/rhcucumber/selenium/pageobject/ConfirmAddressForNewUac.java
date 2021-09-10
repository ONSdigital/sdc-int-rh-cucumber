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
public class ConfirmAddressForNewUac extends PageObjectBase {

  private String expectedConfirmText;
  private String expectedAddress;

  public ConfirmAddressForNewUac(WebDriver driver, Country country) {
    super(driver, country);
    waitForLoading();
    PageFactory.initElements(driver, this);
    
    classPrefix = "ConfirmAddressForNewUac-" + country.name() + ":";
    expectedConfirmText = translate(KEYS.CONFIRM_ADDRESS_FOR_NEW_UAC_EXPECTED_CONFIRM_TEXT);
    expectedAddress = translate(KEYS.CONFIRM_ADDRESS_FOR_NEW_UAC_EXPECTED_ADDRESS);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement confirmAddressTitle;

  @FindBy(xpath = WebPageConstants.XPATH_RADIO_ADDRESS_YES)
  private WebElement optionYes;

  @FindBy(xpath = WebPageConstants.XPATH_RADIO_ADDRESS_NO)
  private WebElement optionNo;

  @FindBy(xpath = WebPageConstants.XPATH_EM_ADDRESS)
  private WebElement addressToConfirm;

  @FindBy(xpath = WebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement continueButton;

  public String getAddressToConfirmText() {
    waitForElement(addressToConfirm, classPrefix + "addressToConfirm");
    return addressToConfirm.getText();
  }

  public String getConfirmAddressTitleText() {
    waitForElement(confirmAddressTitle, classPrefix + "confirmAddressTitle");
    return confirmAddressTitle.getText();
  }

  public void clickContinueButton() {
    waitForElement(continueButton, classPrefix + "continueButton");
    continueButton.click();
  }

  public void clickOptionYes() {
    waitForElement(optionYes, classPrefix + "optionYes");
    optionYes.click();
  }

  public void clickOptionNo() {
    waitForElement(optionNo, classPrefix + "optionNo");
    optionNo.click();
  }
}
