#Author: steve.scorfield@ons.gov.uk
#Keywords Summary : test NewCase
#Feature:  NewCase-Registration-Success
#Scenario Outline: [SOCINT-154, SOCINT-155] New Case Registration Success - Access the Infection Survey Questionnaire


Feature:  NewCase-SIS2-Registration-Success

  ##SETUP calls the following steps...
  ## Given A parent is invited and wats to take part in the SIS2 survey
  ## When the parent logs onto the given url for the survey
  ## Then the parent successfully registers them and their childs details
  @NewCase-SIS2-Registration-Success-TestT155 @Setup @TearDown
  Scenario: [SOCINT-154, SOCINT-155] Registration of new case - Access School Infection Questionnaire
    Given I am on the take part in a survey page
    And I click on "COVID-19 Schools Infection Survey" link
    Then I am presented with a page on how to take part
    And I click on "Register for the survey" button
    Then I am presented with a page to register the child
    And I click on "Register now" button
    Then I am presented with a page to add my name
    And I enter my first, middle and last name
    And I click the “Continue” button
    Then I am presented with a page to enter my mobile number
    And I enter a valid mobile number and click “Continue”
    Then I am presented with a page to confirm my mobile number on
    Given The number is correct and I select the "Yes, my mobile number is correct" button
    And I click the “Continue” button
    Then I am presented with a page to confirm my consent
    And I click on the "I accept" option
    Then I am presented with a page to add my childs name
    And I enter my childs first, middle and last name
    And I then click the "Save and continue" button
    Then I am presented with a page to add my childs school
    And I enter my childs school name "The Aldgate School, City of London"
    And I click the “Continue” button
    Then I am presented with a page to add my childs date of birth
    And I enter my childs date of birth
    And I click the “Continue” button
    Then I am presented with a page to review my childs details
    And I then click the "Save and continue" button
    Then I am presented with a page to confirm child registration


