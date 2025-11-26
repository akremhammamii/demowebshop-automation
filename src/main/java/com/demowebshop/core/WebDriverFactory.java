package com.demowebshop.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import com.demowebshop.config.ConfigurationManager;
import java.time.Duration;

public class WebDriverFactory {

    public static WebDriver createDriver() {

        // Required for Linux/Docker (chromium-driver)
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");

        ChromeOptions options = new ChromeOptions();

        boolean headless = ConfigurationManager.getBool("headless", false);
        if (headless) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        // Chromium binary in Debian/Ubuntu
        options.setBinary("/usr/bin/chromium");

        WebDriver driver = new ChromeDriver(options);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        if (!headless) {
            driver.manage().window().maximize();
        }

        System.out.println("Chromium headless: " + headless);
        return driver;
    }
}
