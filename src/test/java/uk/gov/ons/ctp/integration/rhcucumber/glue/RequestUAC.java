package uk.gov.ons.ctp.integration.rhcucumber.glue;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import uk.gov.ons.ctp.integration.rhcucumber.GlueConst;

@RunWith(Cucumber.class)
@CucumberOptions(
    strict = GlueConst.STRICTNESS,
    plugin = {"pretty", "html:target/cucumber/request-uac"},
    features = {"src/test/resources/features/Request-UAC.feature"},
    tags = GlueConst.COMMON_TAGS,
    glue = {"uk.gov.ons.ctp.integration.rhcucumber.steps"})
public class RequestUAC {}
