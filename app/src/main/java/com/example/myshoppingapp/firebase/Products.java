package com.example.myshoppingapp.firebase;

public class Products {
    private int Quantity;
    private double Price;
    private String name, id, catId;
    private String photo;

    public Products() {
    }

    public Products(String id, int quantity, String catId, double price, String name ,String photo) {
        this.id = id;
        Quantity = quantity;
        this.catId = catId;
        Price = price;
        this.name = name;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
