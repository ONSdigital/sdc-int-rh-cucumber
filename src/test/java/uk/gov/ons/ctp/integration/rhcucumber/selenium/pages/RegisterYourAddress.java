package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import org.openqa.selenium.WebDriver;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.RegisterYourAddressEng;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.RegisterYourAddressWales;

public interface RegisterYourAddress {
  static RegisterYourAddress getRegisterYourAddress(
      final WebDriver webDriver, final Country country) {
    switch (country) {
      case WALES:
        return new RegisterYourAddressWales(webDriver);
      case ENG:
        return new RegisterYourAddressEng(webDriver);
    }
    return null;
  }

  String getExpectedTitleText();

  String getTitleText();

  String getTextWithPhoneNumber();

  String getExpectedTextWithPhoneNumber();
}
