package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.ADStartPageEng;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.StartPageEng;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.StartPageWales;

public interface StartPage {

  static StartPage getStartPage(
      final WebDriver webDriver,
      final Country country,
      final String envBaseUrl,
      String locationId) {
    switch (country) {
      case WALES:
        return new StartPageWales(webDriver, envBaseUrl);
      case ENG:
        return locationId == null
            ? new StartPageEng(webDriver, envBaseUrl)
            : new ADStartPageEng(webDriver, envBaseUrl, locationId);
    }
    return null;
  }

  String getErrorEnterAccessCodeText();

  String getErrorEnterValidCodeText();

  String getTestUPRN();

  void clickAccessSurveyButton();

  void enterUac(String uac);

  void clickRequestNewCodeLink();

  void clickWebChatLink();

  void clickAlternativeLanguageLink();

  void clickUacBox();

  WebElement getOnsLogo();
}
