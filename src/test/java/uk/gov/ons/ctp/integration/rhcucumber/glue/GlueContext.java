package uk.gov.ons.ctp.integration.rhcucumber.glue;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import uk.gov.ons.ctp.common.event.model.CaseUpdate;
import uk.gov.ons.ctp.common.event.model.CollectionExercise;
import uk.gov.ons.ctp.common.event.model.Header;
import uk.gov.ons.ctp.common.event.model.NewCaseSampleSensitive;
import uk.gov.ons.ctp.common.event.model.SurveyLaunchEvent;
import uk.gov.ons.ctp.common.event.model.SurveyLaunchPayload;
import uk.gov.ons.ctp.common.event.model.SurveyLaunchResponse;
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
  UacAuthenticationPayload respondentAuthenticationPayload;
  SurveyLaunchEvent surveyLaunchedEvent;
  Header surveyLaunchedHeader;
  SurveyLaunchPayload surveyLaunchedPayload;
  SurveyLaunchResponse surveyLaunchedResponse;
  NewCaseSampleSensitive familyInformation;
  boolean eqExists;
}
