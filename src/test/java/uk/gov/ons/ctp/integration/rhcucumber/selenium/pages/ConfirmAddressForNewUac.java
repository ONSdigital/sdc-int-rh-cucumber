package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.ConfirmAddressForNewUacEng;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.ConfirmAddressForNewUacWales;

public interface ConfirmAddressForNewUac {

  static ConfirmAddressForNewUac getConfirmAddressForNewUac(
      final WebDriver webDriver, final Country country) {
    switch (country) {
      case WALES:
        return new ConfirmAddressForNewUacWales(webDriver);
      case ENG:
        return new ConfirmAddressForNewUacEng(webDriver);
    }
    return null;
  }

  String getAddressToConfirmText();

  String getConfirmAddressTitleText();

  String getExpectedAddress();

  void clickContinueButton();

  void clickOptionYes();

  void clickOptionNo();

  WebElement getOnsLogo();

  String getExpectedConfirmText();
}
