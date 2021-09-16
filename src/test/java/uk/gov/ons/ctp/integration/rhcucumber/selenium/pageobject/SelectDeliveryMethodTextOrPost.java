package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import lombok.Getter;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Translations.KEYS;

@Getter
public class SelectDeliveryMethodTextOrPost extends PageObjectBase {

  private String expectedSelectDeliveryMethodTextOrPostText =
      "How would you like to receive a new household access code?";

  public SelectDeliveryMethodTextOrPost(WebDriver driver, Country country) {
    super(PageId.SELECT_DELIVERY_METHOD_TEXT_OR_POST, driver, country);
    
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
    waitForElement(onsLogo, "onsLogo");
    return onsLogo;
  }

  public String getSelectDeliveryMethodTextOrPostTitleText() {
    waitForElement(
        selectDeliveryMethodTextOrPostTitle, "selectDeliveryMethodTextOrPostTitle");
    return selectDeliveryMethodTextOrPostTitle.getText();
  }

  public void clickContinueButton() {
    waitForElement(continueButton, "continueButton");
    continueButton.click();
  }

  public void clickOptionText() {
    waitForElement(optionText, "optionText");
    optionText.click();
  }

  public void clickOptionPost() {
    waitForElement(optionPost, "optionPost");
    optionPost.click();
  }
  
  public void clickEnglishLink() {
    waitForElement(changeLanguageLink, "changeLanguageLink");
    changeLanguageLink.click();
  }
}
