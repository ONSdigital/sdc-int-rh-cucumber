package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.WhatIsYourAddressEng;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.WhatIsYourAddressWales;

public interface WhatIsYourAddress {

  static WhatIsYourAddress getWhatIsYourAddress(final WebDriver webDriver, final Country country) {
    switch (country) {
      case WALES:
        return new WhatIsYourAddressWales(webDriver);
      case ENG:
        return new WhatIsYourAddressEng(webDriver);
    }
    return null;
  }

  String getwhatIsYourAddressTitleText();

  void clickContinueButton();

  void addTextToAddressTextBox(String txtToAdd);

  String getErrorEnterValidPostcodeText();

  WebElement getOnsLogo();

  String getExpectedTitleText();
}
