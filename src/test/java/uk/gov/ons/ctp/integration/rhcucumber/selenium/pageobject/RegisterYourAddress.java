package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Translations.KEYS;

@Getter
public class RegisterYourAddress extends PageObjectBase {

  private String expectedTitleText;
  private String expectedTextWithPhoneNumber;

  public RegisterYourAddress(WebDriver driver, Country country) {
    super(PageId.REGISTER_YOUR_ADDRESS, driver, country);
    waitForLoading();
    PageFactory.initElements(driver, this);

    expectedTitleText = translate(KEYS.REGISTER_YOUR_ADDRESS_EXPECTED_TITLE_TEXT);
    expectedTextWithPhoneNumber =
        translate(KEYS.REGISTER_YOUR_ADDRESS_EXPECTED_TEXT_WITH_PHONE_NUMBER);
  }

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement registerYourAddressTitle;

  @FindBy(xpath = WebPageConstants.XPATH_P_WITH_PHONE_NUMBER)
  private WebElement pWithPhoneNumer;

  public String getTitleText() {
    return registerYourAddressTitle.getText();
  }

  public String getTextWithPhoneNumber() {
    return pWithPhoneNumer.getText();
  }
}
