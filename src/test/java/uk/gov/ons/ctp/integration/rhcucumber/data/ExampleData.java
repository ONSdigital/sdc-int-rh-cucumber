package uk.gov.ons.ctp.integration.rhcucumber.data;

import uk.gov.ons.ctp.common.event.model.CaseUpdate;
import uk.gov.ons.ctp.common.event.model.Sample;
import uk.gov.ons.ctp.common.event.model.SampleSensitive;
import uk.gov.ons.ctp.common.event.model.SurveyUpdate;
import uk.gov.ons.ctp.common.event.model.UAC;

public class ExampleData {
  public static final String DEFAULT_CASE_ID = "c45de4dc-3c3b-11e9-b210-d663bd873d13";
  public static final String VALID_MOBILE_NO = "07700 900345";
  public static final String INVALID_MOBILE_NO = "1234567";

  // --- model fixtures below ...

  public static Sample createSample() {
    Sample sample = new Sample();
    sample.setAddressLine1("England House");
    sample.setAddressLine2("England Street");
    sample.setAddressLine3("Smithfield");
    sample.setTownName("Exeter");
    sample.setPostcode("EX1 2TD");
    sample.setRegion("E");
    sample.setUprn("10023122451");
    return sample;
  }

  public static Sample createSampleWales() {
    Sample sample = new Sample();
    sample.setAddressLine1("Wales House");
    sample.setAddressLine2("Wales Street");
    sample.setAddressLine3("Smithfield");
    sample.setTownName("Bangor");
    sample.setPostcode("LL1 2TD");
    sample.setRegion("W");
    sample.setUprn("10023122451");
    return sample;
  }

  public static SampleSensitive createSampleSensitive() {
    SampleSensitive sampleSensitive = new SampleSensitive();
    sampleSensitive.setPhoneNumber(VALID_MOBILE_NO);
    return sampleSensitive;
  }

  public static CaseUpdate createCollectionCase(Sample sample, SampleSensitive sampleSensitive, String id) {
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

  public static CaseUpdate createCollectionCase(String id) {
    Sample sample = createSample();
    SampleSensitive sampleSensitive = createSampleSensitive();
    return createCollectionCase(sample, sampleSensitive, id);
  }

  public static CaseUpdate createWelshCollectionCase(String id) {
    Sample sample = createSampleWales();
    SampleSensitive sampleSensitive = createSampleSensitive();
    return createCollectionCase(sample, sampleSensitive, id);
  }

  public static UAC createUac(String uacHash, String caseId) {
    UAC uac = new UAC();
    uac.setUacHash(uacHash);
    uac.setActive("true");
    uac.setQuestionnaireId("3110000009");
    uac.setCaseId(caseId);
    return uac;
  }

  public static SurveyUpdate createSuveyUpdate() {
    SurveyUpdate surveyUpdate = new SurveyUpdate();
    surveyUpdate.setSurveyId("4a6c6e0a-6384-4da8-8c3c-7c56a801f792");
    surveyUpdate.setName("LMS");
    return surveyUpdate;
  }
}
