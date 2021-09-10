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
public class IsThisMobileNumCorrect extends PageObjectBase {

  private String expectedText = "Is this mobile number correct?";

  public IsThisMobileNumCorrect(WebDriver driver, Country country) {
    super(driver, country);
    classPrefix = "IsThisMobileNumCorrect-" + country.name() + ":";
    waitForLoading();
    PageFactory.initElements(driver, this);
    
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
