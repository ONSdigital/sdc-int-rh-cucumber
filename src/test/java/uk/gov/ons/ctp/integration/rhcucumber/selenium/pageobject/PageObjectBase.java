package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import uk.gov.ons.ctp.common.util.Wait;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Translations;

public abstract class PageObjectBase {
  private Translations constants;
  protected WebDriver driver;
  private Wait wait;
  protected String classPrefix;
  protected String startURL;

  public PageObjectBase(PageId pageId, WebDriver driver, Country country) {
    this.constants = new Translations(country);
    this.classPrefix = pageId.name() + "-" + country.name() + ":";
    this.driver = driver;
    wait = new Wait(driver);
    waitForLoading();
    PageFactory.initElements(driver, this);
  }

  protected void waitForElement(final WebElement element, final String identifier) {
	wait.forElementToBeDisplayed(5, element, classPrefix + identifier);
  }

  protected void waitForElement(
    final int timeout, final WebElement element, final String identifier) {
    wait.forElementToBeDisplayed(timeout, element, classPrefix + identifier);
  }

  protected void waitForLoading() {
    wait.forLoading();
  }

  public String translate(Translations.KEYS key) {
    return constants.get(key);
  }
  
  public Country getCountry() {
    return constants.getCountry();
  }
}
