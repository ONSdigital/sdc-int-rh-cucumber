package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject;

/**
 * This file holds xpath expressions which are used to find common items in the RHUI web pages. An
 * introductory reference for xpath is:
 * https://blog.scrapinghub.com/2016/10/27/an-introduction-to-xpath-with-examples
 *
 * <p>The following xpath expressions aim to uniquely identify page elements yet retain enough
 * flexibility so that they don't require updating for small page changes, such as say adding in an
 * extra div layer or moving some unrelated content to an earlier part of the page.
 *
 * <p>The expressions also try to be readable in the own right. For example the following fragile
 * xpath identifies the full path to an element
 * '/html/body/div/div/form/div[2]/div/div/main/p[4]/a', can be replaced with the more readable
 * expression: '//form//main//a[@href='/webchat']'
 */
public class WebPageConstants {
  public static final String XPATH_LOGO = "//*[text() = 'Office for National Statistics logo']";

  static final String XPATH_PARAGRAPH_ADDRESS = "//main//h1/following-sibling::p";
  public static final String XPATH_HIGHLIGHTED_ERROR_NO1 =
      "//div[@class='panel__body']/p[@class='panel__error']";
  static final String XPATH_LINK_REQUEST_A_NEW_CODE =
      "//main//a[text()='request a new access code']";
  static final String XPATH_LINK_WALES_REQUEST_A_NEW_CODE = XPATH_LINK_REQUEST_A_NEW_CODE;
  // "//main//a[text()='ofyn am god mynediad newydd']";     // TODO: Welsh translation when RHUI
  // ready
  static final String XPATH_LINK_WALES_REQUEST_A_NEW_CODE_SENT =
      "//main//a[text()='ofyn am god mynediad newydd']";
  static final String XPATH_TEXTBOX_ADDRESS = "//main//input[@id='address-autosuggest']";

  // For the Start page
  static final String XPATH_BUTTON_ACCESS_SURVEY = "//form//button[@name='action[save_continue]']";
  public static final String XPATH_PAGE_CONTENT_TITLE = "//main//h1";
  static final String XPATH_TEXTBOX_ENTER_UAC = "//form//main//input[@id='uac']";
  public static final String XPATH_CONTINUE_BUTTON = "//form//main//button[@type='submit']";
  static final String XPATH_BUTTON_REQUEST_A_NEW_CODE = "//main//a[@role='button']";
  static final String XPATH_LINK_CHANGE_LANGUAGE = "//header//li[@class='language-links__item']/a";

  // For the Address Confirmation page
  static final String XPATH_EM_ADDRESS = "//form//p[@class='rh-address-display']";
  static final String XPATH_RADIO_ADDRESS_YES = "//fieldset//input[@id='yes']";
  static final String XPATH_RADIO_ADDRESS_NO = "//fieldset//input[@id='no']";

  // For the Select Delivery Method page
  static final String XPATH_RADIO_METHOD_TEXT = "//fieldset//input[@id='sms']";
  static final String XPATH_RADIO_METHOD_POST = "//fieldset//input[@id='post']";

  // For the What is your Mobile page
  static final String XPATH_TEXTBOX_MOBILE_PHONE_NUMBER = "//main//input[@id='telephone']";

  // For the Mobile Number Correct page
  static final String XPATH_RADIO_MOBILE_YES = "//fieldset//input[@id='yes']";
  static final String XPATH_RADIO_MOBILE_NO = "//fieldset//input[@id='no']";

  // For the Select Address page
  static final String XPATH_PARAGRAPH_ADDRESS_COUNT = "//form//h1/following-sibling::*//p";
  static final String XPATH_RADIO_FIRST_ADDRESS = "(//form//fieldset//input)[1]";
  static final String XPATH_RADIO_SECOND_ADDRESS = "(//form//fieldset//input)[2]";
  static final String XPATH_RADIO_CANNOT_FIND_ADDRESS = "//fieldset//input[@id='xxxx']";

  // For the What is your name Page
  static final String XPATH_TEXTBOX_FIRSTNAME = "//main//input[@id='name_first_name']";
  static final String XPATH_TEXTBOX_LASTNAME = "//main//input[@id='name_last_name']";

  // For New Household Access Code Page
  static final String XPATH_RADIO_POST_ACCESS_CODE_YES = "//fieldset//input[@id='yes']";
  static final String XPATH_RADIO_POST_ACCESS_CODE_NO = "//fieldset//input[@id='no']";

  // For Register Your Address Page
  static final String XPATH_P_WITH_PHONE_NUMBER = "//p[2]";
}
