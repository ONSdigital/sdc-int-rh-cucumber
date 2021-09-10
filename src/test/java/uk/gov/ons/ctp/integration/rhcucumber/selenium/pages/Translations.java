package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import java.util.ResourceBundle;

import lombok.Getter;

public class Translations {
  @Getter
  private Country country;
  private ResourceBundle bundle;
  
  public enum IDS {
    START_PAGE_CLASS_PREFIX,
    START_PAGE_URL_SUFFIX,
    CONFIRM_ADDRESS_CLASS_PREFIX,
    CONFIRM_ADDRESS_CONFIRMATION_TEXT
  }
  
  public Translations(Country country) {
    this.country = country;
    bundle = ResourceBundle.getBundle("translations", country.getLocale());
  }

  public String get(IDS key) {
    String keyName = key.name();
    if (!bundle.containsKey(keyName)) { 
      throw new IllegalStateException("No translation value. Country: " + country + " Key:" + key);
    }

    return bundle.getString(keyName);
  }
}
