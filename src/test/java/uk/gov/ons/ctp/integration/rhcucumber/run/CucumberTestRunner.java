package uk.gov.ons.ctp.integration.rhcucumber.run;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import uk.gov.ons.ctp.integration.rhcucumber.GlueConst;

@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {"pretty", "html:target/cucumber/results.html"},
    features = {
      GlueConst.FEATURES_PATH + "Request-UAC.feature",
      GlueConst.FEATURES_PATH + "UAC-Authentication-Success.feature",
      GlueConst.FEATURES_PATH + "UAC-Authentication-Failure.feature"
    },
    tags = GlueConst.COMMON_TAGS,
    glue = {GlueConst.GLUE_PKG})
public class CucumberTestRunner {}
