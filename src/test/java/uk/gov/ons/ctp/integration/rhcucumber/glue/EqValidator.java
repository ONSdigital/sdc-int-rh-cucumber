package uk.gov.ons.ctp.integration.rhcucumber.glue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import uk.gov.ons.ctp.integration.eqlaunch.crypto.JweDecryptor;
import uk.gov.ons.ctp.integration.eqlaunch.crypto.KeyStore;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.SocialQuestionnaire;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Country;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EqValidator {

  private static final List<String> HASH_KEYS_EXPECTED =
      Arrays.asList(
          "account_service_log_out_url",
          "account_service_url",
          "case_id",
          "channel",
          "collection_exercise_sid",
          "display_address",
          "eq_id",
          "exp",
          "form_type",
          "iat",
          "jti",
          "language_code",
          "period_id",
          "questionnaire_id",
          "region_code",
          "response_id",
          "ru_ref",
          "survey",
          "tx_id",
          "user_id");

  static void clickThoughToEq(WebDriver driver, GlueContext context) {
    if (context.errorMessageContainingCallToEQ == null) {
      try {
        new SocialQuestionnaire(driver, Country.ENG).clickSocialLogo();
        context.eqExists = true;
      } catch (TimeoutException e) {
        // tolerate no EQ deployment for testing
      }
    } else {
      String devTextToFind = "/session/%3Ftoken";
      String localTextToFind = "/session%3Ftoken";
      assertTrue(
          context.errorMessageContainingCallToEQ.contains(devTextToFind)
              || context.errorMessageContainingCallToEQ.contains(localTextToFind));
    }
  }

  public static void verifyTokenSuccessfullyDecrypted(
      GlueContext context, WebDriver driver, String keystore) throws Exception {
    String returnURL;
    String splitter;
    if (context.eqExists) {
      returnURL = driver.getCurrentUrl();
      splitter = "token=";
    } else {
      returnURL = context.errorMessageContainingCallToEQ;
      splitter = "token%3D";
    }

    if (!returnURL.contains(splitter)) {
      fail("Return URL must contain encrypted token - URL is: " + returnURL);
    }

    String hhEqToken = returnURL.split(splitter)[1].split("&")[0];
    final JweDecryptor decryptor = new JweDecryptor(new KeyStore(keystore));

    String decryptedEqToken = decryptor.decrypt(hhEqToken);

    @SuppressWarnings("unchecked")
    HashMap<String, String> result1 = new ObjectMapper().readValue(decryptedEqToken, HashMap.class);
    List<String> hashKeysFound = new ArrayList<>(result1.keySet());

    assertEquals(
        "Must have the correct number of hash keys",
        HASH_KEYS_EXPECTED.size(),
        hashKeysFound.size());

    List<String> sortedExpectedKeys =
        HASH_KEYS_EXPECTED.stream().sorted().collect(Collectors.toList());
    List<String> sortedFoundKeys = hashKeysFound.stream().sorted().collect(Collectors.toList());
    assertEquals(
        "Expected Keys and Found keys must match",
        sortedExpectedKeys.toString(),
        sortedFoundKeys.toString());

    assertEquals(
        "Must have the correct address",
        "England House, England Street",
        result1.get("display_address").trim());
    assertEquals("Must have the correct channel", "rh", result1.get("channel"));
    assertEquals("Must have the correct case type", "HH", result1.get("case_type"));
    assertEquals("Must have the correct eq id", "CENSUS", result1.get("eq_id").toUpperCase());
    assertEquals("Must have the correct UPRN value", "10023122451", result1.get("ru_ref"));
    assertEquals("Must have the correct language_code value", "en", result1.get("language_code"));
    assertEquals(
        "Must have the correct collection_exercise_sid value",
        "4a6c6e0a-6384-4da8-8c3c-7c56a801f792",
        result1.get("collection_exercise_sid"));
    assertEquals("Must have the correct survey value", "CENSUS", result1.get("survey"));
  }
}
