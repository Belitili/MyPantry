package com.example.marie.mypantry;

import java.sql.Date;

public class Product {

    private String productName;

    private double amount;

    private String amountUnit;

    private String location;

    private String secondary_location;

    Product(String productName, double productAmount, String amountUnit, String location, String secondary_location) {
        this.setProductName(productName);
        this.setAmount(productAmount);
        this.setAmountUnit(amountUnit);
        this.setLocation(location);
        this.setSecondary_location(secondary_location);
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

    public void setSecondary_location(String secondary_location) { this.secondary_location = secondary_location; }

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

    public String getSecondary_location() {
        return secondary_location;
    }
}
