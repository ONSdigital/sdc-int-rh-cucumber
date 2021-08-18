package uk.gov.ons.ctp.integration.rhcucumber.glue;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import uk.gov.ons.ctp.common.event.model.CollectionCase;
import uk.gov.ons.ctp.common.event.model.Header;
import uk.gov.ons.ctp.common.event.model.RespondentAuthenticatedPayload;
import uk.gov.ons.ctp.common.event.model.SurveyLaunchedEvent;
import uk.gov.ons.ctp.common.event.model.SurveyLaunchedPayload;
import uk.gov.ons.ctp.common.event.model.SurveyLaunchedResponse;
import uk.gov.ons.ctp.common.event.model.UAC;
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
  CollectionCase caseCreatedPayload;
  UAC uacPayload;
  String fulfilmentRequestedCode;
  Header respondentAuthenticatedHeader;
  RespondentAuthenticatedPayload respondentAuthenticatedPayload;
  SurveyLaunchedEvent surveyLaunchedEvent;
  Header surveyLaunchedHeader;
  SurveyLaunchedPayload surveyLaunchedPayload;
  SurveyLaunchedResponse surveyLaunchedResponse;
  boolean eqExists;
}
