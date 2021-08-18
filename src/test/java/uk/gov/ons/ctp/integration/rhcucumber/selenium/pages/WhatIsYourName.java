package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import org.openqa.selenium.WebDriver;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.WhatIsYourNameEng;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.WhatIsYourNameWales;

public interface WhatIsYourName {

  static WhatIsYourName getWhatIsYourName(final WebDriver webDriver, final Country country) {
    switch (country) {
      case ENG:
        return new WhatIsYourNameEng(webDriver);
      case WALES:
        return new WhatIsYourNameWales(webDriver);
    }
    return null;
  }

  void clickContinueButton();

  void addTextToFirstNameTextBox(String txtToAdd);

  void addTextToLastNameTextBox(String txtToAdd);
}
