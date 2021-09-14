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
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Translations;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartPage extends PageObjectBase {

  private String testUPRN = "10023122451";

  static public StartPage getStartPage(
      final WebDriver webDriver, final Country country, final String envBaseUrl) {
      return new StartPage(webDriver, envBaseUrl, country);
  }
  
  public StartPage(final WebDriver driver, final String urlPrefix, Country country) {
    super(PageId.START_PAGE, driver, country);
    startURL = urlPrefix + translate(Translations.KEYS.START_PAGE_URL_SUFFIX);
    driver.get(startURL);
    waitForLoading();
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_HIGHLIGHTED_ERROR_NO1)
  private WebElement errorEnterAccessCode;

  @FindBy(xpath = WebPageConstants.XPATH_HIGHLIGHTED_ERROR_NO1)
  private WebElement errorEnterValidCode;

  @FindBy(xpath = WebPageConstants.XPATH_BUTTON_ACCESS_SURVEY)
  private WebElement accessSurveyButton;

  @FindBy(xpath = WebPageConstants.XPATH_TEXTBOX_ENTER_UAC)
  private WebElement uacTextBox;

  @FindBy(xpath = WebPageConstants.XPATH_LINK_REQUEST_A_NEW_CODE)
  private WebElement requestNewCodeLink;

  @FindBy(xpath = WebPageConstants.XPATH_LINK_CHANGE_LANGUAGE)
  private WebElement changeLanguageLink;

  public String getErrorEnterAccessCodeText() {
    waitForElement(errorEnterAccessCode, "errorEnterAccessCode");
    return errorEnterAccessCode.getText();
  }

  public String getErrorEnterValidCodeText() {
    waitForElement(errorEnterValidCode, "errorEnterValidCode");
    return errorEnterValidCode.getText();
  }

  public void clickAccessSurveyButton() {
    waitForElement(accessSurveyButton, "accessSurveyButton");
    accessSurveyButton.click();
  }

  public void clickUacBox() {
    waitForElement(uacTextBox, "uacTextBox");
    uacTextBox.click();
  }

  private void addTextToUac(String txtToAdd) {
    waitForElement(uacTextBox, "uacTextBox");
    uacTextBox.sendKeys(txtToAdd);
  }

  public void enterUac(String uac) {
    clickUacBox();
    addTextToUac(uac);
  }

  public void clickRequestNewCodeLink() {
    waitForElement(requestNewCodeLink, "requestNewCodeLink");
    requestNewCodeLink.click();
  }

  public void clickAlternativeLanguageLink() {
    waitForElement(changeLanguageLink, "changeLanguageLink");
    changeLanguageLink.click();
  }
}
