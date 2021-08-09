package uk.gov.ons.ctp.integration.rhcucumber.steps;

import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.ons.ctp.common.cloud.FirestoreDataStore;
import uk.gov.ons.ctp.common.cloud.TestCloudDataStore;
import uk.gov.ons.ctp.common.util.WebDriverFactory;
import uk.gov.ons.ctp.integration.rhcucumber.repository.RespondentDataRepository;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Pages;

@CucumberContextConfiguration
@EnableConfigurationProperties
@SpringBootTest(
    classes = {
      RhStepsContext.class,
      WebDriverFactory.class,
      RespondentDataRepository.class,
      TestCloudDataStore.class,
      FirestoreDataStore.class,
      Pages.class,
      FirestoreDataStore.class,
      RateLimiterMock.class
    })
public class RhSpringRunner {
  @Before(order = 0)
  public void init() {}
}
