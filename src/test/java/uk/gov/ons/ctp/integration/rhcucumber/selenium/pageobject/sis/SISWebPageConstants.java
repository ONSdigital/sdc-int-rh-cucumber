package uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.sis;

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
public class SISWebPageConstants {
  static final String XPATH_LOGO = "//*[text() = 'Office for National Statistics logo']";

  static final String XPATH_HIGHLIGHTED_ERROR_NO1 =
      "//div[@class='ons-panel__body']/p[@class='ons-panel__error']";

  static final String XPATH_PAGE_CONTENT_TITLE = "//main//h1";
  static final String XPATH_CONTINUE_BUTTON = "//form//main//button[@type='submit']";

  static final String XPATH_MOBILE_PHONE_NUMBER = "//main//input[@id='telephone']";

  static final String XPATH_LINK_SIS2_SURVEY = "//main//h2[@class='ons-u-fs-m']/a";
  static final String XPATH_BUTTON_REGISTER_SIS2_SURVEY =
      "//main//nav//ul//li[@class='ons-content-pagination__item']/a";

  static final String XPATH_REGISTER_NOW_BUTTON = "//form//main//button[@type='input']";
  static final String XPATH_TEXTBOX_FIRSTNAME = "//main//input[@id='name_first_name']";
  static final String XPATH_TEXTBOX_MIDDLENAME = "//main//input[@id='name_middle_names']";
  static final String XPATH_TEXTBOX_LASTNAME = "//main//input[@id='name_last_name']";
  static final String XPATH_DISPLAYED_MOBILE_NUMBER = "//form//p[@class='js-mobile-no']";
  static final String XPATH_ACCEPT_BUTTON = "//form//main//button[@name='button-accept']";
  static final String XPATH_CHILD_SCHOOL_NAME = "//main//input[@id='school-name']";
  static final String XPATH_CHILD_DOB_DAY = "//main//input[@id='date-day']";
  static final String XPATH_CHILD_DOB_MONTH = "//main//input[@id='date-month']";
  static final String XPATH_CHILD_DOB_YEAR = "//main//input[@id='date-year']";
}
