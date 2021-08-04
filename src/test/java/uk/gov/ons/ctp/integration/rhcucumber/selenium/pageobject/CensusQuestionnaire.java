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
public class CensusQuestionnaire extends PageObjectBase {

  public CensusQuestionnaire(WebDriver driver) {
    super(driver);
    classPrefix = "CensusQuestionnaire:";
    waitForLoading();
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement censusLogo;

  public void clickCensusLogo() {
    waitForElement(censusLogo, classPrefix + "censusLogo");
    censusLogo.click();
  }
}
