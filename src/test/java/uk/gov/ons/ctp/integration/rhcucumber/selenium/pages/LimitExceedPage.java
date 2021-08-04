package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import org.openqa.selenium.WebDriver;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.LimitExceedPageEng;

public interface LimitExceedPage {

  static LimitExceedPageEng getLimitExceedPage(final WebDriver webDriver, final String envBaseUrl) {
    return new LimitExceedPageEng(webDriver, envBaseUrl);
  }

  String getMessage();

  String getExpectedMessage();
}
