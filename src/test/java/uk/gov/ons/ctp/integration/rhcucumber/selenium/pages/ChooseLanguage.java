package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import org.openqa.selenium.WebElement;

public interface ChooseLanguage {

  String getChooseLanguageTitleText();

  void clickContinueButton();

  void clickOptionYes();

  WebElement getOnsLogo();
}
