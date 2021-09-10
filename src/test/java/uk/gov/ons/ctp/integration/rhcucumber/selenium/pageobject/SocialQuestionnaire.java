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
public class SocialQuestionnaire extends PageObjectBase {

  public SocialQuestionnaire(WebDriver driver, Country country) {
    super(driver, country);
    classPrefix = "SocialQuestionnaire:";
    waitForLoading();
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement socalLogo;

  public void clickSocialLogo() {
    waitForElement(socalLogo, classPrefix + "socialLogo");
    socalLogo.click();
  }
}
