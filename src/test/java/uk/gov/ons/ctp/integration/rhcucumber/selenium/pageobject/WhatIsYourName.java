package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;

@Getter
public class WhatIsYourName extends PageObjectBase {
  private String expectedText = "What is your name?";

  public WhatIsYourName(WebDriver driver, Country country) {
    super(PageId.WHAT_IS_YOUR_NAME, driver, country);
  }

  @FindBy(xpath = WebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement continueButton;

  @FindBy(xpath = WebPageConstants.XPATH_TEXTBOX_FIRSTNAME)
  private WebElement firstNameTextBox;

  @FindBy(xpath = WebPageConstants.XPATH_TEXTBOX_LASTNAME)
  private WebElement lastNameTextBox;

  public void clickContinueButton() {
    waitForElement(continueButton, "continueButton");
    continueButton.click();
  }

  public void addTextToFirstNameTextBox(String txtToAdd) {
    waitForElement(firstNameTextBox, "firstNameTextBox");
    firstNameTextBox.sendKeys(txtToAdd);
  }

  public void addTextToLastNameTextBox(String txtToAdd) {
    waitForElement(lastNameTextBox, "lastNameTextBox");
    lastNameTextBox.sendKeys(txtToAdd);
  }
}
