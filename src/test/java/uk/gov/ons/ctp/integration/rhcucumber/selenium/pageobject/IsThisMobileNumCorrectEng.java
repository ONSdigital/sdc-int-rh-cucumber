package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.IsThisMobileNumCorrect;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IsThisMobileNumCorrectEng extends PageObjectBase implements IsThisMobileNumCorrect {

  private String expectedText = "Is this mobile number correct?";

  public IsThisMobileNumCorrectEng(WebDriver driver) {
    super(driver);
    classPrefix = "IsThisMobileNumCorrect:";
    waitForLoading();
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement isMobileCorrectTitle;

  @FindBy(xpath = WebPageConstants.XPATH_RADIO_MOBILE_YES)
  private WebElement optionYes;

  @FindBy(xpath = WebPageConstants.XPATH_RADIO_MOBILE_NO)
  private WebElement optionNo;

  @FindBy(xpath = WebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement continueButton;

  @Override
  public String getIsMobileCorrectTitleText() {
    waitForElement(isMobileCorrectTitle, classPrefix + "isMobileCorrectTitle");
    return isMobileCorrectTitle.getText();
  }

  @Override
  public void clickContinueButton() {
    waitForElement(continueButton, classPrefix + "continueButton");
    continueButton.click();
  }

  @Override
  public void clickOptionYes() {
    waitForElement(optionYes, classPrefix + "optionYes");
    optionYes.click();
  }

  @Override
  public void clickOptionNo() {
    waitForElement(optionNo, classPrefix + "optionNo");
    optionNo.click();
  }
}
