package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.ConfirmAddressToLinkUac;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmAddressToLinkUacEng extends PageObjectBase implements ConfirmAddressToLinkUac {

  private String expectedConfirmText = "Is this the correct address?";

  public ConfirmAddressToLinkUacEng(WebDriver driver) {
    super(driver);
    classPrefix = "ConfirmAddressToLinkUac:";
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

  @Override
  public String getAddressToConfirmText() {
    waitForElement(addressToConfirm, classPrefix + "addressToConfirm");
    return addressToConfirm.getText();
  }

  @Override
  public String getConfirmAddressTitleText() {
    waitForElement(confirmAddressTitle, classPrefix + "confirmAddressTitle");
    return confirmAddressTitle.getText();
  }

  @Override
  public void clickContinueButton() {
    waitForElement(continueButton, classPrefix + "continueButton");
    continueButton.click();
  }

  @Override
  public void clickOptionYes() {
    waitForElement(optionYes, classPrefix + "optionYes");
    optionYes.click();
  }

  @Override
  public void clickOptionNo() {
    waitForElement(optionNo, classPrefix + "optionNo");
    optionNo.click();
  }
}
