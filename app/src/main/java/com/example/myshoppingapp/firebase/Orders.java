package com.example.myshoppingapp.firebase;

import java.util.Date;

public class Orders {
    private int id , Customer_id;
    private Date Order_date;
    private double Latitude , Longitude;
    private String name;

    public Orders() {}

    public Orders(int id, int customer_id, Date order_date, double latitude, double longitude, String name) {
        this.id = id;
        Customer_id = customer_id;
        Order_date = order_date;
        Latitude = latitude;
        Longitude = longitude;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer_id() {
        return Customer_id;
    }

    public void setCustomer_id(int customer_id) {
        Customer_id = customer_id;
    }

    public Date getOrder_date() {
        return Order_date;
    }

    public void setOrder_date(Date order_date) {
        Order_date = order_date;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
