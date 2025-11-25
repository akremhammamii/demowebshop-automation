package com.demowebshop.pages;

import com.demowebshop.utils.ElementHelper;
import org.openqa.selenium.By;

public class RegisterPage extends BasePage {
    private final By genderMale = By.id("gender-male");
    private final By genderFemale = By.id("gender-female");
    private final By firstName = By.id("FirstName");
    private final By lastName = By.id("LastName");
    private final By email = By.id("Email");
    private final By password = By.id("Password");
    private final By confirmPassword = By.id("ConfirmPassword");
    private final By registerBtn = By.id("register-button");
    private final By continueBtn = By.cssSelector(".button-1.register-continue-button");
    private final By successMessage = By.cssSelector(".result");
    private final By validationSummary = By.cssSelector(".validation-summary-errors");
    private final By fieldValidationError = By.cssSelector(".field-validation-error");

    public RegisterPage(ElementHelper helper) {
        super(helper);
    }

    public void selectGender(String gender) {
        if ("Male".equalsIgnoreCase(gender)) {
            click(genderMale);
        } else if ("Female".equalsIgnoreCase(gender)) {
            click(genderFemale);
        }
    }

    public void fillFirstName(String fn) {
        if (fn != null && !fn.isEmpty()) {
            type(firstName, fn);
        }
    }

    public void fillLastName(String ln) {
        if (ln != null && !ln.isEmpty()) {
            type(lastName, ln);
        }
    }

    public void fillEmail(String emailAddr) {
        if (emailAddr != null && !emailAddr.isEmpty()) {
            type(email, emailAddr);
        }
    }

    public void fillPassword(String pwd) {
        if (pwd != null && !pwd.isEmpty()) {
            type(password, pwd);
        }
    }

    public void fillConfirmPassword(String confirmPwd) {
        if (confirmPwd != null && !confirmPwd.isEmpty()) {
            type(confirmPassword, confirmPwd);
        }
    }

    public void clickRegisterButton() {
        click(registerBtn);
    }

    public void register(String fn, String ln, String emailAddr, String pwd) {
        click(genderMale);
        type(firstName, fn);
        type(lastName, ln);
        type(email, emailAddr);
        type(password, pwd);
        type(confirmPassword, pwd);
        click(registerBtn);
    }

    public void clickContinue() {
        click(continueBtn);
    }

    public String getSuccessMessage() {
        return getText(successMessage);
    }

    public String getValidationErrorMessage() {
        // Try validation summary first
        if (isDisplayed(validationSummary)) {
            return getText(validationSummary);
        }
        // Then try field validation errors
        if (isDisplayed(fieldValidationError)) {
            return getText(fieldValidationError);
        }
        return "";
    }

    public boolean isMyAccountVisible() {
        By myAccountLink = By.cssSelector("a.account");
        return isDisplayed(myAccountLink);
    }
}
