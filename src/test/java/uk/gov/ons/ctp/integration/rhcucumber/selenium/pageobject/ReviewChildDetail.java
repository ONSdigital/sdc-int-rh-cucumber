package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;

@Getter
public class ReviewChildDetail extends PageObjectBase {

  public ReviewChildDetail(WebDriver driver, Country country) {
    super(PageId.REVIEW_CHILD_DETAIL, driver, country);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement reviewChildDetail;

  @FindBy(xpath = WebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement saveAndContinueButton;

  public WebElement getOnsLogo() {
    waitForElement(onsLogo, "onsLogo");
    return onsLogo;
  }

  public String getReviewChildDetailTitleText() {
    waitForElement(reviewChildDetail, "reviewChildDetail");
    return reviewChildDetail.getText();
  }

  public void clickSaveAndContinueButton() {
    waitForElement(saveAndContinueButton, "saveAndContinueButton");
    saveAndContinueButton.click();
  }
}
