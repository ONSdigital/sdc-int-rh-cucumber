package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterYourAddress extends PageObjectBase {

  private String expectedTitleText = "Register an address";
  private String expectedTextWithPhoneNumber =
      "To register your address, we need you to get in touch. You can call us free on 0800 141 2021 or choose another way to contact us.";

  public RegisterYourAddress(WebDriver driver) {
    super(driver);
    classPrefix = "RegisterYourAddressEng:";
    waitForLoading();
    PageFactory.initElements(driver, this);
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
