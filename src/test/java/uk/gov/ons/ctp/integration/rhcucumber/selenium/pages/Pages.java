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
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.IsThisMobileNumCorrect;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.NewHouseholdAccessCode;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.PleaseSupplyYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.RegisterYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.SelectDeliveryMethodTextOrPost;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.SelectYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.SentAccessCode;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.StartPage;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.WhatIsYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.WhatIsYourMobile;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.WhatIsYourName;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.ConsentToSIS2Survey;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.RegisterAChildConfirmationPage;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.RegisterAChildStartPage;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.RegisterChildDOB;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.RegisterChildName;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.RegisterChildSchool;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.RegisterParentMobile;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.RegisterParentName;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.RegisterSIS2StartPage;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.ReviewChildDetail;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis.SIS2HowToTakePart;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class Pages {

  @Value("${rhui.baseurl}")
  private String envBaseUrl;

  @Autowired private WebDriverFactory webDriverFactory;
  private WebDriver webDriver;
  private PageTracker pageTracker;

  private StartPage startPage;
  private ConfirmAddress confirmAddress;
  private WhatIsYourAddress whatIsYourAddress;
  private SelectDeliveryMethodTextOrPost selectDeliveryMethodTextOrPost;
  private IsThisMobileNumCorrect isThisMobileNumCorrect;
  private SelectYourAddress selectYourAddress;
  private SentAccessCode sentAccessCode;
  private PleaseSupplyYourAddress pleaseSupplyYourAddress;
  private RegisterSIS2StartPage registerSIS2StartPage;
  private SIS2HowToTakePart sis2HowToTakePart;
  private RegisterAChildStartPage registerAChildStartPage;
  private RegisterParentName registerParentName;
  private RegisterParentMobile registerParentMobile;
  private ConsentToSIS2Survey consentToSIS2Survey;
  private RegisterChildName registerChildName;
  private RegisterChildSchool registerChildSchool;
  private RegisterChildDOB registerChildDOB;
  private ReviewChildDetail reviewChildDetail;
  private RegisterAChildConfirmationPage registerAChildConfirmationPage;

  @PostConstruct
  private void setupWebDriver() {
    this.webDriver = webDriverFactory.getWebDriver();
    this.pageTracker = new PageTracker(webDriver);
  }

  public ConfirmAddress getConfirmAddress(final Country country) {
    confirmAddress = new ConfirmAddress(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.CONFIRM_ADDRESS, country);
    return confirmAddress;
  }

  public ConfirmAddress getConfirmAddress() {
    pageTracker.verifyCurrentPage(PageId.CONFIRM_ADDRESS, confirmAddress.getCountry());
    return confirmAddress;
  }
  
  public IsThisMobileNumCorrect getIsThisMobileNumCorrect(final Country country) {
    isThisMobileNumCorrect = new IsThisMobileNumCorrect(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.IS_THIS_MOBILE_NUM_CORRECT, country);
    return isThisMobileNumCorrect;
  }

  public IsThisMobileNumCorrect getIsThisMobileNumCorrect() {
    pageTracker.verifyCurrentPage(
        PageId.IS_THIS_MOBILE_NUM_CORRECT, isThisMobileNumCorrect.getCountry());
    return isThisMobileNumCorrect;
  }

  public StartPage getStartPage(final Country country) {
    startPage = new StartPage(webDriver, envBaseUrl, country);
    pageTracker.verifyCurrentPage(PageId.START_PAGE, country);
    return startPage;
  }

  public StartPage getStartPage() {
    pageTracker.verifyCurrentPage(PageId.START_PAGE, startPage.getCountry());
    return startPage;
  }

  public RegisterSIS2StartPage getRegisterSis2StartPage(final Country country) {
    registerSIS2StartPage = new RegisterSIS2StartPage(webDriver, envBaseUrl, country);
    pageTracker.verifyCurrentPage(PageId.REGISTER_SIS2, country);
    return registerSIS2StartPage;
  }

  public RegisterSIS2StartPage getRegisterSis2StartPage() {
    pageTracker.verifyCurrentPage(PageId.REGISTER_SIS2, registerSIS2StartPage.getCountry());
    return registerSIS2StartPage;
  }

  public SIS2HowToTakePart getSisPage(final Country country) {
    sis2HowToTakePart = new SIS2HowToTakePart(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.HOW_TO_TAKE_PART_SIS, country);
    return sis2HowToTakePart;
  }

  public SIS2HowToTakePart getSisPage() {
    pageTracker.verifyCurrentPage(PageId.HOW_TO_TAKE_PART_SIS, sis2HowToTakePart.getCountry());
    return sis2HowToTakePart;
  }

  public RegisterAChildStartPage getRegisterAChildStartPage(final Country country) {
    registerAChildStartPage = new RegisterAChildStartPage(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.REGISTER_A_CHILD, country);
    return registerAChildStartPage;
  }

  public RegisterAChildStartPage getRegisterAChildStartPage() {
    pageTracker.verifyCurrentPage(PageId.REGISTER_A_CHILD, registerAChildStartPage.getCountry());
    return registerAChildStartPage;
  }

  public RegisterParentName getRegisterParentName(final Country country) {
    registerParentName = new RegisterParentName(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.REGISTER_PARENT_NAME, country);
    return registerParentName;
  }

  public RegisterParentName getRegisterParentName() {
    pageTracker.verifyCurrentPage(PageId.REGISTER_PARENT_NAME, registerParentName.getCountry());
    return registerParentName;
  }

  public RegisterParentMobile getRegisterParentMobile(final Country country) {
    registerParentMobile = new RegisterParentMobile(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.REGISTER_PARENT_MOBILE, country);
    return registerParentMobile;
  }

  public RegisterParentMobile getRegisterParentMobile() {
    pageTracker.verifyCurrentPage(PageId.REGISTER_PARENT_MOBILE, registerParentMobile.getCountry());
    return registerParentMobile;
  }

  public ConsentToSIS2Survey getConsentToSIS2Survey(final Country country) {
    consentToSIS2Survey = new ConsentToSIS2Survey(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.CONFIRM_CONSENT, country);
    return consentToSIS2Survey;
  }

  public ConsentToSIS2Survey getConsentToSIS2Survey() {
    pageTracker.verifyCurrentPage(PageId.CONFIRM_CONSENT, consentToSIS2Survey.getCountry());
    return consentToSIS2Survey;
  }

  public RegisterChildName getRegisterChildName(final Country country) {
    registerChildName = new RegisterChildName(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.REGISTER_CHILD_NAME, country);
    return registerChildName;
  }

  public RegisterChildName getRegisterChildName() {
    pageTracker.verifyCurrentPage(PageId.REGISTER_CHILD_NAME, registerChildName.getCountry());
    return registerChildName;
  }

  public RegisterChildSchool getRegisterChildSchool(final Country country) {
    registerChildSchool = new RegisterChildSchool(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.REGISTER_CHILD_SCHOOL, country);
    return registerChildSchool;
  }

  public RegisterChildSchool getRegisterChildSchool() {
    pageTracker.verifyCurrentPage(PageId.REGISTER_CHILD_SCHOOL, registerChildSchool.getCountry());
    return registerChildSchool;
  }

  public RegisterChildDOB getRegisterChildDOB(final Country country) {
    registerChildDOB = new RegisterChildDOB(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.REGISTER_CHILD_DOB, country);
    return registerChildDOB;
  }

  public RegisterChildDOB getRegisterChildDOB() {
    pageTracker.verifyCurrentPage(PageId.REGISTER_CHILD_DOB, registerChildSchool.getCountry());
    return registerChildDOB;
  }

  public ReviewChildDetail getReviewChildDetail(final Country country) {
    reviewChildDetail = new ReviewChildDetail(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.REVIEW_CHILD_DETAIL, country);
    return reviewChildDetail;
  }

  public ReviewChildDetail getReviewChildDetail() {
    pageTracker.verifyCurrentPage(PageId.REVIEW_CHILD_DETAIL, reviewChildDetail.getCountry());
    return reviewChildDetail;
  }

  public RegisterAChildConfirmationPage getRegisterAChildConfirmationPage(final Country country) {
    registerAChildConfirmationPage = new RegisterAChildConfirmationPage(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.REGISTRATION_OF_CHILD_CONFIRMATION, country);
    return registerAChildConfirmationPage;
  }

  public WebDriver getWebDriver() {
    return webDriver;
  }
}
