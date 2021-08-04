package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.WhatIsYourPostcodeEng;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.WhatIsYourPostcodeWales;

public interface WhatIsYourPostcode {

  static WhatIsYourPostcode getWhatIsYourPostcode(
      final WebDriver webDriver, final Country country) {
    switch (country) {
      case WALES:
        return new WhatIsYourPostcodeWales(webDriver);
      case ENG:
        return new WhatIsYourPostcodeEng(webDriver);
    }
    return null;
  }

  String getwhatIsYourPostcodeTitleText();

  void clickContinueButton();

  void addTextToPostcodeTextBox(String txtToAdd);

  String getErrorEnterValidPostcodeText();

  WebElement getOnsLogo();

  String getExpectedPostcodeText();
}
