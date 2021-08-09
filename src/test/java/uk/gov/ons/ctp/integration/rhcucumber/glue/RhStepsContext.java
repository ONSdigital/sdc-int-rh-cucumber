package uk.gov.ons.ctp.integration.rhcucumber.glue;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import uk.gov.ons.ctp.common.event.model.AddressModification;
import uk.gov.ons.ctp.common.event.model.AddressModifiedPayload;
import uk.gov.ons.ctp.common.event.model.CollectionCase;
import uk.gov.ons.ctp.common.event.model.Header;
import uk.gov.ons.ctp.common.event.model.QuestionnaireLinkedDetails;
import uk.gov.ons.ctp.common.event.model.QuestionnaireLinkedEvent;
import uk.gov.ons.ctp.common.event.model.QuestionnaireLinkedPayload;
import uk.gov.ons.ctp.common.event.model.RespondentAuthenticatedEvent;
import uk.gov.ons.ctp.common.event.model.RespondentAuthenticatedPayload;
import uk.gov.ons.ctp.common.event.model.RespondentAuthenticatedResponse;
import uk.gov.ons.ctp.common.event.model.SurveyLaunchedEvent;
import uk.gov.ons.ctp.common.event.model.SurveyLaunchedPayload;
import uk.gov.ons.ctp.common.event.model.SurveyLaunchedResponse;
import uk.gov.ons.ctp.common.event.model.UAC;

@Data
@NoArgsConstructor
@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class RhStepsContext {
  String caseCollection;
  String caseKey;
  String uacCollection;
  String uac;
  String uacKey;
  long timeout;
  Boolean caseFoundInFirestore;
  Boolean uacFoundInFirestore;
  String errorMessageContainingCallToEQ;
  CollectionCase caseCreatedPayload;
  UAC uacPayload;
  String queueName;
  String fulfilmentRequestedCode;
  RespondentAuthenticatedEvent respondentAuthenticatedEvent;
  Header respondentAuthenticatedHeader;
  RespondentAuthenticatedPayload respondentAuthenticatedPayload;
  RespondentAuthenticatedResponse respondentAuthenticatedResponse;
  SurveyLaunchedEvent surveyLaunchedEvent;
  Header surveyLaunchedHeader;
  SurveyLaunchedPayload surveyLaunchedPayload;
  SurveyLaunchedResponse surveyLaunchedResponse;
  QuestionnaireLinkedEvent questionnaireLinkedEvent;
  Header questionnaireLinkedHeader;
  QuestionnaireLinkedPayload questionnaireLinkedPayload;
  QuestionnaireLinkedDetails questionnaireLinkedDetails;
  AddressModifiedPayload addressModifiedPayload;
  AddressModification addressModification;
  String postcode;
  String address;
  String mainWindow;
  boolean eqExists;
}
