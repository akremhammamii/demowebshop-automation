@registration
Feature: User Registration

  @positive
  Scenario: Register successfully with dynamic email
    Given I am on the registration page
    When I fill the register form with:
      | Gender     | Male            |
      | First name | John            |
      | Last name  | Doe             |
      | Email      | <dynamic_email> |
      | Password   | Password123!    |
      | Confirm PW | Password123!    |
    And I submit the registration form
    Then I should see a success message "Your registration completed"
    When I click on the Continue button
    Then the My Account section should be visible

  @negative
  Scenario Outline: Registration validation tests
    Given I am on the registration page
    When I fill the register form with:
      | Gender     | <gender>    |
      | First name | <firstname> |
      | Last name  | <lastname>  |
      | Email      | <email>     |
      | Password   | <password>  |
      | Confirm PW | <confirmPW> |
    And I submit the registration form
    Then I should see an error message "<error_message>"

    Examples:
      | gender | firstname | lastname | email            | password     | confirmPW    | error_message                                        |
      | Male   |           | Doe      | test@example.com | Password123! | Password123! | First name is required.                              |
      | Male   | John      |          | test@example.com | Password123! | Password123! | Last name is required.                               |
      | Male   | John      | Doe      |                  | Password123! | Password123! | Email is required.                                   |
      | Male   | John      | Doe      | test@example.com |              | Password123! | Password is required.                                |
      | Male   | John      | Doe      | invalid-email    | Password123! | Password123! | Wrong email                                          |
      | Male   | John      | Doe      | test@            | Password123! | Password123! | Wrong email                                          |
      | Male   | John      | Doe      | test@example.com | Password123! | DifferentPW! | The password and confirmation password do not match. |
      | Male   | John      | Doe      | test@example.com | weak         | weak         | The password should have at least 6 characters.      |
