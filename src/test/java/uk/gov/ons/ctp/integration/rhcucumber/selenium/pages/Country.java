package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import java.util.Locale;

import lombok.Getter;

@Getter
public enum Country {
  ENG("en"),
  WALES("cy");

  private Locale locale;

  private Country(String language) {
	this.locale = new Locale(language);
  }
}
