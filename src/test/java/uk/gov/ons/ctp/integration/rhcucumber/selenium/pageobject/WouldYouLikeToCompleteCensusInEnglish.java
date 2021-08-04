package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WouldYouLikeToCompleteCensusInEnglish extends PageObjectBase {

  public WouldYouLikeToCompleteCensusInEnglish(WebDriver driver) {
    super(driver);
    classPrefix = "WouldYouLikeToCompleteCensusInEnglish:";
    waitForLoading();
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_RADIO_ADDRESS_YES)
  private WebElement optionYes;

  @FindBy(xpath = WebPageConstants.XPATH_RADIO_ADDRESS_NO)
  private WebElement optionNo;

  @FindBy(xpath = WebPageConstants.XPATH_SAVE_AND_CONTINUE_BUTTON)
  private WebElement continueButton;

  public void clickContinueButton() {
    waitForElement(continueButton, classPrefix + "continueButton");
    continueButton.click();
  }

  public void clickOptionYes() {
    waitForElement(optionYes, classPrefix + "optionYes");
    optionYes.click();
  }

  public void clickOptionNo() {
    waitForElement(optionNo, classPrefix + "optionNo");
    optionNo.click();
  }
}
