package com.example.myshoppingapp;

public class ProductClass {
    public String id;
    public String name;
    public String quantity;
    public String price;
    public String cat_id;

    public ProductClass(String id, String name, String quantity, String price ,String  cat_id) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.cat_id = cat_id;
    }
}
