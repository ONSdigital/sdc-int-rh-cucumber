package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import javax.annotation.PostConstruct;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import uk.gov.ons.ctp.common.util.WebDriverFactory;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.ConfirmAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.ConfirmAddressForNewUac;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.HouseholdInterstitial;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.StartPage;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class Pages {

  @Value("${rhui.baseurl}")
  private String envBaseUrl;

  @Autowired private WebDriverFactory webDriverFactory;
  private WebDriver webDriver;

  private StartPage startPage = null;
  private ConfirmAddress confirmAddress = null;
  private WhatIsYourAddress whatIsYourAddress;
  private SelectDeliveryMethodTextOrPost selectDeliveryMethodTextOrPost = null;
  private IsThisMobileNumCorrect isThisMobileNumCorrect = null;
  private SelectYourAddress selectYourAddress = null;
  private SentAccessCode sentAccessCode = null;
  private PleaseSupplyYourAddress pleaseSupplyYourAddress;

  @PostConstruct
  private void setupWebDriver() {
    this.webDriver = webDriverFactory.getWebDriver();
  }

  public ConfirmAddress getConfirmAddress(final Country country) {
    confirmAddress = new ConfirmAddress(webDriver, country);
    return confirmAddress;
  }

  public ConfirmAddress getConfirmAddress() {
    return confirmAddress;
  }

  public ConfirmAddressForNewUac getConfirmAddressForNewUac(final Country country) {
    return new ConfirmAddressForNewUac(webDriver, country);
  }

  public SelectDeliveryMethodTextOrPost getSelectDeliveryMethodTextOrPost(final Country country) {
    selectDeliveryMethodTextOrPost =
        SelectDeliveryMethodTextOrPost.getSelectDeliveryMethodTextOrPost(webDriver, country);
    return selectDeliveryMethodTextOrPost;
  }

  public SelectDeliveryMethodTextOrPost getSelectDeliveryMethodTextOrPost() {
    return selectDeliveryMethodTextOrPost;
  }

  public IsThisMobileNumCorrect getIsThisMobileNumCorrect(final Country country) {
    isThisMobileNumCorrect = IsThisMobileNumCorrect.getIsThisMobileNumCorrect(webDriver, country);
    return isThisMobileNumCorrect;
  }

  public IsThisMobileNumCorrect getIsThisMobileNumCorrect() {
    return isThisMobileNumCorrect;
  }

  public PleaseSupplyYourAddress getPleaseSupplyYourAddress(final Country country) {
    pleaseSupplyYourAddress =
        PleaseSupplyYourAddress.getPleaseSupplyYourAddress(webDriver, country);
    return pleaseSupplyYourAddress;
  }

  public PleaseSupplyYourAddress getPleaseSupplyYourAddress() {
    return pleaseSupplyYourAddress;
  }

  public SelectYourAddress getSelectYourAddress(final Country country) {
    selectYourAddress = SelectYourAddress.getSelectYourAddress(webDriver, country);
    return selectYourAddress;
  }

  public SelectYourAddress getSelectYourAddress() {
    return selectYourAddress;
  }

  public SentAccessCode getSentAccessCode(final Country country) {
    sentAccessCode = SentAccessCode.getSentAccessCode(webDriver, country);
    return sentAccessCode;
  }

  public SentAccessCode getSentAccessCode() {
    return sentAccessCode;
  }

  public StartPage getStartPage(final Country country) {
    startPage = StartPage.getStartPage(webDriver, country, envBaseUrl);
    return startPage;
  }

  public StartPage getStartPage() {
    return startPage; //PMB Delete?
  }

  public WhatIsYourMobile getWhatIsYourMobile(final Country country) {
    return WhatIsYourMobile.getWhatIsYourMobile(webDriver, country);
  }

  public WhatIsYourAddress getWhatIsYourAddress(final Country country) {
    whatIsYourAddress = WhatIsYourAddress.getWhatIsYourAddress(webDriver, country);
    return whatIsYourAddress;
  }

  public WhatIsYourAddress getWhatIsYourAddress() {
    return whatIsYourAddress;
  }

  public WhatIsYourName getWhatIsYourName(final Country country) {
    return WhatIsYourName.getWhatIsYourName(webDriver, country);
  }

  public NewHouseholdAccessCode getNewHouseholdAccessCode(final Country country) {
    return NewHouseholdAccessCode.getNewHouseholdAccessCode(webDriver, country);
  }

  public RegisterYourAddress getRegisterYourAddress(final Country country) {
    return RegisterYourAddress.getRegisterYourAddress(webDriver, country);
  }

  public HouseholdInterstitial getHouseholdInterstitial(final Country country) {
    return new HouseholdInterstitial(webDriver, country);
  }

  public WebDriver getWebDriver() {
    return webDriver;
  }
}
