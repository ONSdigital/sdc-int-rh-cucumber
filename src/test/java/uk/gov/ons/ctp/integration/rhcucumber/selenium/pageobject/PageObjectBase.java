package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.gov.ons.ctp.common.util.Wait;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Translations;

public abstract class PageObjectBase {
  private Translations constants;
  protected WebDriver driver;
  private Wait wait;
  protected String classPrefix;
  protected String startURL;

  public PageObjectBase() {}

  public PageObjectBase(WebDriver driver) {
    // PMB Delete method
    this(driver, Country.ENG);
  }

  public PageObjectBase(WebDriver driver, Country country) {
    this.constants = new Translations(country);
    this.driver = driver;
    wait = new Wait(driver);
    wait.forLoading();
  }

  protected void waitForElement(final WebElement element, final String identifier) {
	wait.forElementToBeDisplayed(5, element, identifier);
  }

  protected void waitForElement(
      final int timeout, final WebElement element, final String identifier) {
    wait.forElementToBeDisplayed(timeout, element, identifier);
  }

  protected void waitForLoading() {
    wait.forLoading();
  }

  public String getStartURL() {
    return startURL;
  }
  
  public String translate(Translations.KEYS key) {
    return constants.get(key);
  }
  
  public Country getCountry() {
    return constants.getCountry();
  }
}
