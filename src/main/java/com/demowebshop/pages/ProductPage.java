package com.demowebshop.pages;

import com.demowebshop.utils.ElementHelper;
import org.openqa.selenium.By;

public class ProductPage extends BasePage {
    private final By addToCartBtn = By.cssSelector("button[id^='add-to-cart-button']");
    private final By title = By.cssSelector("div.product-name h1");

    public ProductPage(ElementHelper helper) {
        super(helper);
    }

    public void addToCart() {
        click(addToCartBtn);
    }

    public String getTitle() {
        return getText(title);
    }
}
