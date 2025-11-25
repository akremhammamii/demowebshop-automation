package com.demowebshop.steps;

import com.demowebshop.context.TestContext;
import com.demowebshop.utils.RandomUtil;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginSteps {
    private final TestContext context;

    public LoginSteps(TestContext context) {
        this.context = context;
    }

    @Given("I open the demo webshop")
    public void openShop() {
        context.getDriver().get(java.util.Objects.requireNonNull(
                com.demowebshop.config.ConfigurationManager.get("baseUrl"), "baseUrl not found in config"));
    }

    @Given("I navigate to the registration page")
    public void navigateToRegistrationPage() {
        openShop();
        context.getPages().home().goToRegister();
    }

    @And("I register a new user with valid information and a dynamic email")
    public void registerNewUserWithDynamicEmail() {
        String email = RandomUtil.randomEmail();
        String password = "Password123!";
        
        // Store credentials for later use
        context.getScenarioContext().set("registeredEmail", email);
        context.getScenarioContext().set("registeredPassword", password);
        
        // Fill and submit registration form
        context.getPages().register().selectGender("Male");
        context.getPages().register().fillFirstName("John");
        context.getPages().register().fillLastName("Doe");
        context.getPages().register().fillEmail(email);
        context.getPages().register().fillPassword(password);
        context.getPages().register().fillConfirmPassword(password);
        context.getPages().register().clickRegisterButton();
    }

    @And("I see {string}")
    public void iSeeMessage(String expectedMessage) {
        String actualMessage = context.getPages().register().getSuccessMessage();
        assertTrue(actualMessage.contains(expectedMessage),
                "Expected to see: " + expectedMessage + ", but got: " + actualMessage);
    }

    @And("I click Continue after registration")
    public void clickContinueAfterRegistration() {
        context.getPages().register().clickContinue();
    }

    @And("I am on the home page")
    public void verifyOnHomePage() {
        // Verify we're on home page by checking the My Account link is visible
        assertTrue(context.getPages().register().isMyAccountVisible(),
                "Not on home page - My Account link not visible");
    }

    @Given("I navigate to the login page")
    public void navigateToLoginPage() {
        // Logout first if already logged in, then go to login page
        try {
            context.getPages().home().logout();
        } catch (Exception e) {
            // Ignore if logout fails (user not logged in)
        }
        context.getPages().home().goToLogin();
    }

    @When("I login with the valid email and password created during registration")
    public void loginWithValidCredentials() {
        String email = (String) context.getScenarioContext().get("registeredEmail");
        String password = (String) context.getScenarioContext().get("registeredPassword");
        context.getPages().login().login(email, password);
    }

    @When("I attempt to login with email {string} and password {string}")
    public void attemptLoginWithCredentials(String email, String password) {
        // Handle special case for dynamic email placeholder
        if ("<validEmailDynamic>".equals(email)) {
            email = (String) context.getScenarioContext().get("registeredEmail");
        }
        
        // Fill the form
        context.getPages().login().fillEmail(email);
        context.getPages().login().fillPassword(password);
        context.getPages().login().clickLoginButton();
    }

    @Then("I should see the My Account page")
    public void verifyShouldSeeMyAccountPage() {
        assertTrue(context.getPages().login().isMyAccountPageVisible(),
                "My Account page should be visible after successful login");
    }

    @Then("I should see the error message {string}")
    public void verifyShouldSeeErrorMessage(String expectedError) {
        String actualError = context.getPages().login().getErrorMessage();
        assertTrue(actualError.contains(expectedError),
                "Expected error message to contain: " + expectedError + ", but got: " + actualError);
    }

    // Legacy steps kept for backward compatibility
    @When("I go to login")
    public void goToLogin() {
        context.getPages().home().goToLogin();
    }

    @And("I login with {string} and {string}")
    public void loginWith(final String email, final String pass) {
        context.getPages().login().login(email, pass);
    }

    @And("I login with registered credentials")
    public void loginWithRegisteredCredentials() {
        String email = (String) context.getScenarioContext().get("registeredEmail");
        String password = "Password123!";
        context.getPages().login().login(email, password);
    }

    @When("I logout")
    public void logout() {
        context.getPages().home().logout();
    }

    @Then("I should be logged in")
    public void shouldBeLogged() {
        assertTrue(context.getPages().home().isDisplayed(
                org.openqa.selenium.By.cssSelector("a.account")),
                "User is not logged in - account link not found");
    }
}
