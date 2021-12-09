package uk.gov.ons.ctp.integration.rhcucumber.data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import uk.gov.ons.ctp.common.event.model.CaseUpdate;
import uk.gov.ons.ctp.common.event.model.CaseUpdateSample;
import uk.gov.ons.ctp.common.event.model.CaseUpdateSampleSensitive;
import uk.gov.ons.ctp.common.event.model.CollectionExercise;
import uk.gov.ons.ctp.common.event.model.CollectionExerciseMetadata;
import uk.gov.ons.ctp.common.event.model.NewCaseSampleSensitive;
import uk.gov.ons.ctp.common.event.model.SurveyUpdate;
import uk.gov.ons.ctp.common.event.model.UacUpdate;
import uk.gov.ons.ctp.common.event.model.WaveMetadata;

public class ExampleData {
  public static final String DEFAULT_CASE_ID = "c45de4dc-3c3b-11e9-b210-d663bd873d13";
  public static final String VALID_MOBILE_NO = "07700 900345";
  public static final String INVALID_MOBILE_NO = "1234567";
  public static final String SURVEY_URL =
      "https://raw.githubusercontent.com/ONSdigital/eq-questionnaire-runner/"
          + "social-demo/test_schemas/en/zzz_9999.json";

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

    sample.setGor9d("E12000009");
    sample.setLaCode("EX");
    sample.setUprnLatitude("50.72116");
    sample.setUprnLongitude("-3.53363");

    sample.setQuestionnaire("12345");
    sample.setSampleUnitRef("REF-4321");
    sample.setCohort("CC3");
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

    sample.setGor9d("W99999999");
    sample.setLaCode("BA");
    sample.setUprnLatitude("53.22896");
    sample.setUprnLongitude("-4.12912");

    sample.setQuestionnaire("12345");
    sample.setSampleUnitRef("REF-4321");
    sample.setCohort("CC3");
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
    cc.setRefusalReceived("HARD_REFUSAL");
    cc.setCollectionExerciseId("4a6c6e0a-6384-4da8-8c3c-7c56a801f792");
    cc.setInvalid(false);
    cc.setSample(sample);
    cc.setSampleSensitive(sampleSensitive);
    cc.setSurveyId("4a6c6e0a-6384-4da8-8c3c-7c56a801f792");
    cc.setCaseRef("10000000017");
    cc.setCreatedAt(Date.from(Instant.parse("2021-01-01T01:02:03.456Z")));
    cc.setLastUpdatedAt(Date.from(Instant.parse("2021-10-10T00:00:00.000Z")));
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

  public static UacUpdate createUac(String uacHash, String caseId) {
    UacUpdate uac = new UacUpdate();
    uac.setCaseId(caseId);
    uac.setSurveyId("4a6c6e0a-6384-4da8-8c3c-7c56a801f792");
    uac.setCollectionExerciseId("4a6c6e0a-6384-4da8-8c3c-7c56a801f792");
    uac.setCollectionInstrumentUrl(SURVEY_URL);
    uac.setActive(true);
    uac.setUacHash(uacHash);
    uac.setQid("3110000009");
    uac.setReceiptReceived(false);
    uac.setMetadata(new WaveMetadata(94));
    uac.setEqLaunched(false);

    return uac;
  }

  public static SurveyUpdate createSurveyUpdate() {
    SurveyUpdate surveyUpdate = new SurveyUpdate();
    surveyUpdate.setSurveyId("4a6c6e0a-6384-4da8-8c3c-7c56a801f792");
    surveyUpdate.setName("LMS");
    surveyUpdate.setSampleDefinitionUrl("test/social.json");
    surveyUpdate.setSampleDefinition(
        "[\n"
            + "      {\n"
            + "        \"columnName\": \"addressLine1\",\n"
            + "        \"rules\": [\n"
            + "          {\n"
            + "            \"className\": \"uk.gov.ons.ssdc.common.validation.MandatoryRule\"\n"
            + "          },\n"
            + "          {\n"
            + "            \"className\": \"uk.gov.ons.ssdc.common.validation.LengthRule\",\n"
            + "            \"maxLength\": 60\n"
            + "          }\n"
            + "        ]\n"
            + "      }]");
    Map<String, Object> meta = new HashMap<>();
    meta.put("ex_e4", true);
    surveyUpdate.setMetadata(meta);
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
    newCaseSampleSensitive.setChildDob(LocalDate.parse("2010-12-31"));
    newCaseSampleSensitive.setEmailAddress("Jane.M.Bloggs@email.com");
    newCaseSampleSensitive.setMobileNumber("07387654321");
    return newCaseSampleSensitive;
  }
}
