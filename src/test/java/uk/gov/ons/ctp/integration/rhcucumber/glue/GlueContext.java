package uk.gov.ons.ctp.integration.rhcucumber.glue;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import uk.gov.ons.ctp.common.event.model.CaseUpdate;
import uk.gov.ons.ctp.common.event.model.CollectionExercise;
import uk.gov.ons.ctp.common.event.model.EqLaunch;
import uk.gov.ons.ctp.common.event.model.EqLaunchEvent;
import uk.gov.ons.ctp.common.event.model.EqLaunchPayload;
import uk.gov.ons.ctp.common.event.model.Header;
import uk.gov.ons.ctp.common.event.model.NewCaseSampleSensitive;
import uk.gov.ons.ctp.common.event.model.SurveyUpdate;
import uk.gov.ons.ctp.common.event.model.UacAuthenticationPayload;
import uk.gov.ons.ctp.common.event.model.UacUpdate;
import uk.gov.ons.ctp.integration.rhcucumber.data.ExampleData;

@Data
@NoArgsConstructor
@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class GlueContext {
  String caseCollection = "case";
  String caseKey = ExampleData.DEFAULT_CASE_ID;
  String uacCollection = "uac";
  String uac;
  String uacKey;
  String errorMessageContainingCallToEQ;
  CaseUpdate caseCreatedPayload;
  SurveyUpdate surveyUpdatePayload;
  CollectionExercise collectionExercise;
  UacUpdate uacPayload;
  String fulfilmentRequestedCode;
  Header respondentAuthenticationHeader;
  UacAuthenticationPayload uacAuthenticationPayload;
  EqLaunchEvent eqLaunchedEvent;
  Header eqLaunchedHeader;
  EqLaunchPayload eqLaunchedPayload;
  EqLaunch eqLaunched;
  NewCaseSampleSensitive familyInformation;
  boolean eqExists;
}
