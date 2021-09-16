package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import lombok.Getter;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Translations.KEYS;

@Getter
public class IsThisMobileNumCorrect extends PageObjectBase {

  private String expectedText = "Is this mobile number correct?";

  public IsThisMobileNumCorrect(WebDriver driver, Country country) {
    super(PageId.IS_THIS_MOBILE_NUM_CORRECT, driver, country);
    
    expectedText = translate(KEYS.IS_THIS_MOBILE_NUM_CORRECT_EXPECTED_TEXT);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement isMobileCorrectTitle;

  @FindBy(xpath = WebPageConstants.XPATH_RADIO_MOBILE_YES)
  private WebElement optionYes;

  @FindBy(xpath = WebPageConstants.XPATH_RADIO_MOBILE_NO)
  private WebElement optionNo;

  @FindBy(xpath = WebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement continueButton;

  public String getIsMobileCorrectTitleText() {
    waitForElement(isMobileCorrectTitle, classPrefix + "isMobileCorrectTitle");
    return isMobileCorrectTitle.getText();
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
