package com.example.bookmanager.pojo;

import androidx.annotation.NonNull;

public class Product {

    private String id;
    private String name;
    private int quantity;
    private double unitPrice;
    private String userId;

    public Product() {
    }

    public Product(String name, int quantity, double unitPrice, String userId) {
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @NonNull
    @Override
    public String toString() {
        return "\nProduct Name: " + name +
                "\nProduct quantity: " + quantity +
                "\nProduct Unit Price:" + unitPrice + "\n";
    }
}
