package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.HouseholdInterstitialEng;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.HouseholdInterstitialWales;

public interface HouseholdInterstitial {

  static HouseholdInterstitial getHouseholdInterstitial(
      final WebDriver webDriver, final Country country) {
    switch (country) {
      case WALES:
        return new HouseholdInterstitialWales(webDriver);
      case ENG:
        return new HouseholdInterstitialEng(webDriver);
    }
    return null;
  }

  WebElement getOnsLogo();

  void clickContinueButton();

  String getHouseholdInterstitialTitleText();

  String getExpectedText();
}
