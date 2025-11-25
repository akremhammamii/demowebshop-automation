package com.demowebshop.utils;

import java.util.Random;
import java.util.UUID;

public class RandomUtil {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";

    public static String randomEmail() {
        return "user_" + UUID.randomUUID().toString().substring(0, 8) + "@test.com";
    }

    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i=0;i<length;i++) sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        return sb.toString();
    }

    public static String randomNumber(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i=0;i<length;i++) sb.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        return sb.toString();
    }

    public static String randomPassword() {
        return randomString(6) + randomNumber(3);
    }
}