package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.SentAccessCodeEng;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.SentAccessCodeWales;

public interface SentAccessCode {

  static SentAccessCode getSentAccessCode(final WebDriver webDriver, final Country country) {
    switch (country) {
      case WALES:
        return new SentAccessCodeWales(webDriver);
      case ENG:
        return new SentAccessCodeEng(webDriver);
    }
    return null;
  }

  String getSentAccessCodeTitleText();

  String getStartSurveyButtonText();

  String getRequestNewCodeLinkText();

  WebElement getOnsLogo();

  String getExpectedText();

  String getExpectedRequestCodeText();
}
