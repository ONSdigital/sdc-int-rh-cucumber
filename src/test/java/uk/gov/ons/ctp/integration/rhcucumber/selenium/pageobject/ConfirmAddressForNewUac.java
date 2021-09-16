package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Translations.KEYS;

@Getter
public class ConfirmAddressForNewUac extends PageObjectBase {

  private String expectedConfirmText;
  private String expectedAddress;

  public ConfirmAddressForNewUac(WebDriver driver, Country country) {
    super(PageId.CONFIRM_ADDRESS_FOR_NEW_UAC, driver, country);

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
    waitForElement(addressToConfirm, "addressToConfirm");
    return addressToConfirm.getText();
  }

  public String getConfirmAddressTitleText() {
    waitForElement(confirmAddressTitle, "confirmAddressTitle");
    return confirmAddressTitle.getText();
  }

  public void clickContinueButton() {
    waitForElement(continueButton, "continueButton");
    continueButton.click();
  }

  public void clickOptionYes() {
    waitForElement(optionYes, "optionYes");
    optionYes.click();
  }

  public void clickOptionNo() {
    waitForElement(optionNo, "optionNo");
    optionNo.click();
  }
}
