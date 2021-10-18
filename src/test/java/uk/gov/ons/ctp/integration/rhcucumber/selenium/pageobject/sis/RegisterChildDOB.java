package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.PageObjectBase;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.SISWebPageConstants;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;

@Getter
public class RegisterChildDOB extends PageObjectBase {

  public RegisterChildDOB(WebDriver driver, Country country) {
    super(PageId.REGISTER_CHILD_DOB, driver, country);
  }

  @FindBy(xpath = SISWebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = SISWebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement registerChildDOBTitle;

  @FindBy(xpath = SISWebPageConstants.XPATH_CHILD_DOB_DAY)
  private WebElement dayDateBox;

  @FindBy(xpath = SISWebPageConstants.XPATH_CHILD_DOB_MONTH)
  private WebElement monthDateBox;

  @FindBy(xpath = SISWebPageConstants.XPATH_CHILD_DOB_YEAR)
  private WebElement yearDateBox;

  @FindBy(xpath = SISWebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement continueButton;

  public WebElement getOnsLogo() {
    waitForElement(onsLogo, "onsLogo");
    return onsLogo;
  }

  public String getRegisterChildDOBTitle() {
    waitForElement(registerChildDOBTitle, "registerChildDOBTitle");
    return registerChildDOBTitle.getText();
  }

  public void clickDayBox() {
    waitForElement(dayDateBox, "dayDateBox");
    dayDateBox.click();
  }

  private void addTextToDayBox(String txtToAdd) {
    waitForElement(dayDateBox, "dayDateBox");
    dayDateBox.sendKeys(txtToAdd);
  }

  public void enterDOBDay(String dobDay) {
    clickDayBox();
    addTextToDayBox(dobDay);
  }

  public void clickMonthBox() {
    waitForElement(monthDateBox, "monthDateBox");
    monthDateBox.click();
  }

  private void addTextToMonthBox(String txtToAdd) {
    waitForElement(monthDateBox, "monthDateBox");
    monthDateBox.sendKeys(txtToAdd);
  }

  public void enterDOBMonth(String dobMonth) {
    clickMonthBox();
    addTextToMonthBox(dobMonth);
  }

  public void clickYearBox() {
    waitForElement(yearDateBox, "yearDateBox");
    yearDateBox.click();
  }

  private void addTextToYearBox(String txtToAdd) {
    waitForElement(yearDateBox, "yearDateBox");
    yearDateBox.sendKeys(txtToAdd);
  }

  public void enterDOBYear(String dobYear) {
    clickYearBox();
    addTextToYearBox(dobYear);
  }

  public void clickContinueButton() {
    waitForElement(continueButton, "continueButton");
    continueButton.click();
  }
}
