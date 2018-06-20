package com.example.marie.mypantry;

public class Product {

    private String productName;

    private double amount;

    private String amountUnit;

    private String location;

    Product(String productName, double productAmount, String amountUnit, String location) {
        this.setProductName(productName);
        this.setAmount(productAmount);
        this.setAmountUnit(amountUnit);
        this.setLocation(location);
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setAmountUnit(String unit) {
        this.amountUnit = unit;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProductName() {
        return productName;
    }

    public double getAmount() {
        return amount;
    }

    public String getAmountUnit() { return amountUnit; }

    public String getLocation() {
        return location;
    }
}
