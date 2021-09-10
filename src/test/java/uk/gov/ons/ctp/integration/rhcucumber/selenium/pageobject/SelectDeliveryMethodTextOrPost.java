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
public class SelectDeliveryMethodTextOrPost extends PageObjectBase {

  private String expectedSelectDeliveryMethodTextOrPostText =
      "How would you like to receive a new household access code?";

  public SelectDeliveryMethodTextOrPost(WebDriver driver, Country country) {
    super(driver);
    classPrefix = "SelectDeliveryMethodTextOrPost-" + country.name() + ":";
    waitForLoading();
    PageFactory.initElements(driver, this);
    
    expectedSelectDeliveryMethodTextOrPostText = translate(KEYS.SELECT_DELIVERY_METHOD_TEXT_OR_POST_EXPECTED_DELIVERY_TEXT);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement selectDeliveryMethodTextOrPostTitle;

  @FindBy(xpath = WebPageConstants.XPATH_RADIO_METHOD_TEXT)
  private WebElement optionText;

  @FindBy(xpath = WebPageConstants.XPATH_RADIO_METHOD_POST)
  private WebElement optionPost;

  @FindBy(xpath = WebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement continueButton;

  @FindBy(xpath = WebPageConstants.XPATH_LINK_CHANGE_LANGUAGE)
  private WebElement changeLanguageLink;

  public WebElement getOnsLogo() {
    waitForElement(onsLogo, classPrefix + "onsLogo");
    return onsLogo;
  }

  public String getSelectDeliveryMethodTextOrPostTitleText() {
    waitForElement(
        selectDeliveryMethodTextOrPostTitle, classPrefix + "selectDeliveryMethodTextOrPostTitle");
    return selectDeliveryMethodTextOrPostTitle.getText();
  }

  public void clickContinueButton() {
    waitForElement(continueButton, classPrefix + "continueButton");
    continueButton.click();
  }

  public void clickOptionText() {
    waitForElement(optionText, classPrefix + "optionText");
    optionText.click();
  }

  public void clickOptionPost() {
    waitForElement(optionPost, classPrefix + "optionPost");
    optionPost.click();
  }
  
  public void clickEnglishLink() {
    waitForElement(changeLanguageLink, classPrefix + "changeLanguageLink");
    changeLanguageLink.click();
  }
}
