package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.SentAccessCode;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentAccessCodeEng extends PageObjectBase implements SentAccessCode {

  private String expectedText = "A text has been sent to 07700 900345";
  private String expectedRequestCodeText = "request a new access code";

  public SentAccessCodeEng(WebDriver driver) {
    super(driver);
    classPrefix = "SentAccessCode:";
    waitForLoading();
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement sentAccessCodeTitle;

  @FindBy(xpath = WebPageConstants.XPATH_BUTTON_REQUEST_A_NEW_CODE)
  private WebElement startSurveyButton;

  @FindBy(xpath = WebPageConstants.XPATH_LINK_REQUEST_A_NEW_CODE)
  private WebElement requestNewCodeLink;

  @Override
  public String getSentAccessCodeTitleText() {
    waitForElement(sentAccessCodeTitle, classPrefix + "sentAccessCodeTitle");
    return sentAccessCodeTitle.getText();
  }

  @Override
  public String getStartSurveyButtonText() {
    waitForElement(startSurveyButton, classPrefix + "startSurveyButton");
    return startSurveyButton.getText();
  }

  @Override
  public String getRequestNewCodeLinkText() {
    waitForElement(requestNewCodeLink, classPrefix + "requestNewCodeLink");
    return requestNewCodeLink.getText();
  }
}
