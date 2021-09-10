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
public class HouseholdInterstitial extends PageObjectBase {

  private String expectedText;

  public HouseholdInterstitial(WebDriver driver, Country country) {
    super(driver, country);
    classPrefix = "HouseholdInterstitial-" + country.name() + ":";
    waitForLoading();
    PageFactory.initElements(driver, this);

    expectedText = translate(KEYS.HOUSEHOLD_INTERSTITIAL_EXPECTED_TEXT);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement householdInterstitialTitle;

  @FindBy(xpath = WebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement continueButton;

  public WebElement getOnsLogo() {
    waitForElement(onsLogo, classPrefix + "onsLogo");
    return onsLogo;
  }

  public void clickContinueButton() {
    waitForElement(continueButton, classPrefix + "continueButton");
    continueButton.click();
  }

  public String getHouseholdInterstitialTitleText() {
    return householdInterstitialTitle.getText();
  }
}
