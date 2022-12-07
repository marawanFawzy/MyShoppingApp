package com.example.myshoppingapp.firebase;

import java.util.ArrayList;

public class Cart {
    private String id  , customerId;
    private ArrayList<String>ordDetId;
    private ArrayList<String>products;
    public Cart(){}
    public Cart(String id ,String customerId , ArrayList<String> ordDetId , ArrayList<String> products)
    {
        this.id = id;
        this.customerId = customerId;
        this.ordDetId = ordDetId;
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

    public ArrayList<String> getOrdDetId() {
        return ordDetId;
    }

    public void setOrdDetId(ArrayList<String> ordDetId) {
        this.ordDetId = ordDetId;
    }

    public ArrayList<String> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<String> products) {
        this.products = products;
    }
}
