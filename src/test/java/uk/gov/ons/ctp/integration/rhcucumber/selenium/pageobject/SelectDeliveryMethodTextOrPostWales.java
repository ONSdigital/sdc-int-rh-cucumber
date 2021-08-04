package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.SelectDeliveryMethodTextOrPost;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectDeliveryMethodTextOrPostWales extends PageObjectBase
    implements SelectDeliveryMethodTextOrPost {

  private String expectedSelectDeliveryMethodTextOrPostText =
      "Sut hoffech chi gael cod mynediad newydd ar gyfer y cartref?";

  public SelectDeliveryMethodTextOrPostWales(WebDriver driver) {
    super(driver);
    classPrefix = "SelectDeliveryMethodTextOrPostWales:";
    waitForLoading();
    PageFactory.initElements(driver, this);
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
  private WebElement englishLink;

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
    waitForElement(englishLink, classPrefix + "englishLink");
    englishLink.click();
  }
}
