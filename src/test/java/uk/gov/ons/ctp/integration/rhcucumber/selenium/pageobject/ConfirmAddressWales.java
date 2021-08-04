package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.ConfirmAddress;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmAddressWales extends PageObjectBase implements ConfirmAddress {

  private String expectedConfirmText = "Ai dyma'r cyfeiriad cywir?";

  public ConfirmAddressWales(WebDriver driver) {
    super(driver);
    classPrefix = "ConfirmAddressWales:";
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

  @FindBy(xpath = WebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement continueButton;

  @FindBy(xpath = WebPageConstants.XPATH_PARAGRAPH_ADDRESS)
  private WebElement wholeAddressParagraph;

  private String firstLineAddress;

  private String secondLineAddress;

  private String thirdLineAddress;

  private String townName;

  private String postcode;

  @FindBy(xpath = WebPageConstants.XPATH_LINK_CHANGE_LANGUAGE)
  private WebElement englishLink;

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

  public void setAddressTextFields() {
    String address = wholeAddressParagraph.getText();
    String[] addressText = address.split("\n");
    firstLineAddress = addressText[0];
    secondLineAddress = addressText[1];
    thirdLineAddress = addressText[2];
    townName = addressText[3];
    postcode = addressText[4];
  }

  public void clickEnglishLink() {
    waitForElement(englishLink, classPrefix + "englishLink");
    englishLink.click();
  }
}
