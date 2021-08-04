package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.ConfirmAddressForNewUac;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmAddressForNewUacWales extends PageObjectBase
    implements ConfirmAddressForNewUac {

  private String expectedConfirmText = "Ai dyma'r cyfeiriad cywir?";
  private String expectedAddress = "1 Pentwyn Terrace\nMarshfield\nCardiff\nCF3 2TW";

  public ConfirmAddressForNewUacWales(WebDriver driver) {
    super(driver);
    classPrefix = "ConfirmAddressForNewUacWales:";
    waitForLoading();
    PageFactory.initElements(driver, this);
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

  public String getAddressToConfirmText() {
    waitForElement(addressToConfirm, classPrefix + "addressToConfirm");
    return addressToConfirm.getText();
  }
}
