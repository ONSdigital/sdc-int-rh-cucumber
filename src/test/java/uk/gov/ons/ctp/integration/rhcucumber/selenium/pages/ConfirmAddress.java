package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.ConfirmAddressEng;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.ConfirmAddressWales;

public interface ConfirmAddress {

  static ConfirmAddress getConfirmAddress(final WebDriver webDriver, final Country country) {
    switch (country) {
      case WALES:
        return new ConfirmAddressWales(webDriver);
      case ENG:
        return new ConfirmAddressEng(webDriver);
    }
    return null;
  }

  WebElement getOnsLogo();

  String getConfirmAddressTitleText();

  void clickContinueButton();

  void clickOptionYes();

  void clickOptionNo();

  void setAddressTextFields();

  String getFirstLineAddress();

  String getSecondLineAddress();

  String getThirdLineAddress();

  String getTownName();

  String getPostcode();

  String getExpectedConfirmText();
}
