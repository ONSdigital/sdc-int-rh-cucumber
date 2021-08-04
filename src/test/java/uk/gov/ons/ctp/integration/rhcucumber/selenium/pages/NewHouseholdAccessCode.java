package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import org.openqa.selenium.WebDriver;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.NewHouseholdAccessCodeEng;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.NewHouseholdAccessCodeWales;

public interface NewHouseholdAccessCode {

  static NewHouseholdAccessCode getNewHouseholdAccessCode(
      final WebDriver webDriver, final Country country) {
    switch (country) {
      case ENG:
        return new NewHouseholdAccessCodeEng(webDriver);
      case WALES:
        return new NewHouseholdAccessCodeWales(webDriver);
    }
    return null;
  }

  void clickContinueButton();

  void selectYesSendByPost();

  void selectNoSendAnotherWay();
}
