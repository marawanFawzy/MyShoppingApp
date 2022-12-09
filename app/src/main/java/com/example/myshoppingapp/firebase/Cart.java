package com.example.myshoppingapp.firebase;

import java.util.ArrayList;

public class Cart {
    private String id, customerId;
    private ArrayList<String> productsQuantity;
    private ArrayList<String> products;
    private ArrayList<String> names;
    private ArrayList<String> prices;

    public Cart() {
    }

    public Cart(String id, String customerId, ArrayList<String> productsQuantity, ArrayList<String> products, ArrayList<String> names, ArrayList<String> prices) {
        this.id = id;
        this.customerId = customerId;
        this.productsQuantity = productsQuantity;
        this.products = products;
        this.names = names;
        this.prices = prices;
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

    public ArrayList<String> getProductsQuantity() {
        return productsQuantity;
    }

    public void setProductsQuantity(ArrayList<String> productsQuantity) {
        this.productsQuantity = productsQuantity;
    }

    public ArrayList<String> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<String> products) {
        this.products = products;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    public ArrayList<String> getPrices() {
        return prices;
    }

    public void setPrices(ArrayList<String> prices) {
        this.prices = prices;
    }
}
