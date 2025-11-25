package com.demowebshop.pages;

import com.demowebshop.utils.ElementHelper;
import org.openqa.selenium.By;

public class HomePage extends BasePage {
    private final By loginLink = By.cssSelector("a.ico-login");
    private final By registerLink = By.cssSelector("a.ico-register");
    private final By searchBox = By.id("small-searchterms");
    private final By searchBtn = By.cssSelector("button[type='submit']");

    public HomePage(ElementHelper helper) {
        super(helper);
    }

    public void goToLogin() {
        click(loginLink);
    }

    public void goToRegister() {
        click(registerLink);
    }

    public void search(String term) {
        type(searchBox, term);
        click(searchBtn);
    }

    public void logout() {
        By logoutLink = By.cssSelector("a.ico-logout");
        click(logoutLink);
    }
}
