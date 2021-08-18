#Author: eleanor.cook@ons.gov.uk
#Keywords Summary : test UAC Authentication Failures
#Feature:  UAC-Authentication-Failure
#Scenario: [CR-T163, CR-T164, CR-T165, CR-T166] Unhappy path - No UAC entered
#Scenario: [CR-T163, CR-T164, CR-T165, CR-T166] Unhappy path – Invalid UAC entered
#Scenario: [CR-T167, CR-T168] Unhappy path - No UAC entered - Wales
#Scenario: [CR-T167, CR-T168] Unhappy path – Invalid UAC entered - Wales
## (Comments)

Feature:  UAC-Authentication-Failure

  ## this test only covers scenario for caseType = HH & individual flag = false
  ## additional tests need to be written to cover CE and SPG caseTypes and HH with individual flag = true
  @UAC-Authentication-Failure-TestT164
  @UAC-Authentication-Failure-WalesTestT168First
  @Setup @TearDown
  Scenario Outline: [CR-T163, CR-T164, CR-T165, CR-T166, CR-T169, CR-170,CR-T172, CR-T173, CR-T174, CR-T167, CR-T168] Unhappy path - No UAC entered
    Given I am a respondent and I am on the RH Start Page <country>
    When I click the “Access survey” button
    Then a blank uac error <errorMessage> appears

    Examples:
      | country  | errorMessage |
      ## CR-T163, CR-T164, CR-T165, CR-T166
      |  ENG   | "Enter an access code"   |
      ## CR-T167, CR-T168
      |  WALES | "Rhowch god mynediad" |

  @UAC-Authentication-Failure-TestT165
  @UAC-Authentication-Failure-WalesTestT168Second
  @Setup @TearDown
  Scenario Outline: [CR-T163, CR-T164, CR-T165, CR-T166, CR-T169, CR-170,CR-T172, CR-T173, CR-T174, CR-T167, CR-T168] Unhappy path – Invalid UAC entered
    Given I am a respondent and I am on the RH Start Page <country>
    When I enter an invalid UAC "aaaaaaaaaaaaaaaa" into the text box
    And I click the “Access survey” button
    Then an invalid uac error <errorMessage> appears

    Examples:
      | country  | errorMessage |
      ## CR-T163, CR-T164, CR-T165, CR-T166
      |  ENG   | "Enter a valid access code"   |
      ## CR-T167, CR-T168
      |  WALES | "Rhowch god mynediad dilys" |
