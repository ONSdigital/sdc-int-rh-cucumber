package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;

@Getter
public class SIS2HowToTakePart extends PageObjectBase {

  public SIS2HowToTakePart(final WebDriver driver, Country country) {
    super(PageId.HOW_TO_TAKE_PART_SIS, driver, country);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement sis2Title;

  @FindBy(xpath = WebPageConstants.XPATH_BUTTON_REGISTER_SIS2_SURVEY)
  private WebElement registerForSurvey;

  public String getSis2TitleText() {
    waitForElement(sis2Title, "sis2Title");
    return sis2Title.getText();
  }

  public void clickRegisterForSurveyButton() {
    waitForElement(registerForSurvey, "loadSIS2Survey");
    registerForSurvey.click();
  }
}
