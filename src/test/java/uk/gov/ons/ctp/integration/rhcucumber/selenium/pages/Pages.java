package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import uk.gov.ons.ctp.common.util.WebDriverFactory;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.ConfirmAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.ConfirmAddressForNewUac;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.ConsentToSIS2Survey;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.HouseholdInterstitial;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.RegisterAChildConfirmationPage;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.RegisterAChildStartPage;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.RegisterChildDOB;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.RegisterChildName;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.RegisterChildSchool;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.RegisterParentMobile;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.RegisterParentName;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.ReviewChildDetail;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.SISPage;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.IsThisMobileNumCorrect;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.NewHouseholdAccessCode;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.PleaseSupplyYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.RegisterSIS2StartPage;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.RegisterYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.SelectDeliveryMethodTextOrPost;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.SelectYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.SentAccessCode;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.StartPage;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.WhatIsYourAddress;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.WhatIsYourMobile;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.WhatIsYourName;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.PageTracker.PageId;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@Slf4j
public class Pages {

  @Value("${rhui.baseurl}")
  private String envBaseUrl;

  @Autowired private WebDriverFactory webDriverFactory;
  private WebDriver webDriver;
  private PageTracker pageTracker;

  private StartPage startPage = null;
  private ConfirmAddress confirmAddress = null;
  private WhatIsYourAddress whatIsYourAddress;
  private SelectDeliveryMethodTextOrPost selectDeliveryMethodTextOrPost = null;
  private IsThisMobileNumCorrect isThisMobileNumCorrect = null;
  private SelectYourAddress selectYourAddress = null;
  private SentAccessCode sentAccessCode = null;
  private PleaseSupplyYourAddress pleaseSupplyYourAddress;
  private RegisterSIS2StartPage registerSIS2StartPage = null;
  private SISPage sisPage = null;
  private RegisterAChildStartPage registerAChildStartPage = null;
  private RegisterParentName registerParentName = null;
  private RegisterParentMobile registerParentMobile = null;
  private ConsentToSIS2Survey consentToSIS2Survey = null;
  private RegisterChildName registerChildName = null;
  private RegisterChildSchool registerChildSchool = null;
  private RegisterChildDOB registerChildDOB = null;
  private ReviewChildDetail reviewChildDetail = null;
  private RegisterAChildConfirmationPage registerAChildConfirmationPage = null;

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

  public ConfirmAddressForNewUac getConfirmAddressForNewUac(final Country country) {
    ConfirmAddressForNewUac confirm = new ConfirmAddressForNewUac(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.CONFIRM_ADDRESS_FOR_NEW_UAC, country);
    return confirm;
  }

  public SelectDeliveryMethodTextOrPost getSelectDeliveryMethodTextOrPost(final Country country) {
    selectDeliveryMethodTextOrPost = new SelectDeliveryMethodTextOrPost(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.SELECT_DELIVERY_METHOD_TEXT_OR_POST, country);
    return selectDeliveryMethodTextOrPost;
  }

  public SelectDeliveryMethodTextOrPost getSelectDeliveryMethodTextOrPost() {
    pageTracker.verifyCurrentPage(
        PageId.SELECT_DELIVERY_METHOD_TEXT_OR_POST, selectDeliveryMethodTextOrPost.getCountry());
    return selectDeliveryMethodTextOrPost;
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

  public PleaseSupplyYourAddress getPleaseSupplyYourAddress(final Country country) {
    pleaseSupplyYourAddress = new PleaseSupplyYourAddress(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.PLEASE_SUPPLY_YOUR_ADDRESS, country);
    return pleaseSupplyYourAddress;
  }

  public PleaseSupplyYourAddress getPleaseSupplyYourAddress() {
    pageTracker.verifyCurrentPage(
        PageId.PLEASE_SUPPLY_YOUR_ADDRESS, pleaseSupplyYourAddress.getCountry());
    return pleaseSupplyYourAddress;
  }

  public SelectYourAddress getSelectYourAddress(final Country country) {
    selectYourAddress = new SelectYourAddress(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.SELECT_YOUR_ADDRESS, country);
    return selectYourAddress;
  }

  public SelectYourAddress getSelectYourAddress() {
    pageTracker.verifyCurrentPage(PageId.SELECT_YOUR_ADDRESS, selectYourAddress.getCountry());
    return selectYourAddress;
  }

  public SentAccessCode getSentAccessCode(final Country country) {
    sentAccessCode = new SentAccessCode(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.SENT_ACCESS_CODE, country);
    return sentAccessCode;
  }

  public SentAccessCode getSentAccessCode() {
    pageTracker.verifyCurrentPage(PageId.SENT_ACCESS_CODE, sentAccessCode.getCountry());
    return sentAccessCode;
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

  public WhatIsYourMobile getWhatIsYourMobile(final Country country) {
    WhatIsYourMobile whatIsYourMobile = new WhatIsYourMobile(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.WHAT_IS_YOUR_MOBILE, country);
    return whatIsYourMobile;
  }

  public WhatIsYourAddress getWhatIsYourAddress(final Country country) {
    whatIsYourAddress = new WhatIsYourAddress(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.WHAT_IS_YOUR_ADDRESS, country);
    return whatIsYourAddress;
  }

  public WhatIsYourAddress getWhatIsYourAddress() {
    pageTracker.verifyCurrentPage(PageId.WHAT_IS_YOUR_ADDRESS, whatIsYourAddress.getCountry());
    return whatIsYourAddress;
  }

  public WhatIsYourName getWhatIsYourName(final Country country) {
    WhatIsYourName whatIsYourName = new WhatIsYourName(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.WHAT_IS_YOUR_NAME, country);
    return whatIsYourName;
  }

  public NewHouseholdAccessCode getNewHouseholdAccessCode(final Country country) {
    NewHouseholdAccessCode newHouseholdAccessCode = new NewHouseholdAccessCode(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.NEW_HOUSEHOLD_ACCESS_CODE, country);
    return newHouseholdAccessCode;
  }

  public RegisterYourAddress getRegisterYourAddress(final Country country) {
    RegisterYourAddress registerYourAddress = new RegisterYourAddress(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.REGISTER_YOUR_ADDRESS, country);
    return registerYourAddress;
  }

  public HouseholdInterstitial getHouseholdInterstitial(final Country country) {
    HouseholdInterstitial householdInterstitial = new HouseholdInterstitial(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.HOUSEHOLD_INTERSTITIAL, country);
    return householdInterstitial;
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

  public SISPage getSisPage(final Country country) {
    sisPage = new SISPage(webDriver, country);
    pageTracker.verifyCurrentPage(PageId.HOW_TO_TAKE_PART_SIS, country);
    return sisPage;
  }

  public SISPage getSisPage() {
    pageTracker.verifyCurrentPage(PageId.HOW_TO_TAKE_PART_SIS, sisPage.getCountry());
    return sisPage;
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
