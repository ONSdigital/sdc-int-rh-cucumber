#Author: eleanor.cook@ons.gov.uk
#Keywords Summary : Request UAC
#Feature: Request-UAC
#Scenario: [CR-T65] Check if an error message is displayed when an incorrect postcode is entered
#Scenario: [CR-T67] Check if an address is displayed in ‘Is your address correct?’ page
#Scenario: [CR-T70] User selects NO option on ‘Is your address correct?’ page
#Scenario: [CR-T71] Check if a message is displayed when an address is not included for rehearsal area
#Scenario: [CR-T73] Respondent selects Yes option in 'Is your mobile number correct?' page
#Scenario: [CR-T74] Respondent selects No option in 'Is your mobile number correct?' page
#Scenario: [CR-T75] Check if an error message is displayed when an invalid mobile number is entered
#Scenario: [CR-T76] Check if a fulfilment request is sent to RM after the Respondent confirms the mobile no.
#Scenario: [CR-T389] Request UAC for a address in AIMS but not in RHSVC - Text Message
#Scenario: [CR-T390] Request UAC for a address in AIMS but not in RHSVC - Postal

Feature: Request-UAC
  I would like to request a new Unique Access Code
  @RequestUAC-TestT65 @Setup @TearDown
  Scenario Outline: [CR-T65] Check if an error message is displayed when an incorrect postcode is entered
    Given I am a respondent and I am on the RH Start Page <country>
    When I click on request a new access code
    Then I am presented with a page to enter my postcode on

    # WRITEME - RHUI not ready yet.
    # And I enter the invalid UK postcode "aaaaa" into the postcode textbox
    # And select Continue
    # Then an invalid postcode error <errorMessage> appears

    Examples:
      | country | errorMessage |
      | ENG   | "Enter a valid UK postcode" |
      | WALES | "Rhowch god post dilys yn y Deyrnas Unedig" |

  @RequestUAC-TestT67 @Setup @TearDown @ignore
  Scenario Outline: [CR-T67] Check if an address is displayed in ‘Is your address correct?’ page
    Given SETUP-4 - a valid uac and associated case exist in Firestore with region <country>
    Given I am a respondent and I am on the RH Start Page <country>
    When I click on request a new access code
    Then I am presented with a page to enter my postcode on
    And I enter the valid UK postcode <postcode>
    And select Continue
    Then I am presented with a page to select an address from
    Given a number of addresses is displayed
    When I select the first address and click continue
    Then I am presented with a page displaying the expected address

    Examples:
      | country | postcode   | street              |
      | ENG   | "EX2 6GA"  | 'England House'     |
      | WALES | "CF3 2TW"  | '1 Pentwyn Terrace' |

  @RequestUAC-TestT70 @Setup @TearDown @ignore
  Scenario Outline: [CR-T70] User selects NO option on ‘Is your address correct?’ page
    Given SETUP-4 - a valid uac and associated case exist in Firestore with region <country>
    Given I am a respondent and I am on the RH Start Page <country>
    When I click on request a new access code
    Then I am presented with a page to enter my postcode on
    And I enter the valid UK postcode <postcode>
    And select Continue
    Then I am presented with a page to select an address from
    Given a number of addresses is displayed
    Then I select the first address and click continue
    Given I am presented with a page displaying the expected address
    When I select the ‘No, I need to change the address’ option and click continue
    Then I am presented with a page to enter my postcode on

    Examples:
      | country | postcode | address                                                      |
      | ENG   | "EX2 6GA"  | 'England House\nEngland Street\nSmithfield\nExeter\nEX1 2TD' |
      | WALES | "CF3 2TW"  | '1 Pentwyn Terrace\nMarshfield\nCardiff\nCF3 2TW'            |

  ##SETUP calls the following steps...
  ## Given RM constructs a case_created event and a uac_updated event
  ## When RM sends the case_created and a uac_updated events to RH via RabbitMQ
  ## Then a valid uac exists in Firestore ready for use by a respondent
  @RequestUAC-TestT73 @Setup @TearDown @ignore
  Scenario Outline: [CR-T73] Respondent selects Yes option in 'Is your mobile number correct?' page
    Given SETUP-2 - a valid uac exists in Firestore and there is an associated case in Firestore <country>
    Given I am a respondent and I am on the RH Start Page
    When I click on request a new access code
    Then I am presented with a page to enter my postcode on
    And I enter the valid UK postcode <postcode>
    And select Continue
    Then I am presented with a page to select an address from
    Given a number of addresses is displayed
    Then I select the first address and click continue
    Given I am presented with a page displaying the expected address
    When I select the ‘YES, This address is correct’ option and click continue
    And the respondent sees the Household Interstitial page and clicks continue
    Then I am presented with a page asking text or post
    Given I select 'Text message' and click 'Continue'
    Then I am presented with a page asking for my mobile number
    Given I enter my mobile number and click continue
    And I am presented with a page to confirm my mobile number on
    When I select the Yes option to send the text now
    Then I am presented with a message saying that I have been sent an access code
    And I am presented with a button <startsurvey>
    And I am also presented with a link to request a new access code

    Examples:
      | country | postcode   | street              | address                                                      | startsurvey         |
      | ENG   | "EX2 6GA"  | 'England House'     | 'England House\nEngland Street\nSmithfield\nExeter\nEX1 2TD' | 'Start survey'        |
      | WALES | "CF3 2TW"  | '1 Pentwyn Terrace' | '1 Pentwyn Terrace\nMarshfield\nCardiff\nCF3 2TW'            | "Dechrau'r cyfrifiad" |

  ##SETUP calls the following steps...
  ## Given RM constructs a case_created event and a uac_updated event
  ## When RM sends the case_created and a uac_updated events to RH via RabbitMQ
  ## Then a valid uac exists in Firestore ready for use by a respondent
  @ignore
  @RequestUAC-TestT74 @Setup @TearDown
  Scenario Outline: [CR-T74] Respondent selects No option in 'Is your mobile number correct?' page
    Given SETUP-2 - a valid uac exists in Firestore and there is an associated case in Firestore <country>
    Given I am a respondent and I am on the RH Start Page
    When I click on request a new access code
    Then I am presented with a page to enter my postcode on
    And I enter the valid UK postcode <postcode>
    And select Continue
    Then I am presented with a page to select an address from
    Given a number of addresses is displayed
    Then I select the first address and click continue
    Given I am presented with a page displaying the expected address
    When I select the ‘YES, This address is correct’ option and click continue
    And the respondent sees the Household Interstitial page and clicks continue
    Then I am presented with a page asking text or post
    Given I select 'Text message' and click 'Continue'
    Then I am presented with a page asking for my mobile number
    Given I enter my mobile number and click continue
    And I am presented with a page to confirm my mobile number on
    When I select the No option (because the number displayed is not correct)
    Then I am presented with a page asking for my mobile number

    Examples:
      | country | postcode   | street              | address                                                      |
      | ENG   | "EX2 6GA"  | 'England House'     | 'England House\nEngland Street\nSmithfield\nExeter\nEX1 2TD' |
      | WALES | "CF3 2TW"  | '1 Pentwyn Terrace' | '1 Pentwyn Terrace\nMarshfield\nCardiff\nCF3 2TW'            |

  ##SETUP calls the following steps...
  ## Given RM constructs a case_created event and a uac_updated event
  ## When RM sends the case_created and a uac_updated events to RH via RabbitMQ
  ## Then a valid uac exists in Firestore ready for use by a respondent
  @ignore
  @RequestUAC-TestT75 @Setup @TearDown
  Scenario Outline: [CR-T75] Check if an error message is displayed when an invalid mobile number is entered
    Given SETUP-2 - a valid uac exists in Firestore and there is an associated case in Firestore <country>
    Given I am a respondent and I am on the RH Start Page
    When I click on request a new access code
    Then I am presented with a page to enter my postcode on
    And I enter the valid UK postcode <postcode>
    And select Continue
    Then I am presented with a page to select an address from
    Given a number of addresses is displayed
    Then I select the first address and click continue
    Given I am presented with a page displaying the expected address
    And I select the ‘YES, This address is correct’ option and click continue
    And the respondent sees the Household Interstitial page and clicks continue
    Then I am presented with a page asking text or post
    Given I select 'Text message' and click 'Continue'
    And I am presented with a page asking for my mobile number
    When I enter an invalid mobile number and click continue
    Then an invalid mobile number error message is displayed

    Examples:
      | country | postcode   | street              | address                                                      |
      | ENG   | "EX2 6GA"  | 'England House'     | 'England House\nEngland Street\nSmithfield\nExeter\nEX1 2TD' |
      | WALES | "CF3 2TW"  | '1 Pentwyn Terrace' | '1 Pentwyn Terrace\nMarshfield\nCardiff\nCF3 2TW'            |

  ##SETUP calls the following steps...
  ## Given RM constructs a case_created event and a uac_updated event
  ## When RM sends the case_created and a uac_updated events to RH via RabbitMQ
  ## Then a valid uac exists in Firestore ready for use by a respondent
  @ignore
  @RequestUAC-TestT76 @Setup @TearDown
  Scenario Outline:[CR-T76] Check if a fulfilment request is sent to RM after the Respondent confirms the mobile no.
    Given SETUP-2 - a valid uac exists in Firestore and there is an associated case in Firestore <country>
    Given I am a respondent and I am on the RH Start Page
    When I click on request a new access code
    Then I am presented with a page to enter my postcode on
    And I enter the valid UK postcode <postcode>
    And select Continue
    Then I am presented with a page to select an address from
    Given a number of addresses is displayed
    Then I select the first address and click continue
    Given I am presented with a page displaying the expected address
    When I select the ‘YES, This address is correct’ option and click continue
    And the respondent sees the Household Interstitial page and clicks continue
    Then I am presented with a page asking text or post
    Given I select 'Text message' and click 'Continue'
    Then I am presented with a page asking for my mobile number
    Given I enter my mobile number and click continue
    And I am presented with a page to confirm my mobile number on
    And an empty queue exists for sending Fulfilment Requested events
    When I select the Yes option to send the text now
    Then a FulfilmentRequested event is sent to RM

    Examples:
      | country | postcode   | street              | address                                                      |
      | ENG   | "EX2 6GA"  | 'England House'     | 'England House\nEngland Street\nSmithfield\nExeter\nEX1 2TD' |
      | WALES | "CF3 2TW"  | '1 Pentwyn Terrace' | '1 Pentwyn Terrace\nMarshfield\nCardiff\nCF3 2TW'            |

  #"Given Steps"
  #" 1. Address exists in AIMS (UPRN)
  #" 2. There no case linked to the selected UPRN in RHSVC
  #" 3. the respondent is requesting a UAC (What is your postcode? page)
  #" 4. respondent has selected the address in step 1
  @ignore
  @RequestUAC-Test389
  @SetUpRHEng
  @TearDown
  Scenario Outline: [CR-T389] Request UAC for a address in AIMS but not in RHSVC - Text Message
    Given The respondent confirms a valid <country> address <postcode>
    And the respondent sees the Household Interstitial page and clicks continue
    And the respondent selects the delivery channel as "Text message"
    And the respondents enters their valid UK mobile telephone number
    When the respondents selects continue "text"
    Then RHSVC publishes a new address event
    And RHSVC publishes a UAC fulfilment request

    Examples:
      | country | postcode   |
      | ENG   | "EX2 6GA"  |
      | WALES | "CF3 2TW"  |

  #"Given Steps"
  #" 1. Address exists in AIMS (UPRN)
  #" 2. There no case linked to the selected UPRN in RHSVC
  #" 3. the respondent is requesting a UAC (What is your postcode? page)
  #" 4. respondent has selected the address in step 1
  @ignore
  @RequestUAC-Test390
  @SetUpRHEng
  @TearDown
  Scenario Outline: [CR-T390] Request UAC for a address in AIMS but not in RHSVC - postal
    Given The respondent confirms a valid <country> address <postcode>
    And the respondent sees the Household Interstitial page and clicks continue
    And the respondent selects the delivery channel as "Post"
    And the respondent enters their first and last name
    When the respondent confirms the address by selecting "Yes, send the access code by post"
    And selects continue
    Then RHSVC publishes a new address event
    And RHSVC publishes a UAC fulfilment request

    Examples:
      | country | postcode   |
      | ENG   | "EX2 6GA"  |
      | WALES | "CF3 2TW"  |

  # Setup Steps
  # 1. The respondent is requesting a household UAC for Wales (in English) via SMS
  # 2. Address exists in AIMS (UPRN)
  # 3. The respondent enters their postcode on What is your postcode? path /en/requests/access-code/enter-address/
  # 4. respondent has selected the address in step 1
  # 5. the respondent confirms the address
  # 6. the respondent selects the delivery channel as Text message
  # 7. the respondents enters their valid UK mobile telephone number
  # 8. the respondent selects Yes, send the text option on path /en/requests/access-code/confirm-mobile/
  @ignore
  @RequestUAC-Test402
  @SetUpRHEng
  @TearDown
  Scenario Outline: [CR-T402/403] - Request UAC for a address in AIMS but not in RHSVC - text
    Given the respondent selects continue on the confirm your mobile page "CF3 2TW" <country>
    When RHSVC publishes a fulfilment request
    Then check the fulfilment code is <fulfilmentCode>

    Examples:
      | country | fulfilmentCode |
      | ENG   | "UACHHT2"      |
      | WALES | "UACHHT2W"     |

  #  " Setup Steps
  #  " 1. The respondent is requesting a household UAC for Wales (in English) via SMS
  #  " 2. Address exists in AIMS (UPRN)
  #  " 3. The respondent enters their postcode on What is your postcode? path /en/requests/access-code/enter-address/
  #  " 4. respondent has selected the address in step 1
  #  " 5. the respondent confirms the address
  #  " 6. the respondent selects the delivery channel as Post
  #  " 7. the respondents enters their first and last name
  #  " 8. the respondent selects Yes, send the access code by post option on path /en/requests/access-code/confirm-name-address/
  @ignore
  @RequestUAC-Test404
  @SetUpRHEng
  @TearDown
  Scenario: [CR-T404] - Check an new address added event is created
    Given the respondent selects continue on the confirm postal address page "CF3 2TW" ENG
    When RHSVC publishes a fulfilment request
    Then check the fulfilment code is "P_UAC_UACHHP2B"

  #"Given Steps"
  #" 1. Address exists in AIMS (UPRN)
  #" 2. There no case linked to the selected UPRN in RHSVC
  #" 3. the respondent is requesting a UAC (What is your postcode? page)
  #" 4. respondent has selected the address in step 1
  @ignore
  @RequestUAC-Test411
  @SetUpRHEng
  @TearDown
  Scenario Outline: [CR-T411] - Check an new address added event is created
    Given I click on "request a new access code" in the start page <country>
    And I enter a postcode <postcode>
    And select "I can't find my address", and click "Continue"
    Then I am presented with a page to call the Customer Contact Centre with the correct telephone number

    Examples:
      | country | postcode   |
      | ENG   | "EX2 6GA"  |
      | WALES | "SA38 9NP" |
