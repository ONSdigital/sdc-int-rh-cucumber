package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.SelectDeliveryMethodTextOrPostEng;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.SelectDeliveryMethodTextOrPostWales;

public interface SelectDeliveryMethodTextOrPost {

  static SelectDeliveryMethodTextOrPost getSelectDeliveryMethodTextOrPost(
      final WebDriver webDriver, final Country country) {
    switch (country) {
      case WALES:
        return new SelectDeliveryMethodTextOrPostWales(webDriver);
      case ENG:
        return new SelectDeliveryMethodTextOrPostEng(webDriver);
    }
    return null;
  }

  WebElement getOnsLogo();

  String getSelectDeliveryMethodTextOrPostTitleText();

  void clickContinueButton();

  void clickOptionText();

  void clickOptionPost();

  String getExpectedSelectDeliveryMethodTextOrPostText();
}
