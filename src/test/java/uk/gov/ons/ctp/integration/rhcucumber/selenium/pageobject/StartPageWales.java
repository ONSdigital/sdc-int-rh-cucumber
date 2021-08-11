package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.StartPage;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartPageWales extends PageObjectBase implements StartPage {

  public static final String START_URL_SUFFIX = "cy/start/";
  private String testUPRN = "100100645811";

  public StartPageWales(final WebDriver driver, final String urlPrefix) {
    super(driver);
    classPrefix = "Dechrau:";
    startURL = urlPrefix + START_URL_SUFFIX;
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

  @FindBy(xpath = WebPageConstants.XPATH_LINK_WALES_REQUEST_A_NEW_CODE)
  private WebElement requestNewCodeLink;

  @FindBy(xpath = WebPageConstants.XPATH_LINK_WALES_WEB_CHAT)
  private WebElement webChatLink;

  @FindBy(xpath = WebPageConstants.XPATH_LINK_CHANGE_LANGUAGE)
  private WebElement englishLink;

  public String getErrorEnterAccessCodeText() {
    waitForElement(errorEnterAccessCode, classPrefix + "errorEnterAccessCode");
    return errorEnterAccessCode.getText();
  }

  public String getErrorEnterValidCodeText() {
    waitForElement(errorEnterValidCode, classPrefix + "errorEnterValidCode");
    return errorEnterValidCode.getText();
  }

  public void clickAccessSurveyButton() {
    waitForElement(accessSurveyButton, classPrefix + "accessSurveyButton");
    accessSurveyButton.click();
  }

  public void clickUacBox() {
    waitForElement(uacTextBox, classPrefix + "uacTextBox");
    uacTextBox.click();
  }

  private void addTextToUac(String txtToAdd) {
    waitForElement(uacTextBox, classPrefix + "uacTextBox");
    uacTextBox.sendKeys(txtToAdd);
  }

  public void enterUac(String uac) {
    clickUacBox();
    addTextToUac(uac);
  }

  public void clickRequestNewCodeLink() {
    waitForElement(requestNewCodeLink, classPrefix + "requestNewCodeLink");
    requestNewCodeLink.click();
  }

  public void clickWebChatLink() {
    waitForElement(webChatLink, classPrefix + "webChatLink");
    webChatLink.click();
  }

  @Override
  public void clickAlternativeLanguageLink() {
    clickEnglishLink();
  }

  private void clickEnglishLink() {
    waitForElement(englishLink, classPrefix + "englishLink");
    englishLink.click();
  }
}
