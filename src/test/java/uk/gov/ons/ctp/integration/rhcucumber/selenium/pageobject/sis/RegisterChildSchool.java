package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.PageObjectBase;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;

@Getter
public class RegisterChildSchool extends PageObjectBase {

  public RegisterChildSchool(WebDriver driver, Country country) {
    super(PageId.REGISTER_CHILD_SCHOOL, driver, country);
  }

  @FindBy(xpath = SISWebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = SISWebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement registerSchoolNameTitle;

  @FindBy(xpath = SISWebPageConstants.XPATH_CHILD_SCHOOL_NAME)
  private WebElement schoolNameTextBox;

  @FindBy(xpath = SISWebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement continueButton;

  public WebElement getOnsLogo() {
    waitForElement(onsLogo, "onsLogo");
    return onsLogo;
  }

  public String getRegisterSchoolNameTitle() {
    waitForElement(registerSchoolNameTitle, "registerSchoolNameTitle");
    return registerSchoolNameTitle.getText();
  }

  public void clickSchoolName() {
    waitForElement(schoolNameTextBox, "schoolNameTextBox");
    schoolNameTextBox.click();
  }

  private void addTextToSchoolName(String txtToAdd) {
    waitForElement(schoolNameTextBox, "schoolNameTextBox");
    schoolNameTextBox.sendKeys(txtToAdd);
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
