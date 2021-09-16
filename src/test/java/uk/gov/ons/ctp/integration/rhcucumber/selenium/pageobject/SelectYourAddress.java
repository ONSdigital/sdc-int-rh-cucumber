package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import lombok.Getter;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Translations.KEYS;

@Getter
public class SelectYourAddress extends PageObjectBase {

  private String expectedSelectionText;
  private String expectedSelectionListText1;
  private String expectedSelectionListText2;
  private String expectedPostcode;

  public SelectYourAddress(WebDriver driver, Country country) {
    super(PageId.SELECT_YOUR_ADDRESS, driver, country);
    
    expectedSelectionText = translate(KEYS.SELECT_YOUR_ADDRESS_EXPECTED_SELECTION_TEXT);
    expectedSelectionListText1 = translate(KEYS.SELECT_YOUR_ADDRESS_EXPECTED_SELECTION_LIST_TEXT1);
    expectedSelectionListText2 = translate(KEYS.SELECT_YOUR_ADDRESS_EXPECTED_SELECTION_LIST_TEXT2);
    expectedPostcode = translate(KEYS.SELECT_YOUR_ADDRESS_EXPECTED_POSTCODE);
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
    waitForElement(selectYourAddressTitle, "selectYourAddressTitle");
    return selectYourAddressTitle.getText();
  }

  public void clickContinueButton() {
    waitForElement(continueButton, "continueButton");
    continueButton.click();
  }

  public void selectFirstBulletPoint() {
    waitForElement(firstBulletPoint, "firstBulletPoint");
    firstBulletPoint.click();
  }

  public void selectSecondBulletPoint() {
    waitForElement(secondBulletPoint, "secondBulletPoint");
    secondBulletPoint.click();
  }

  public void selectCannotFindAddressBulletPoint() {
    waitForElement(cannotFindAddressBulletPoint, "cannotFindAddressBulletPoint");
    cannotFindAddressBulletPoint.click();
  }
}
