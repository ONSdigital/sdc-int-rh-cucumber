package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectYourAddress extends PageObjectBase {

  private String expectedSelectionText = "Select your address";
  private String expectedSelectionListText1 = "";
  private String expectedSelectionListText2 = " addresses found for postcode";
  private String expectedPostcode = "EX2 6GA";

  public SelectYourAddress(WebDriver driver) {
    super(driver);
    classPrefix = "SelectYourAddressEng:";
    waitForLoading();
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement selectYourAddressTitle;

  @FindBy(xpath = WebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement continueButton;

  @FindBy(xpath = WebPageConstants.XPATH_PARAGRAPH_ADDRESS_COUNT)
  private WebElement numAddressesMessage;

  @FindBy(xpath = WebPageConstants.XPATH_RADIO_FIRST_ADDRESS)
  private WebElement firstBulletPoint;

  @FindBy(xpath = WebPageConstants.XPATH_RADIO_SECOND_ADDRESS)
  private WebElement secondBulletPoint;

  @FindBy(xpath = WebPageConstants.XPATH_RADIO_CANNOT_FIND_ADDRESS)
  private WebElement cannotFindAddressBulletPoint;

  public String getSelectYourAddressTitleText() {
    waitForElement(selectYourAddressTitle, classPrefix + "selectYourAddressTitle");
    return selectYourAddressTitle.getText();
  }

  public void clickContinueButton() {
    waitForElement(continueButton, classPrefix + "continueButton");
    continueButton.click();
  }

  public void selectFirstBulletPoint() {
    waitForElement(firstBulletPoint, classPrefix + "firstBulletPoint");
    firstBulletPoint.click();
  }

  public void selectSecondBulletPoint() {
    waitForElement(secondBulletPoint, classPrefix + "secondBulletPoint");
    secondBulletPoint.click();
  }

  public void selectCannotFindAddressBulletPoint() {
    waitForElement(cannotFindAddressBulletPoint, classPrefix + "cannotFindAddressBulletPoint");
    cannotFindAddressBulletPoint.click();
  }
}
