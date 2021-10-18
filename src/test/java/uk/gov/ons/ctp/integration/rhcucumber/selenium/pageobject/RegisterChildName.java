package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;

@Getter
public class RegisterChildName extends PageObjectBase {

  public RegisterChildName(WebDriver driver, Country country) {
    super(PageId.REGISTER_PARENT_NAME, driver, country);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement registerChildNameTitle;

  @FindBy(xpath = WebPageConstants.XPATH_TEXTBOX_FIRSTNAME)
  private WebElement firstNameTextBox;

  @FindBy(xpath = WebPageConstants.XPATH_TEXTBOX_MIDDLENAME)
  private WebElement middleNameTextBox;

  @FindBy(xpath = WebPageConstants.XPATH_TEXTBOX_LASTNAME)
  private WebElement lastNameTextBox;

  @FindBy(xpath = WebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement saveAndContinueButton;

  public WebElement getOnsLogo() {
    waitForElement(onsLogo, "onsLogo");
    return onsLogo;
  }

  public String getRegisterChildNameTitleText() {
    waitForElement(registerChildNameTitle, "registerChildNameTitle");
    return registerChildNameTitle.getText();
  }

  public void clickFirstNameBox() {
    waitForElement(firstNameTextBox, "firstNameTextBox");
    firstNameTextBox.click();
  }

  private void addTextToFirstName(String txtToAdd) {
    waitForElement(firstNameTextBox, "firstNameTextBox");
    firstNameTextBox.sendKeys(txtToAdd);
  }

  public void enterFirstName(String firstName) {
    clickFirstNameBox();
    addTextToFirstName(firstName);
  }

  public void clickMiddleNameBox() {
    waitForElement(middleNameTextBox, "middleNameTextBox");
    middleNameTextBox.click();
  }

  private void addTextToMiddleName(String txtToAdd) {
    waitForElement(middleNameTextBox, "middleNameTextBox");
    middleNameTextBox.sendKeys(txtToAdd);
  }

  public void enterMiddleName(String middleName) {
    clickMiddleNameBox();
    addTextToMiddleName(middleName);
  }

  public void clickLastNameBox() {
    waitForElement(lastNameTextBox, "lastNameTextBox");
    lastNameTextBox.click();
  }

  private void addTextToLastName(String txtToAdd) {
    waitForElement(lastNameTextBox, "lastNameTextBox");
    lastNameTextBox.sendKeys(txtToAdd);
  }

  public void enterLastName(String lastName) {
    clickLastNameBox();
    addTextToLastName(lastName);
  }

  public void clickSaveAndContinueButton() {
    waitForElement(saveAndContinueButton, "saveAndContinueButton");
    saveAndContinueButton.click();
  }
}
