package com.demowebshop.pages;

import com.demowebshop.utils.ElementHelper;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public abstract class BasePage {
    protected final ElementHelper helper;

    public BasePage(ElementHelper helper) {
        this.helper = helper;
    }

    @Step("Click {0}")
    protected void click(By locator) {
        helper.click(locator);
    }

    @Step("Type to {0}")
    protected void type(By locator, String text) {
        helper.type(locator, text);
    }

    @Step("Get text from {0}")
    protected String getText(By locator) {
        return helper.waitVisible(locator).getText();
    }

    public boolean isDisplayed(By locator) {
        return helper.isDisplayed(locator);
    }
}