package com.demowebshop.pages;

import com.demowebshop.utils.ElementHelper;

public class PageManager {
    private final ElementHelper helper;

    public PageManager(ElementHelper helper) {
        this.helper = helper;
    }

    public HomePage home() {
        return new HomePage(helper);
    }

    public LoginPage login() {
        return new LoginPage(helper);
    }

    public RegisterPage register() {
        return new RegisterPage(helper);
    }

    public ProductPage product() {
        return new ProductPage(helper);
    }

    public CartPage cart() {
        return new CartPage(helper);
    }

    public CheckoutPage checkout() {
        return new CheckoutPage(helper);
    }
}
