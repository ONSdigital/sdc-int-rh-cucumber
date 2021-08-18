package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.PleaseSupplyYourAddressEng;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.PleaseSupplyYourAddressWales;

public interface PleaseSupplyYourAddress {

  static PleaseSupplyYourAddress getPleaseSupplyYourAddress(
      final WebDriver webDriver, final Country country) {
    switch (country) {
      case WALES:
        return new PleaseSupplyYourAddressWales(webDriver);
      case ENG:
        return new PleaseSupplyYourAddressEng(webDriver);
    }
    return null;
  }

  String getPleaseSupplyYourAddressTitleText();

  void clickContinueButton();

  void addTextToPostcodeTextBox(String txtToAdd);

  WebElement getOnsLogo();

  WebElement getContinueButton();

  String getExpectedText();
}
