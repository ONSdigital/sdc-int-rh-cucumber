package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import java.util.ResourceBundle;

import lombok.Getter;

public class Translations {
  @Getter
  private Country country;
  private ResourceBundle bundle;
  
  public enum KEYS {
    START_PAGE_URL_SUFFIX,
    CONFIRM_ADDRESS_CONFIRMATION_TEXT,
    CONFIRM_ADDRESS_FOR_NEW_UAC_EXPECTED_CONFIRM_TEXT,
    CONFIRM_ADDRESS_FOR_NEW_UAC_EXPECTED_ADDRESS,
    HOUSEHOLD_INTERSTITIAL_EXPECTED_TEXT, 
    IS_THIS_MOBILE_NUM_CORRECT_EXPECTED_TEXT, 
    PLEASE_SUPPLY_YOUR_ADDRESS_EXPECTED_TEXT, 
    PLEASE_SUPPLY_YOUR_ADDRESS_EXPECTED_SELECTION_TEXT, 
    REGISTER_YOUR_ADDRESS_EXPECTED_TITLE_TEXT, 
    REGISTER_YOUR_ADDRESS_EXPECTED_TEXT_WITH_PHONE_NUMBER,
    SELECT_DELIVERY_METHOD_TEXT_OR_POST_EXPECTED_DELIVERY_TEXT, 
    SELECT_YOUR_ADDRESS_EXPECTED_SELECTION_TEXT, 
    SELECT_YOUR_ADDRESS_EXPECTED_SELECTION_LIST_TEXT1, 
    SELECT_YOUR_ADDRESS_EXPECTED_SELECTION_LIST_TEXT2, 
    SELECT_YOUR_ADDRESS_EXPECTED_POSTCODE, 
    SENT_ACCESS_CODE_EXPECTED_TEXT, 
    SENT_ACCESS_CODE_EXPECTED_REQUEST_CODE_TEXT,
    WHAT_IS_YOUR_MOBILE_EXPECTED_TEXT,
    WHAT_IS_YOUR_MOBILE_EXPECTED_ERROR_TEXT
  }
  
  public Translations(Country country) {
    this.country = country;
    bundle = ResourceBundle.getBundle("translations", country.getLocale());
  }

  public String get(KEYS key) {
    String keyName = key.name();
    if (!bundle.containsKey(keyName)) { 
      throw new IllegalStateException("No translation value. Country: " + country + " Key:" + key);
    }

    return bundle.getString(keyName);
  }
}
