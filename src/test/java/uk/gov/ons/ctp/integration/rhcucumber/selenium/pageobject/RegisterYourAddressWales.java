package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.RegisterYourAddress;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterYourAddressWales extends PageObjectBase implements RegisterYourAddress {

  private String expectedTitleText = "Cofrestru cyfeiriad";
  private String expectedTextWithPhoneNumber =
      "I gofrestru eich cyfeiriad, bydd angen i chi gysylltu â ni. Gallwch chi ein ffonio ni am ddim ar 0800 169 2021 neu ddewis ffordd arall o gysylltu â ni.";

  public RegisterYourAddressWales(WebDriver driver) {
    super(driver);
    classPrefix = "RegisterYourAddressWales:";
    waitForLoading();
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath = WebPageConstants.XPATH_P_WITH_PHONE_NUMBER)
  private WebElement paragraphWithPhoneNumer;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement registerYourAddressTitle;

  @Override
  public String getTitleText() {
    return registerYourAddressTitle.getText();
  }

  @Override
  public String getTextWithPhoneNumber() {
    return paragraphWithPhoneNumer.getText();
  }
}
