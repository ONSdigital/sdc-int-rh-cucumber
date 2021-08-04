package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.ConfirmAddressToLinkUacEng;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.ConfirmAddressToLinkUacWales;

public interface ConfirmAddressToLinkUac {

  static ConfirmAddressToLinkUac getConfirmAddressToLinkUac(
      final WebDriver webDriver, final Country country) {
    switch (country) {
      case WALES:
        return new ConfirmAddressToLinkUacWales(webDriver);
      case ENG:
        return new ConfirmAddressToLinkUacEng(webDriver);
    }
    return null;
  }

  String getAddressToConfirmText();

  String getConfirmAddressTitleText();

  void clickContinueButton();

  void clickOptionYes();

  void clickOptionNo();

  WebElement getOnsLogo();

  WebElement getOptionYes();

  WebElement getOptionNo();

  WebElement getContinueButton();

  void setOnsLogo(WebElement onsLogo);

  void setOptionYes(WebElement optionYes);

  void setOptionNo(WebElement optionNo);

  void setContinueButton(WebElement continueButton);

  String getExpectedConfirmText();
}
