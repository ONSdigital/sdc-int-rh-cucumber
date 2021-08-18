package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.HouseholdInterstitial;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseholdInterstitialEng extends PageObjectBase implements HouseholdInterstitial {

  private String expectedText = "Request a new household access code";

  public HouseholdInterstitialEng(WebDriver driver) {
    super(driver);
    classPrefix = "HouseholdInterstitial:";
    waitForLoading();
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement householdInterstitialTitle;

  @FindBy(xpath = WebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement continueButton;

  @Override
  public WebElement getOnsLogo() {
    waitForElement(onsLogo, classPrefix + "onsLogo");
    return onsLogo;
  }

  @Override
  public void clickContinueButton() {
    waitForElement(continueButton, classPrefix + "continueButton");
    continueButton.click();
  }

  @Override
  public String getHouseholdInterstitialTitleText() {
    return householdInterstitialTitle.getText();
  }
}
