/**
 * Utility class providing common Selenium WebDriver helper methods.
 * Used across page objects for waiting, clicking, typing, scrolling and screenshots.
 */
package com.demowebshop.utils;

import org.openqa.selenium.By;
import org.jetbrains.annotations.NotNull;
import com.demowebshop.config.ConfigurationManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.ByteArrayInputStream;
import java.time.Duration;

public class ElementHelper {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public ElementHelper(WebDriver driver) {
        this.driver = driver;
        int t = ConfigurationManager.getInt("timeout", 10);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(t));
    }

    public WebElement waitVisible(@NotNull By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitClickable(@NotNull By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void click(@NotNull By locator) {
        waitClickable(locator).click();
    }

    public void clickJS(@NotNull By locator) {
        WebElement el = waitVisible(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    public void type(@NotNull By locator, String text) {
        WebElement el = waitVisible(locator);
        el.clear();
        el.sendKeys(text);
    }

    public void scrollTo(@NotNull By locator) {
        WebElement el = waitVisible(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
    }

    public boolean isDisplayed(@NotNull By locator) {
        try {
            return waitVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public byte[] takeScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    public ByteArrayInputStream takeScreenshotStream() {
        return new ByteArrayInputStream(takeScreenshot());
    }

}
