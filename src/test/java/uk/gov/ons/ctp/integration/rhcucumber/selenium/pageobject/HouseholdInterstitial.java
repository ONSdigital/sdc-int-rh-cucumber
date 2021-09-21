package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Translations.KEYS;

@Getter
public class HouseholdInterstitial extends PageObjectBase {

  private String expectedText;

  public HouseholdInterstitial(WebDriver driver, Country country) {
    super(PageId.HOUSEHOLD_INTERSTITIAL, driver, country);

    expectedText = translate(KEYS.HOUSEHOLD_INTERSTITIAL_EXPECTED_TEXT);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement householdInterstitialTitle;

  @FindBy(xpath = WebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement continueButton;

  public WebElement getOnsLogo() {
    waitForElement(onsLogo, "onsLogo");
    return onsLogo;
  }

  public void clickContinueButton() {
    waitForElement(continueButton, "continueButton");
    continueButton.click();
  }

  public String getHouseholdInterstitialTitleText() {
    return householdInterstitialTitle.getText();
  }
}
