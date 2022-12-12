package com.example.myshoppingapp;

public class ProductClass {

    public String customerId;
    public String name;
    public String id;
    public String quantity;
    public String price;
    public String image;
    public double time;

    public ProductClass(String customerId, String id, String name, String price, String quantity , String image) {
        this.customerId = customerId;
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
    }
    public ProductClass(String customerId, String id, String name, String price, String quantity , String image ,double time) {
        this.customerId = customerId;
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
        this.time = time;
    }
}
