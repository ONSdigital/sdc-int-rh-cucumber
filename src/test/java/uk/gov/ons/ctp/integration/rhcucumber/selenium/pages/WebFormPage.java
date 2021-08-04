package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import org.openqa.selenium.WebDriver;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.WebFormPageEng;

public interface WebFormPage {

  static WebFormPage getWebFormPage(
      final WebDriver webDriver, final String envBaseUrl, PageType pageType) {
    return new WebFormPageEng(webDriver, envBaseUrl, pageType);
  }

  enum PageType {
    SUCCESS_PAGE,
    START_PAGE;
  }

  void clickSendMessage();

  void addTextToMoreDetailTextBox(String txtToAdd);

  void addName(String name);

  void addEmailAddress(String emailAddress);

  void clickOptionCountry();

  void clickOptionQueryType();

  String getContactUsText();

  String getMessageSentText();

  String getExpectedContactUsText();

  String getExpectedSentText();
}
