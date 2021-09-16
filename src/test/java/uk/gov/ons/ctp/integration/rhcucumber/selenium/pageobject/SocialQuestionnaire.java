package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;

@Getter
public class SocialQuestionnaire extends PageObjectBase {

  public SocialQuestionnaire(WebDriver driver, Country country) {
    super(PageId.SOCIAL_QUESTIONNAIRE, driver, country);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement socalLogo;

  public void clickSocialLogo() {
    waitForElement(socalLogo, "socialLogo");
    socalLogo.click();
  }
}
