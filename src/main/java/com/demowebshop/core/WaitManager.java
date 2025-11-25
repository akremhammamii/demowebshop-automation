package com.demowebshop.core;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitManager {

    private final WebDriverWait wait;

    public WaitManager(WebDriver driver, int timeoutSeconds) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    public WebElement waitForVisible(final By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(java.util.Objects.requireNonNull(locator)));
    }

    public WebElement waitForClickable(final By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(java.util.Objects.requireNonNull(locator)));
    }

    public boolean waitForInvisibility(final By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(java.util.Objects.requireNonNull(locator)));
    }
}
