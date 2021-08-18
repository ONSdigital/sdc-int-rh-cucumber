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
public class SentAccessCodeWales extends PageObjectBase implements SentAccessCode {

  private String expectedText = "Mae neges destun wedi cael ei hanfon i 07700 900345";
  private String expectedRequestCodeText = "ofyn am god mynediad newydd";

  public SentAccessCodeWales(WebDriver driver) {
    super(driver);
    classPrefix = "SentAccessCodeWales:";
    waitForLoading();
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement sentAccessCodeTitle;

  @FindBy(xpath = WebPageConstants.XPATH_BUTTON_REQUEST_A_NEW_CODE)
  private WebElement startSurveyButton;

  @FindBy(xpath = WebPageConstants.XPATH_LINK_WALES_REQUEST_A_NEW_CODE_SENT)
  private WebElement requestNewCodeLink;

  @FindBy(xpath = WebPageConstants.XPATH_LINK_WALES_REQUEST_A_NEW_CODE_SENT)
  private WebElement requestNewCodeLinkSent;

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

  public String getRequestNewCodeLinkSentText() {
    waitForElement(requestNewCodeLinkSent, classPrefix + "requestNewCodeLink");
    return requestNewCodeLinkSent.getText();
  }
}
