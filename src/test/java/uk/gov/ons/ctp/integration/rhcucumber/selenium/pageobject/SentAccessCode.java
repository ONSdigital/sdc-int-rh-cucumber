package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Translations.KEYS;

@Getter
public class SentAccessCode extends PageObjectBase {

  private String expectedText = "A text has been sent to 07700 900345";
  private String expectedRequestCodeText = "request a new access code";

  public SentAccessCode(WebDriver driver, Country country) {
    super(PageId.SENT_ACCESS_CODE, driver, country);

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
    waitForElement(sentAccessCodeTitle, "sentAccessCodeTitle");
    return sentAccessCodeTitle.getText();
  }

  public String getStartSurveyButtonText() {
    waitForElement(startSurveyButton, "startSurveyButton");
    return startSurveyButton.getText();
  }

  public String getRequestNewCodeLinkText() {
    waitForElement(requestNewCodeLink, "requestNewCodeLink");
    return requestNewCodeLink.getText();
  }
}
