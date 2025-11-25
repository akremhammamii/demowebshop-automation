package com.demowebshop.steps;

import com.demowebshop.context.TestContext;
import com.demowebshop.utils.RandomUtil;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterSteps {
    private final TestContext context;

    public RegisterSteps(TestContext context) {
        this.context = context;
    }

    @When("I register a new user")
    public void registerUser() {
        String email = RandomUtil.randomEmail();
        context.getScenarioContext().set("registeredEmail", email);
        context.getPages().home().goToRegister();
        context.getPages().register().register("John", "Doe", email, "Password123!");
        context.getPages().register().clickContinue();
    }

    @Given("I am on the registration page")
    public void navigateToRegistrationPage() {
        // First open the website
        context.getDriver().get(java.util.Objects.requireNonNull(
                com.demowebshop.config.ConfigurationManager.get("baseUrl"), "baseUrl not found in config"));
        // Then navigate to registration
        context.getPages().home().goToRegister();
    }

    @When("I fill the register form with:")
    public void fillRegisterForm(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);

        String gender = data.get("Gender");
        String firstName = data.get("First name");
        String lastName = data.get("Last name");
        String email = data.get("Email");
        String password = data.get("Password");
        String confirmPW = data.get("Confirm PW");

        // Handle dynamic email generation
        if ("<dynamic_email>".equals(email)) {
            email = RandomUtil.randomEmail();
            context.getScenarioContext().set("registeredEmail", email);
        }

        // Fill the form
        if (gender != null && !gender.isEmpty()) {
            context.getPages().register().selectGender(gender);
        }
        context.getPages().register().fillFirstName(firstName);
        context.getPages().register().fillLastName(lastName);
        context.getPages().register().fillEmail(email);
        context.getPages().register().fillPassword(password);
        context.getPages().register().fillConfirmPassword(confirmPW);
    }

    @When("I submit the registration form")
    public void submitRegistrationForm() {
        context.getPages().register().clickRegisterButton();
    }

    @Then("I should see a success message {string}")
    public void verifyShouldSeeSuccessMessage(String expectedMessage) {
        String actualMessage = context.getPages().register().getSuccessMessage();
        assertTrue(actualMessage.contains(expectedMessage),
                "Expected success message to contain: " + expectedMessage + ", but got: " + actualMessage);
    }

    @When("I click on the Continue button")
    public void clickContinueButton() {
        context.getPages().register().clickContinue();
    }

    @Then("the My Account section should be visible")
    public void verifyMyAccountVisible() {
        assertTrue(context.getPages().register().isMyAccountVisible(),
                "My Account section should be visible after successful registration");
    }

    @Then("I should see an error message {string}")
    public void verifyShouldSeeErrorMessage(String expectedError) {
        String actualError = context.getPages().register().getValidationErrorMessage();
        assertTrue(actualError.contains(expectedError),
                "Expected error message to contain: " + expectedError + ", but got: " + actualError);
    }

    @Then("registration should be successful")
    public void verifyRegister() {
        // check account link visible (after clicking Continue, user is logged in)
        assertTrue(context.getPages().home().isDisplayed(org.openqa.selenium.By.cssSelector("a.account")));
    }

    @Then("I should see a confirmation message")
    public void verifyShouldSeeConfirmationMessage() {
        // After registration, the continue button redirects to homepage and user is logged in
        // Verify the account link is visible as confirmation
        assertTrue(context.getPages().home().isDisplayed(org.openqa.selenium.By.cssSelector("a.account")),
                "Confirmation message not found - user not logged in after registration");
    }
}
