package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.LimitExceedPage;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LimitExceedPageEng extends PageObjectBase implements LimitExceedPage {
  private String expectedMessage =
      "You have reached the maximum number of access codes you can request online";

  @FindBy(xpath = WebPageConstants.XPATH_LIMIT_EXCEED_PAGE_MESSAGE)
  private WebElement limitExceedMessage;

  public LimitExceedPageEng(WebDriver webDriver, String envBaseUrl) {

    super(webDriver);
    classPrefix = "limitExceedPageEng:";
    waitForLoading();
    PageFactory.initElements(driver, this);
  }

  @Override
  public String getMessage() {
    waitForElement(limitExceedMessage, classPrefix + "confirmMessageSentText");
    return limitExceedMessage.getText();
  }
}
