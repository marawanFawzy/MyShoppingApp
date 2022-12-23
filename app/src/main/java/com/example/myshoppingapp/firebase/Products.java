package com.example.myshoppingapp.firebase;

import java.util.ArrayList;

public class Products implements Cloneable {
    private int Quantity;
    private double Price;
    private String name, id, catId;
    private String photo;
    private String Description;
    private ArrayList<String> Feedback;
    private double Days_For_Delivery;
    private int Time_for_Delivery_in_Seconds;
    private double discount = 0;


    public Products() {
    }

    public Products(Products p) {
        this.id = p.id;
        this.Quantity = p.Quantity;
        this.catId = p.catId;
        this.Price = p.Price;
        this.name = p.name;
        this.photo = p.photo;
        this.Feedback = p.Feedback;
        this.Description = p.Description;
        this.Days_For_Delivery = p.Days_For_Delivery;
    }

    public Products(String id, int quantity, String catId, double price, String name, String photo, String Description, ArrayList<String> Feedback, double Days_For_Delivery) {
        this.id = id;
        this.Quantity = quantity;
        this.catId = catId;
        this.Price = price;
        this.name = name;
        this.photo = photo;
        this.Feedback = Feedback;
        this.Description = Description;
        this.Days_For_Delivery = Days_For_Delivery;
    }

    public Products(String id, int Quantity, String catId, int Price, String name, String photo) {
        this.id = id;
        this.Quantity = Quantity;
        this.catId = catId;
        this.Price = Price;
        this.name = name;
        this.photo = photo;
    }


    public double getDays_For_Delivery() {
        return Days_For_Delivery;
    }

    public int getTime_for_Delivery_in_Seconds() {
        return Time_for_Delivery_in_Seconds;
    }

    public void setDays_For_Delivery(double days_For_Delivery) {
        Days_For_Delivery = days_For_Delivery;
        this.Time_for_Delivery_in_Seconds = (int) (days_For_Delivery * 24 * 60 * 60);
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public ArrayList<String> getFeedback() {
        return Feedback;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setFeedback(ArrayList<String> feedback) {
        Feedback = feedback;
    }

    @Override
    public Products clone() {
        try {
            return (Products) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
