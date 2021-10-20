package uk.gov.ons.ctp.integration.rhcucumber.repository;

import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.ons.ctp.common.cloud.TestCloudDataStore;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.common.event.model.CaseUpdate;
import uk.gov.ons.ctp.common.event.model.UacUpdate;

@Service
public class RespondentDataRepository {

  private TestCloudDataStore cloudDataStore;

  @Value("${GOOGLE_CLOUD_PROJECT}")
  private String gcpProject;

  @Value("${cloud-storage.case-schema-name}")
  private String caseSchemaName;

  @Value("${cloud-storage.uac-schema-name}")
  private String uacSchemaName;

  String caseSchema;
  private String uacSchema;

  private static final String[] SEARCH_BY_UPRN_PATH = new String[] {"address", "uprn"};

  @PostConstruct
  public void init() {
    caseSchema = gcpProject + "-" + caseSchemaName.toLowerCase();
    uacSchema = gcpProject + "-" + uacSchemaName.toLowerCase();
  }

  @Autowired
  public RespondentDataRepository(TestCloudDataStore cloudDataStore) {
    this.cloudDataStore = cloudDataStore;
  }

  public List<CaseUpdate> readLatestCaseUpdateByUprn(final String uprn) throws CTPException {
    return cloudDataStore.search(CaseUpdate.class, caseSchema, SEARCH_BY_UPRN_PATH, uprn);
  }

  public void deleteCasesbyCaseId(final String caseID) throws CTPException {
    cloudDataStore.deleteObject(caseSchema, caseID);
  }

  public void deleteCollections() throws CTPException {
    cloudDataStore.deleteCollection(caseSchema);
    cloudDataStore.deleteCollection(uacSchema);
  }

  public Optional<UacUpdate> readUAC(final String universalAccessCodeHash) throws CTPException {
    return cloudDataStore.retrieveObject(UacUpdate.class, uacSchema, universalAccessCodeHash);
  }

  public boolean waitForObject(String collection, String key, long timeoutMillis)
      throws CTPException {
    return cloudDataStore.waitForObject(collection, key, timeoutMillis);
  }
}
