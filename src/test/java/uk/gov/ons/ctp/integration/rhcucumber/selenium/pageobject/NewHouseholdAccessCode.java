package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import lombok.Getter;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;

@Getter
public class NewHouseholdAccessCode extends PageObjectBase {
  public NewHouseholdAccessCode(WebDriver webDriver, Country country) {
    super(PageId.NEW_HOUSEHOLD_ACCESS_CODE, webDriver, country);
  }

  @FindBy(xpath = WebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement continueButton;

  @FindBy(xpath = WebPageConstants.XPATH_RADIO_POST_ACCESS_CODE_YES)
  private WebElement optionYes;

  @FindBy(xpath = WebPageConstants.XPATH_RADIO_POST_ACCESS_CODE_NO)
  private WebElement optionNo;

  public void clickContinueButton() {
    waitForElement(continueButton, "continueButton");
    continueButton.click();
  }

  public void selectYesSendByPost() {
    waitForElement(optionYes, "optionYes");
    optionYes.click();
  }

  public void selectNoSendAnotherWay() {
    waitForElement(optionNo, "optionNo");
    optionNo.click();
  }
}
