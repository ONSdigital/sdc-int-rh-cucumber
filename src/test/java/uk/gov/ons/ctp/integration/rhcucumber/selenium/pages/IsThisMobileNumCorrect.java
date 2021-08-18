package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.IsThisMobileNumCorrectEng;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.IsThisMobileNumCorrectWales;

public interface IsThisMobileNumCorrect {

  static IsThisMobileNumCorrect getIsThisMobileNumCorrect(
      final WebDriver webDriver, final Country country) {
    switch (country) {
      case WALES:
        return new IsThisMobileNumCorrectWales(webDriver);
      case ENG:
        return new IsThisMobileNumCorrectEng(webDriver);
    }
    return null;
  }

  String getIsMobileCorrectTitleText();

  void clickContinueButton();

  void clickOptionYes();

  void clickOptionNo();

  WebElement getOnsLogo();

  String getExpectedText();
}
