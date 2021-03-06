package uk.gov.ons.ctp.integration.rhcucumber.glue;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.ons.ctp.common.cloud.FirestoreDataStore;
import uk.gov.ons.ctp.common.cloud.FirestoreProviderImpl;
import uk.gov.ons.ctp.common.firestore.TestCloudDataStore;
import uk.gov.ons.ctp.common.util.WebDriverFactory;
import uk.gov.ons.ctp.integration.rhcucumber.repository.RespondentDataRepository;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Pages;

@CucumberContextConfiguration
@EnableConfigurationProperties
@SpringBootTest(
    classes = {
      GlueContext.class,
      WebDriverFactory.class,
      RespondentDataRepository.class,
      TestCloudDataStore.class,
      FirestoreDataStore.class,
      FirestoreProviderImpl.class,
      Pages.class,
      FirestoreDataStore.class
    })
public class CucumberSpringConfiguration {}
