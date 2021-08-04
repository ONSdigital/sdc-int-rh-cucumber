package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.ChangeYourAddressEng;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.ChangeYourAddressWales;

public interface ChangeYourAddress {

  static ChangeYourAddress getChangeYourAddress(final WebDriver webDriver, final Country country) {
    switch (country) {
      case WALES:
        return new ChangeYourAddressWales(webDriver);
      case ENG:
        return new ChangeYourAddressEng(webDriver);
    }
    return null;
  }

  String getChangeYourAddressTitleText();

  void clickSaveAndContinueButton();

  void addTextToBuildingAndStreetTextBox(String txtToAdd);

  WebElement getOnsLogo();
}
