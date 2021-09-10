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
