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
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Translations.KEYS;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentAccessCode extends PageObjectBase {

  private String expectedText = "A text has been sent to 07700 900345";
  private String expectedRequestCodeText = "request a new access code";

  public SentAccessCode(WebDriver driver, Country country) {
    super(driver);
    classPrefix = "SentAccessCode-" + country.name() + ":";
    waitForLoading();
    PageFactory.initElements(driver, this);
    
    expectedText = translate(KEYS.SENT_ACCESS_CODE_EXPECTED_TEXT);
    expectedRequestCodeText = translate(KEYS.SENT_ACCESS_CODE_EXPECTED_REQUEST_CODE_TEXT);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement sentAccessCodeTitle;

  @FindBy(xpath = WebPageConstants.XPATH_BUTTON_REQUEST_A_NEW_CODE)
  private WebElement startSurveyButton;

  @FindBy(xpath = WebPageConstants.XPATH_LINK_REQUEST_A_NEW_CODE)
  private WebElement requestNewCodeLink;

  public String getSentAccessCodeTitleText() {
    waitForElement(sentAccessCodeTitle, classPrefix + "sentAccessCodeTitle");
    return sentAccessCodeTitle.getText();
  }

  public String getStartSurveyButtonText() {
    waitForElement(startSurveyButton, classPrefix + "startSurveyButton");
    return startSurveyButton.getText();
  }

  public String getRequestNewCodeLinkText() {
    waitForElement(requestNewCodeLink, classPrefix + "requestNewCodeLink");
    return requestNewCodeLink.getText();
  }
}
