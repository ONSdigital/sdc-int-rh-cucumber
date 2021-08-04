package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.ChooseLanguage;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChooseLanguageImpl extends PageObjectBase implements ChooseLanguage {

  public ChooseLanguageImpl(WebDriver driver) {
    super(driver);
    classPrefix = "ChooseLanguageImpl:";
    waitForLoading();
    PageFactory.initElements(driver, this);
  }

  @FindBy(xpath = WebPageConstants.XPATH_LOGO)
  private WebElement onsLogo;

  @FindBy(xpath = WebPageConstants.XPATH_PAGE_CONTENT_TITLE)
  private WebElement chooseLanguageTitle;

  @FindBy(how = How.ID, using = "yes")
  private WebElement optionYes;

  @FindBy(how = How.ID, using = "no")
  private WebElement optionNo;

  @FindBy(xpath = WebPageConstants.XPATH_CONTINUE_BUTTON)
  private WebElement continueButton;

  @Override
  public String getChooseLanguageTitleText() {
    waitForElement(chooseLanguageTitle, classPrefix + "chooseLanguageTitle");
    return chooseLanguageTitle.getText();
  }

  @Override
  public void clickContinueButton() {
    waitForElement(continueButton, classPrefix + "continueButton");
    continueButton.click();
  }

  @Override
  public void clickOptionYes() {
    waitForElement(optionYes, classPrefix + "optionYes");
    optionYes.click();
  }
}
