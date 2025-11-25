package com.demowebshop.pages;

import com.demowebshop.utils.ElementHelper;
import org.openqa.selenium.By;

public class CheckoutPage extends BasePage {
    private final By continueBtn = By.cssSelector("input.button-1.new-address-next-step-button");
    private final By placeOrderBtn = By.cssSelector("button.button-1.order-completed");

    public CheckoutPage(ElementHelper helper) {
        super(helper);
    }

    public void proceedBilling() {
        click(continueBtn);
    }

    public void placeOrder() {
        click(placeOrderBtn);
    }
}
