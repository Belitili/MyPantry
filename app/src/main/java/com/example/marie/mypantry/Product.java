package com.example.marie.mypantry;

public class Product {

    private String productName;

    private String amount;

    Product(String productName, String productAmount) {
        this.setProductName(productName);
        this.setAmount(productAmount);
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public String getAmount() {
        return amount;
    }
}
