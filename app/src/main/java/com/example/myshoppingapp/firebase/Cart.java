package com.example.myshoppingapp.firebase;

import java.util.ArrayList;
public class Cart {
    private String id, customerId;
    private ArrayList<Products> products;

    public Cart() {
    }

    public Cart(String id, String customerId, ArrayList<Products> products) {
        this.id = id;
        this.customerId = customerId;
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public ArrayList<Products> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Products> products) {
        this.products = products;
    }
}