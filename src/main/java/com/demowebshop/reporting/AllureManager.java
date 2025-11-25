package com.demowebshop.reporting;

import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;

public class AllureManager {
    public static void attachScreenshot(byte[] bytes, String name) {
        Allure.addAttachment(name, new ByteArrayInputStream(bytes));
    }

    public static void attachText(String name, String text) {
        Allure.addAttachment(name, "text/plain", text);
    }
}
