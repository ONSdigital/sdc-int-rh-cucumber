package uk.gov.ons.ctp.integration.rhcucumber.selenium.pages;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;

import lombok.Getter;

public class PageTracker {
  
  @Getter
  public enum PageId {
    START_PAGE("Enter your 16-character access code", "Rhowch eich cod mynediad, sy'n cynnwys 16 nod"),
    CONFIRM_ADDRESS("Is this the correct address?", "Ai dyma'r cyfeiriad cywir?"),
    CONFIRM_ADDRESS_FOR_NEW_UAC("TODO", "TODO"),
    WHAT_IS_YOUR_ADDRESS("What is your address?", "What is your address?"), // TODO: Wales translation, when RHUI ready
    SELECT_DELIVERY_METHOD_TEXT_OR_POST("TODO", "TODO"),
    IS_THIS_MOBILE_NUM_CORRECT("TODO", "TODO"),
    PLEASE_SUPPLY_YOUR_ADDRESS("TODO", "TODO"),
    SELECT_YOUR_ADDRESS("TODO", "TODO"),
    SENT_ACCESS_CODE("TODO", "TODO"),
    WHAT_IS_YOUR_MOBILE("TODO", "TODO"),
    WHAT_IS_YOUR_NAME("TODO", "TODO"),
    NEW_HOUSEHOLD_ACCESS_CODE("TODO", "TODO"),
    REGISTER_YOUR_ADDRESS("TODO", "TODO"),
    HOUSEHOLD_INTERSTITIAL("TODO", "TODO"),
    PAGE_NOT_FOUND("Page not found", "Heb ddod o hyd i'r dudalen");
    
    private String identiferEnglish;
    private String identiferWelsh;
    
    private PageId(String identiferEnglish, String identiferWelsh) {
      this.identiferEnglish = identiferEnglish;
      this.identiferWelsh = identiferWelsh;
    }

    String getIdentifierEnglish() {
      return identiferEnglish;
    }
    String getIdentifierWelsh() {
      return identiferWelsh;
    }
  }
  
  private WebDriver webDriver;
  private static int pageCaptureNum = 1;
  

  public PageTracker(WebDriver webDriver) {
    this.webDriver = webDriver;
  }
  
  /**
   * Verifies that we are on the expected page.
   * 
   * If 'dumpPageContent' property is set to 'true' then the page content is written to a temp file.
   * 
   * @param expectedPage is the Page that the ucumber test code thinks we should be on.
   * @param expectedCountry is the Country that the Cucumber test code is using.
   */
  public void verifyCurrentPage(PageId expectedPage, Country expectedCountry) {
    String pageContent = getPageContent();
    
    // Identify pages country
    Country actualCountry = null;
    if (pageContent.contains("Crown copyright")) {
      actualCountry = Country.ENG;
    } else if (pageContent.contains("Hawlfraint y Goron")) {
      actualCountry = Country.WALES;
    }
    
    // Identify current page based on its content
    PageId actualPage = null;
    for (PageId currPageId : PageId.values()) {
      if (actualCountry == Country.ENG && pageContent.contains(currPageId.getIdentiferEnglish())) {
        actualPage = currPageId;
        break;
      }
      if (actualCountry == Country.WALES && pageContent.contains(currPageId.getIdentiferWelsh())) {
        actualPage = currPageId;
        break;
      }
    }
    
    // Check if we are on the expected page
    String exceptionText = null;
    if (actualCountry == null) {
      exceptionText = "Failed to identify country of page. Expected page: " + expectedPage.name() + " Expected country: " + expectedCountry;
    } else if (actualPage == null) {
      String pageTitle = StringUtils.substringsBetween(pageContent, "<title>", "</title>")[0];
      exceptionText = "Failed to identify page. Page title: '" + pageTitle + "'";
    } else if (expectedPage != actualPage) {
      exceptionText = "On wrong page. Expected page: " + expectedPage.name() + " ActualPage: " + actualPage.name();
    } else if (expectedCountry != actualCountry) {
      exceptionText = "Incorrect language version of page. On page: " + expectedPage.name() + " ExpectedCountry: " + expectedCountry.name() 
         + " ActualCountry: " + actualCountry.name();
    }

    // Optionally dump page content to file
    String pageContentFile = null;
    String pageDumpingFlag = System.getProperty("dumpPageContent", "false");
    if (pageDumpingFlag.equalsIgnoreCase("true")) {
      pageContentFile = dumpPageContent(pageContent, expectedPage, expectedCountry);
    } 

    // Fail if something has gone wrong
    if (exceptionText != null) {
      if (pageContentFile == null) {
        throw new IllegalStateException(exceptionText);
      } else {
        throw new IllegalStateException(exceptionText + " PageContent: " + pageContentFile); 
      }
    }
  }
  
  private String getPageContent() {
    final Document doc = Jsoup.parse(webDriver.getPageSource());
    return doc.html();
  }
  
  private String dumpPageContent(String pageContent, PageId expectedPage, Country country) {
    try {
      String fileName = "/tmp/rh." + (pageCaptureNum++) + "." + expectedPage.name() + "." + country.name() + ".html";
      FileOutputStream outputStream = new FileOutputStream(fileName);
      outputStream.write(pageContent.getBytes()); 
      
      outputStream.close();
      return fileName;
    } catch (IOException e) {
      throw new IllegalStateException("Failed to dump page content", e);
    }
  }
}
