package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.WebFormPage;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebFormPageEng extends PageObjectBase implements WebFormPage {
  public static final String START_URL_SUFFIX = "en/web-form/";
  public static final String START_URL_SUCCESS_SUFFIX = "en/web-form/success/";
  private String expectedContactUsText = "Thank you for contacting us";
  private String expectedSentText = "Your message has been sent";

  @FindBy(xpath = WebPageConstants.XPATH_DIV_QUERY_TYPE_HIGHLIGHT)
  private WebElement typeOfQueryHighlight;

  @FindBy(xpath = WebPageConstants.XPATH_DIV_SELECT_COUNTRY_HIGHLIGHT)
  private WebElement selectCountryHighlight;

  @FindBy(xpath = WebPageConstants.XPATH_SEND_MESSAGE_BUTTON)
  private WebElement sendMessageButton;

  @FindBy(xpath = WebPageConstants.XPATH_MORE_DETAIL)
  private WebElement moreDetailTextBox;

  @FindBy(xpath = WebPageConstants.XPATH_NAME)
  private WebElement nameBox;

  @FindBy(xpath = WebPageConstants.XPATH_EMAIL_ADDRESS)
  private WebElement emailAddressBox;

  @FindBy(xpath = WebPageConstants.XPATH_RADIO_COUNTRY)
  private WebElement country;

  @FindBy(xpath = WebPageConstants.XPATH_RADIO_QUERY_TYPE)
  private WebElement queryType;

  @FindBy(xpath = WebPageConstants.XPATH_WEB_FORM_PAGE_CONTENT_TITLE)
  private WebElement confirmWebformTitle;

  @FindBy(xpath = WebPageConstants.XPATH_WEB_FORM_MESSAGE_SENT_TEXT)
  private WebElement messageSentText;

  public WebFormPageEng(final WebDriver driver, final String urlPrefix, final PageType pageType) {
    super(driver);
    classPrefix = "WebFormPageEng:";
    startURL =
        urlPrefix
            + (pageType == PageType.SUCCESS_PAGE ? START_URL_SUCCESS_SUFFIX : START_URL_SUFFIX);
    driver.get(startURL);
    waitForLoading();
    PageFactory.initElements(driver, this);
  }

  @Override
  public void clickSendMessage() {
    waitForElement(30, sendMessageButton, classPrefix + "sendMessageButton");
    sendMessageButton.click();
  }

  @Override
  public void addTextToMoreDetailTextBox(String txtToAdd) {
    waitForElement(moreDetailTextBox, classPrefix + "moreDetailTextBox  ");
    moreDetailTextBox.sendKeys(txtToAdd);
  }

  @Override
  public void addName(String name) {
    waitForElement(nameBox, classPrefix + "nameBox");
    nameBox.sendKeys(name);
  }

  @Override
  public void addEmailAddress(String emailAddress) {
    waitForElement(emailAddressBox, classPrefix + "emailAddressBox");
    emailAddressBox.sendKeys(emailAddress);
  }

  @Override
  public void clickOptionCountry() {
    waitForElement(country, classPrefix + "country");
    country.click();
  }

  @Override
  public void clickOptionQueryType() {
    waitForElement(queryType, classPrefix + "queryType");
    queryType.click();
  }

  @Override
  public String getContactUsText() {
    waitForElement(confirmWebformTitle, classPrefix + "confirmWebformTitle");
    return confirmWebformTitle.getText();
  }

  @Override
  public String getMessageSentText() {
    waitForElement(messageSentText, classPrefix + "confirmMessageSentText");
    return messageSentText.getText();
  }
}
