package com.demowebshop.context;

import com.demowebshop.config.ConfigurationManager;
import com.demowebshop.core.DriverManager;
import com.demowebshop.core.WebDriverFactory;
import com.demowebshop.pages.PageManager;
import com.demowebshop.utils.ElementHelper;
import org.openqa.selenium.WebDriver;

public class TestContext {
    private final WebDriver driver;
    private final ElementHelper helper;
    private final PageManager pages;
    private final ScenarioContext scenarioContext;

    public TestContext() {
        String browser = ConfigurationManager.get("browser");
        boolean headless = ConfigurationManager.getBool("headless", false);
        this.driver = WebDriverFactory.createDriver(browser, headless);
        DriverManager.setDriver(this.driver);
        this.helper = new ElementHelper(driver);
        this.pages = new PageManager(helper);
        this.scenarioContext = new ScenarioContext();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public ElementHelper getHelper() {
        return helper;
    }

    public PageManager getPages() {
        return pages;
    }

    public ScenarioContext getScenarioContext() {
        return scenarioContext;
    }

    public void quit() {
        DriverManager.quitDriver();
    }
}
