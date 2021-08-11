package uk.gov.ons.ctp.integration.rhcucumber.data;

import java.util.Date;
import uk.gov.ons.ctp.common.event.model.Address;
import uk.gov.ons.ctp.common.event.model.CollectionCase;
import uk.gov.ons.ctp.common.event.model.Contact;
import uk.gov.ons.ctp.common.event.model.UAC;

public class ExampleData {
  public static final String DEFAULT_CASE_ID = "c45de4dc-3c3b-11e9-b210-d663bd873d13";

  // --- model fixtures below ...

  public static Address createNimrodAddress() {
    Address address = new Address();
    address.setAddressLine1("England House");
    address.setAddressLine2("England Street");
    address.setAddressLine3("Smithfield");
    address.setTownName("Exeter");
    address.setPostcode("EX1 2TD");
    address.setRegion("E");
    address.setLatitude("51.4934");
    address.setLongitude("0.0098");
    address.setUprn("10023122451");
    address.setAddressType("HI");
    address.setEstabType("E1");
    return address;
  }

  public static Address createNimrodAddressWales() {
    Address address = new Address();
    address.setAddressLine1("Wales House");
    address.setAddressLine2("Wales Street");
    address.setAddressLine3("Smithfield");
    address.setTownName("Bangor");
    address.setPostcode("LL1 2TD");
    address.setRegion("W");
    address.setLatitude("51.4934");
    address.setLongitude("0.0098");
    address.setUprn("10023122451");
    address.setAddressType("HI");
    address.setEstabType("E1");
    return address;
  }

  public static Contact createLadySallyContact() {
    Contact contact = new Contact();
    contact.setTitle("Lady");
    contact.setForename("Sally");
    contact.setSurname("Scatterbrain");
    contact.setTelNo("07700900345");
    return contact;
  }

  public static CollectionCase createCollectionCase(Address addr, Contact contact, String id) {
    CollectionCase cc = new CollectionCase();
    cc.setAddress(addr);
    cc.setContact(contact);
    cc.setId(id);
    cc.setCaseRef("ella3");
    cc.setSurvey("CENSUS");
    cc.setCollectionExerciseId("4a6c6e0a-6384-4da8-8c3c-7c56a801f792");
    cc.setActionableFrom("2018-08-12T20:17:46.384Z");
    cc.setCaseType("HH");
    cc.setCreatedDateTime(new Date());
    cc.setAddressInvalid(false);
    return cc;
  }

  public static CollectionCase createCollectionCase(String id) {
    Address address = createNimrodAddress();
    Contact contact = createLadySallyContact();
    return createCollectionCase(address, contact, id);
  }

  public static CollectionCase createWelshCollectionCase(String id) {
    Address address = createNimrodAddressWales();
    Contact contact = createLadySallyContact();
    return createCollectionCase(address, contact, id);
  }

  public static UAC createUac(String uacHash, String caseId) {
    UAC uac = new UAC();
    uac.setUacHash(uacHash);
    uac.setActive("true");
    uac.setQuestionnaireId("3110000009");
    uac.setCaseId(caseId);
    return uac;
  }
}
