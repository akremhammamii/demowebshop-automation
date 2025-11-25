package com.demowebshop.pages;

import com.demowebshop.utils.ElementHelper;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {
    private final By email = By.id("Email");
    private final By password = By.id("Password");
    private final By loginBtn = By.cssSelector("input.login-button");
    private final By errorMessage = By.cssSelector(".validation-summary-errors");
    private final By myAccountLink = By.cssSelector("a.account");

    public LoginPage(ElementHelper helper) {
        super(helper);
    }

    public void fillEmail(String emailValue) {
        if (emailValue != null && !emailValue.isEmpty()) {
            type(email, emailValue);
        }
    }

    public void fillPassword(String passwordValue) {
        if (passwordValue != null && !passwordValue.isEmpty()) {
            type(password, passwordValue);
        }
    }

    public void clickLoginButton() {
        click(loginBtn);
    }

    public void login(String user, String pass) {
        type(email, user);
        type(password, pass);
        click(loginBtn);
    }

    public String getErrorMessage() {
        if (isDisplayed(errorMessage)) {
            return getText(errorMessage);
        }
        return "";
    }

    public boolean isMyAccountPageVisible() {
        return isDisplayed(myAccountLink);
    }
}
