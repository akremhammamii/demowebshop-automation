package com.demowebshop.config;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigurationManager {
    private static final Properties props = new Properties();
    static {
        try (FileInputStream fis = new FileInputStream("src/main/java/com/demowebshop/resources/config.properties")) {
            props.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(props.getProperty(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static boolean getBool(String key, boolean defaultValue) {
        try {
            return Boolean.parseBoolean(props.getProperty(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
