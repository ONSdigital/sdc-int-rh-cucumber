package uk.gov.ons.ctp.integration.rhcucumber.glue;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.ons.ctp.common.cloud.FirestoreDataStore;
import uk.gov.ons.ctp.common.cloud.TestCloudDataStore;
import uk.gov.ons.ctp.common.util.SimpleWebDriverFactory;
import uk.gov.ons.ctp.integration.rhcucumber.repository.RespondentDataRepository;
import uk.gov.ons.ctp.integration.rhcucumber.selenium.pages.Pages;

@CucumberContextConfiguration
@EnableConfigurationProperties
@SpringBootTest(
    classes = {
      GlueContext.class,
      SimpleWebDriverFactory.class,
      RespondentDataRepository.class,
      TestCloudDataStore.class,
      FirestoreDataStore.class,
      Pages.class,
      FirestoreDataStore.class
    })
public class CucumberSpringConfiguration {}
