package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import java.util.Locale;
import java.util.ResourceBundle;

public class Translations {
  private Locale locale;
  private ResourceBundle bundle;
  
  public enum IDS {
    START_PAGE_CLASS_PREFIX,
    START_PAGE_URL_SUFFIX, 
    START_PAGE_CHANGE_LANGUAGE_LINK
  }
  
  public Translations(Country country) {
    System.out.println("Loading constants: " + country.getLocale());
    this.locale = country.getLocale();
    bundle = ResourceBundle.getBundle("translations", locale);
  }

  public String get(IDS key) {
    String keyName = key.name();
    if (!bundle.containsKey(keyName)) { 
      throw new IllegalStateException("No translation value. Key:" + key + " Locale: " + locale);
    }

    return bundle.getString(keyName);
  }
}
