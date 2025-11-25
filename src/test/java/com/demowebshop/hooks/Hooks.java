package com.demowebshop.hooks;

import com.demowebshop.context.TestContext;
import com.demowebshop.reporting.AllureManager;
import io.cucumber.java.*;

public class Hooks {
    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        // base URL can be navigated in Given steps
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        boolean allSteps = Boolean.parseBoolean(System.getProperty("allure.screenshot.everystep", "false"));
        if (scenario.isFailed() || allSteps) {
            try {
                byte[] shot = context.getHelper().takeScreenshot();
                AllureManager.attachScreenshot(shot, "screenshot");
            } catch (Exception ignored) {
            }
        }
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            AllureManager.attachText("Scenario failed", scenario.getName());
        }
        context.quit();
    }
}
