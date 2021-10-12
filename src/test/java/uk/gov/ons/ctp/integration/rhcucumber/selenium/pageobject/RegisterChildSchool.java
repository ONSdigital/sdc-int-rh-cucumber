package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Translations.KEYS;

@Getter
public class RegisterChildSchool extends PageObjectBase {

  public RegisterChildSchool(WebDriver driver, Country country) {
    super(PageId.REGISTER_CHILD_SCHOOL, driver, country);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement registerSchoolNameTitle;

  @FindBy(xpath = WebPageConstants.XPATH_CHILD_SCHOOL_NAME)
  private WebElement firstNameTextBox;

  @FindBy(xpath = WebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement continueButton;

  public WebElement getOnsLogo() {
    waitForElement(onsLogo, "onsLogo");
    return onsLogo;
  }

  public String getRegisterSchoolNameTitle() {
    waitForElement(registerSchoolNameTitle, "registerSchoolNameTitle");
    return registerSchoolNameTitle.getText();
  }

  public String getExpectedConfirmText() {
    return translate(KEYS.CONFIRM_ADDRESS_CONFIRMATION_TEXT);
  }

  public void clickSchoolName() {
    waitForElement(firstNameTextBox, "firstNameTextBox");
    firstNameTextBox.click();
  }

  private void addTextToSchoolName(String txtToAdd) {
    waitForElement(firstNameTextBox, "firstNameTextBox");
    firstNameTextBox.sendKeys(txtToAdd);
  }

  public void enterSchoolName(String firstName) {
    clickSchoolName();
    addTextToSchoolName(firstName);
  }

  public void clickContinueButton() {
    waitForElement(continueButton, "continueButton");
    continueButton.click();
  }
}
