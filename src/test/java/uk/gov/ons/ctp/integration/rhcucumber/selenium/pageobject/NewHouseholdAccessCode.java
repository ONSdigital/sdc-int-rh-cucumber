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

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewHouseholdAccessCode extends PageObjectBase {
  public NewHouseholdAccessCode(WebDriver webDriver, Country country) {
    super(webDriver);
    classPrefix = "NewHouseholdAccessCodeENG-" + country.name() + ":";
    waitForLoading();
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath = WebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement continueButton;

  @FindBy(xpath = WebPageConstants.XPATH_RADIO_POST_ACCESS_CODE_YES)
  private WebElement optionYes;

  @FindBy(xpath = WebPageConstants.XPATH_RADIO_POST_ACCESS_CODE_NO)
  private WebElement optionNo;

  public void clickContinueButton() {
    waitForElement(continueButton, classPrefix + "continueButton");
    continueButton.click();
  }

  public void selectYesSendByPost() {
    waitForElement(optionYes, classPrefix + "optionYes");
    optionYes.click();
  }

  public void selectNoSendAnotherWay() {
    waitForElement(optionNo, classPrefix + "optionNo");
    optionNo.click();
  }
}
