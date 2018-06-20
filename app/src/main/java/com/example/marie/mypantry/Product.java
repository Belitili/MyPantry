package com.example.marie.mypantry;

public class Product {

    private String productName;

    private String amount;

    private String location;

    Product(String productName, String productAmount, String location) {
        this.setProductName(productName);
        this.setAmount(productAmount);
        this.setLocation(location);
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProductName() {
        return productName;
    }

    public String getAmount() {
        return amount;
    }

    public String getLocation() {
        return location;
    }
}
