package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.SelectYourAddressEng;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.SelectYourAddressWales;

public interface SelectYourAddress {

  static SelectYourAddress getSelectYourAddress(final WebDriver webDriver, final Country country) {
    switch (country) {
      case WALES:
        return new SelectYourAddressWales(webDriver);
      case ENG:
        return new SelectYourAddressEng(webDriver);
    }
    return null;
  }

  String getSelectYourAddressTitleText();

  String getExpectedPostcode();

  void clickContinueButton();

  void selectFirstBulletPoint();

  void selectSecondBulletPoint();

  void selectCannotFindAddressBulletPoint();

  WebElement getOnsLogo();

  String getExpectedSelectionText();

  String getExpectedSelectionListText1();

  String getExpectedSelectionListText2();
}
