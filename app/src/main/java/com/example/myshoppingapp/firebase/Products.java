package com.example.myshoppingapp.firebase;

public class Products {
    private int id , Quantity , CatId;
    private double Price;
    private String name;
    public Products(){}

    public Products(int id, int quantity, int catId, double price, String name) {
        this.id = id;
        Quantity = quantity;
        CatId = catId;
        Price = price;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getCatId() {
        return CatId;
    }

    public void setCatId(int catId) {
        CatId = catId;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
