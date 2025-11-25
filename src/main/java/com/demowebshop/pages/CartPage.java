package com.demowebshop.pages;

import com.demowebshop.utils.ElementHelper;
import org.openqa.selenium.By;

public class CartPage extends BasePage {
    private final By terms = By.id("termsofservice");
    private final By checkout = By.id("checkout");
    private final By qtyField = By.cssSelector("input.qty-input");
    private final By updateBtn = By.name("updatecart");

    public CartPage(ElementHelper helper) {
        super(helper);
    }

    public void updateQuantity(String qty) {
        type(qtyField, qty);
        click(updateBtn);
    }

    public void acceptTermsAndCheckout() {
        click(terms);
        click(checkout);
    }
}