package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.WhatIsYourMobileEng;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.WhatIsYourMobileWales;

public interface WhatIsYourMobile {

  static WhatIsYourMobile getWhatIsYourMobile(final WebDriver webDriver, final Country country) {
    switch (country) {
      case WALES:
        return new WhatIsYourMobileWales(webDriver);
      case ENG:
        return new WhatIsYourMobileEng(webDriver);
    }
    return null;
  }

  String getWhatIsYourMobileTitleText();

  void clickContinueButton();

  void addTextToMobileNumBox(String txtToAdd);

  String getInvalidMobileErrorText();

  WebElement getOnsLogo();

  String getExpectedText();

  String getExpectedErrorText();
}
