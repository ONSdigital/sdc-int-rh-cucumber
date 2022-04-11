#Author: eleanor.cook@ons.gov.uk
#Keywords Summary : test UAC Authentication Success
#Feature:  UAC-Authentication-Success
#Scenario Outline: [CR-T163] The Happy Path - Access the Survey Questionnaire
#Scenario: [CR-T167] The Happy Path - Access the Survey Questionnaire - Wales
#Scenario  [CR-T163, CR-T164, CR-T165, CR-T166] Check a Respondent Authentication event is sent to RM after UAC authentication
#Scenario: [CR-T167, CR-T168] Check a Respondent Authentication event is sent to RM after UAC authentication - Wales
#Scenario  [CR-T163, CR-T164, CR-T165, CR-T166] Check an EQ Launched event is sent to RM after address confirmed


Feature:  UAC-Authentication-Success

  ##SETUP calls the following steps...
  ## Given RM constructs a case_created event and a uac_updated event
  ## When RM sends the case_created and uac <eventType> events to RH via RabbitMQ
  ## Then a valid uac exists in Firestore ready for use by a respondent
  @UAC-Authentication-Success-TestT163 @Setup @TearDown
  Scenario Outline: [CR-T163] The Happy Path - Access the Survey Questionnaire
    Given SETUP-3 - a valid uac exists in Firestore and there is an associated case in Firestore ENG eventType <eventType>
    Given I am a respondent and I am on the RH Start Page
    When I enter the valid UAC into the text box
    And I click the “Access survey” button
    Then I am presented with a page to confirm my address
    Given I select the “Yes, this address is correct” option
    When I click the “Continue” button
    Then I am directed to the Questionnaire
    And The Token Is Successfully Decrypted

    Examples:
      | eventType    |
      | "UAC_UPDATE" |

  ##SETUP calls the following steps...
  ##  Given RM constructs a case_created event and a uac_updated event where Region is W
  ##  When RM sends the case_created and a uac_updated events to RH via RabbitMQ
  ##  Then a valid uac exists in Firestore ready for use by a respondent
  @UAC-Authentication-Success-WalesTestT167 @Setup @TearDown
  Scenario: [CR-T167] The Happy Path - Access the Survey Questionnaire - Wales
    Given SETUP-4 - a valid uac and associated case exist in Firestore with region WALES
    Given I am a respondent and I am on the RH Start Page
    When I enter the valid UAC into the text box
    And I click the “Access survey” button
    Then I am presented with a page to confirm my address
    Given I select the “Yes, this address is correct” option
    When I click the “Continue” button
    Then I am directed to the Questionnaire

  ##SETUP calls the following steps...
  ## Given RM constructs a case_created event and a uac_updated event
  ## When RM sends the case_created and uac <eventType> events to RH via RabbitMQ
  ## Then a valid uac exists in Firestore ready for use by a respondent
  @UAC-Authentication-Success-TestT166First @Setup @TearDown
  Scenario Outline: [CR-T163, CR-T164, CR-T165, CR-T166] Check a Respondent Authentication event is sent to RM after UAC authentication
    Given SETUP-3 - a valid uac exists in Firestore and there is an associated case in Firestore ENG eventType <eventType>
    Given an empty queue exists for sending Respondent Authentication events
    And I am a respondent and I am on the RH Start Page
    When I enter the valid UAC into the text box
    And I click the “Access survey” button
    Then a Respondent Authentication event is sent to RM
    And the respondentAuthenticationHeader contains the correct values

    Examples:
      | eventType    |
      | "UAC_UPDATE" |

  ##SETUP calls the following steps...
  ##  Given RM constructs a case_created event and a uac_updated event where Region is W
  ##  When RM sends the case_created and a uac_updated events to RH via RabbitMQ
  ##  Then a valid uac exists in Firestore ready for use by a respondent
  @UAC-Authentication-Success-WalesTestT168Third @Setup @TearDown
  Scenario: [CR-T167, CR-T168] Check a Respondent Authentication event is sent to RM after UAC authentication - Wales
    Given SETUP-4 - a valid uac and associated case exist in Firestore with region WALES
    Given an empty queue exists for sending Respondent Authentication events
    And I am a respondent and I am on the RH Start Page
    When I enter the valid UAC into the text box
    And I click the “Access survey” button
    Then a Respondent Authentication event is sent to RM
    And the respondentAuthenticationHeader contains the correct values

  ##SETUP calls the following steps...
  ## Given RM constructs a case_created event and a uac_updated event
  ## When RM sends the case_created and uac <eventType> events to RH via RabbitMQ
  ## Then a valid uac exists in Firestore ready for use by a respondent
  @UAC-Authentication-Success-TestT166Second @Setup @TearDown
  Scenario Outline: [CR-T163, CR-T164, CR-T165, CR-T166] Check an EQ Launched event is sent to RM after address confirmed
    Given SETUP-3 - a valid uac exists in Firestore and there is an associated case in Firestore ENG eventType <eventType>
    Given an empty queue exists for sending Respondent Authentication events
    And I am a respondent and I am on the RH Start Page
    When I enter the valid UAC into the text box
    And I click the “Access survey” button
    Then a Respondent Authentication event is sent to RM
    Given I am presented with a page to confirm my address
    When I select the “Yes, this address is correct” option
    And an empty queue exists for sending EQ Launched events
    And I click the “Continue” button
    Then an EQ Launched event is sent to RM
    And the eqLaunchedHeader contains the correct values

    Examples:
      | eventType    |
      | "UAC_UPDATE" |

