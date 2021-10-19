package uk.gov.ons.ctp.integration.rhcucumber.data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import uk.gov.ons.ctp.common.event.model.CaseUpdate;
import uk.gov.ons.ctp.common.event.model.CaseUpdateSample;
import uk.gov.ons.ctp.common.event.model.CaseUpdateSampleSensitive;
import uk.gov.ons.ctp.common.event.model.CollectionExercise;
import uk.gov.ons.ctp.common.event.model.CollectionExerciseMetadata;
import uk.gov.ons.ctp.common.event.model.NewCaseSampleSensitive;
import uk.gov.ons.ctp.common.event.model.SurveyUpdate;
import uk.gov.ons.ctp.common.event.model.UAC;

public class ExampleData {
  public static final String DEFAULT_CASE_ID = "c45de4dc-3c3b-11e9-b210-d663bd873d13";
  public static final String VALID_MOBILE_NO = "07700 900345";
  public static final String INVALID_MOBILE_NO = "1234567";

  // --- model fixtures below ...

  public static CaseUpdateSample createSample() {
    CaseUpdateSample sample = new CaseUpdateSample();
    sample.setAddressLine1("England House");
    sample.setAddressLine2("England Street");
    sample.setAddressLine3("Smithfield");
    sample.setTownName("Exeter");
    sample.setPostcode("EX1 2TD");
    sample.setRegion("E");
    sample.setUprn("10023122451");
    return sample;
  }

  public static CaseUpdateSample createSampleWales() {
    CaseUpdateSample sample = new CaseUpdateSample();
    sample.setAddressLine1("Wales House");
    sample.setAddressLine2("Wales Street");
    sample.setAddressLine3("Smithfield");
    sample.setTownName("Bangor");
    sample.setPostcode("LL1 2TD");
    sample.setRegion("W");
    sample.setUprn("10023122451");
    return sample;
  }

  public static CaseUpdateSampleSensitive createSampleSensitive() {
    CaseUpdateSampleSensitive sampleSensitive = new CaseUpdateSampleSensitive();
    sampleSensitive.setPhoneNumber(VALID_MOBILE_NO);
    return sampleSensitive;
  }

  public static CollectionExerciseMetadata createCollectionExerciseMetaData() {
    CollectionExerciseMetadata collectionExerciseMetadata = new CollectionExerciseMetadata();
    collectionExerciseMetadata.setCohorts(1);
    collectionExerciseMetadata.setCohortSchedule(1);
    collectionExerciseMetadata.setNumberOfWaves(1);
    collectionExerciseMetadata.setWaveLength(1);
    return collectionExerciseMetadata;
  }

  public static CaseUpdate createCaseUpdate(
      CaseUpdateSample sample, CaseUpdateSampleSensitive sampleSensitive, String id) {
    CaseUpdate cc = new CaseUpdate();
    cc.setCaseId(id);
    cc.setRefusalReceived("CENSUS");
    cc.setCollectionExerciseId("4a6c6e0a-6384-4da8-8c3c-7c56a801f792");
    cc.setInvalid(false);
    cc.setSample(sample);
    cc.setSampleSensitive(sampleSensitive);
    cc.setSurveyId("4a6c6e0a-6384-4da8-8c3c-7c56a801f792");
    return cc;
  }

  public static CaseUpdate createCaseUpdate(String id) {
    CaseUpdateSample sample = createSample();
    CaseUpdateSampleSensitive sampleSensitive = createSampleSensitive();
    return createCaseUpdate(sample, sampleSensitive, id);
  }

  public static CaseUpdate createWelshCaseUpdate(String id) {
    CaseUpdateSample sample = createSampleWales();
    CaseUpdateSampleSensitive sampleSensitive = createSampleSensitive();
    return createCaseUpdate(sample, sampleSensitive, id);
  }

  public static UAC createUac(String uacHash, String caseId) {
    UAC uac = new UAC();
    uac.setUacHash(uacHash);
    uac.setActive("true");
    uac.setQuestionnaireId("3110000009");
    uac.setCaseId(caseId);
    return uac;
  }

  public static SurveyUpdate createSurveyUpdate() {
    SurveyUpdate surveyUpdate = new SurveyUpdate();
    surveyUpdate.setSurveyId("4a6c6e0a-6384-4da8-8c3c-7c56a801f792");
    surveyUpdate.setName("LMS");
    return surveyUpdate;
  }

  public static CollectionExercise createCollectionExercise() {
    CollectionExercise collectionExercise = new CollectionExercise();
    collectionExercise.setSurveyId("4a6c6e0a-6384-4da8-8c3c-7c56a801f792");
    collectionExercise.setCollectionExerciseId("4a6c6e0a-6384-4da8-8c3c-7c56a801f792");
    collectionExercise.setName("Dummy");
    collectionExercise.setStartDate(Date.from(Instant.parse("2021-09-17T23:59:59.999Z")));
    collectionExercise.setEndDate(Date.from(Instant.parse("2021-09-27T23:59:59.999Z")));
    collectionExercise.setReference("MVP012021");
    collectionExercise.setMetadata(createCollectionExerciseMetaData());
    return collectionExercise;
  }

  public static NewCaseSampleSensitive constructFamilyInformation() {
    NewCaseSampleSensitive newCaseSampleSensitive = new NewCaseSampleSensitive();
    newCaseSampleSensitive.setFirstName("Mike");
    newCaseSampleSensitive.setLastName("Bloggs");
    newCaseSampleSensitive.setChildFirstName("Jo");
    newCaseSampleSensitive.setChildMiddleNames("Mary");
    newCaseSampleSensitive.setChildLastName("Bloggs");
    newCaseSampleSensitive.setParentEmailAddress("Mike.J.Bloggs@email.com");
    newCaseSampleSensitive.setParentMobileNumber("07312345678");
    newCaseSampleSensitive.setChildDob(LocalDate.parse("2010-12-31"));
    newCaseSampleSensitive.setChildEmailAddress("Jane.M.Bloggs@email.com");
    newCaseSampleSensitive.setChildMobileNumber("07387654321");
    return newCaseSampleSensitive;
  }
}
