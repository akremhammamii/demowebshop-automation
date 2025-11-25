@login
Feature: User Login

  Background:
    Given I navigate to the registration page
    And I register a new user with valid information and a dynamic email
    And I see "Your registration completed"
    And I click Continue after registration
    And I am on the home page

  @positive
  Scenario: Successful login with valid credentials
    Given I navigate to the login page
    When I login with the valid email and password created during registration
    Then I should see the My Account page

  @negative
  Scenario Outline: Failed login with invalid credentials
    Given I navigate to the login page
    When I attempt to login with email "<email>" and password "<password>"
    Then I should see the error message "<message>"

    Examples:
      | email               | password     | message                                                          |
      | invalid@test.com    | WrongPass123 | Login was unsuccessful. Please correct the errors and try again. |
      | <validEmailDynamic> | WrongPass123 | Login was unsuccessful. Please correct the errors and try again. |
      |                     | Pass123!     | Login was unsuccessful. Please correct the errors and try again. |
      | invalid@test.com    |              | Login was unsuccessful. Please correct the errors and try again. |

  @bug @BUG-LOGIN-001 @negative
  Scenario: Login with invalid email format shows no error (Known Bug)
    Given I navigate to the login page
    When I attempt to login with email "invalid_format" and password "Pass123!"
    Then I should see the error message "Login was unsuccessful. Please correct the errors and try again."
