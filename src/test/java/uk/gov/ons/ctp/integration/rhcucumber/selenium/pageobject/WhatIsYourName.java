package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhatIsYourName extends PageObjectBase {
  private String expectedText = "What is your name?";

  public WhatIsYourName(WebDriver driver, Country country) {
    super(driver, country);
    classPrefix = "WhatIsYourName-" + country.name() + ":";
    waitForLoading();
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath = WebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement continueButton;

  @FindBy(xpath = WebPageConstants.XPATH_TEXTBOX_FIRSTNAME)
  private WebElement firstNameTextBox;

  @FindBy(xpath = WebPageConstants.XPATH_TEXTBOX_LASTNAME)
  private WebElement lastNameTextBox;

  public void clickContinueButton() {
    waitForElement(continueButton, classPrefix + "continueButton");
    continueButton.click();
  }

  public void addTextToFirstNameTextBox(String txtToAdd) {
    waitForElement(firstNameTextBox, classPrefix + "firstNameTextBox  ");
    firstNameTextBox.sendKeys(txtToAdd);
  }

  public void addTextToLastNameTextBox(String txtToAdd) {
    waitForElement(lastNameTextBox, classPrefix + "lastNameTextBox");
    lastNameTextBox.sendKeys(txtToAdd);
  }
}
