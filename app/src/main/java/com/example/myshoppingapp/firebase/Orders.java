package com.example.myshoppingapp.firebase;

import java.util.Date;

public class Orders implements Cloneable {

    private String id, Customer_id;
    private Date Order_date;
    private double Latitude, Longitude, total;
    private String name, feedback, paymentMethod;
    private float rating;
    private Cart cart;
    private double estimatedTime;

    private Orders() {

    }

    private Orders(OrderBuilder orderBuilder) {
        this.id = orderBuilder.id;
        this.Customer_id = orderBuilder.Customer_id;
        this.Order_date = orderBuilder.Order_date;
        this.Latitude = orderBuilder.Latitude;
        this.Longitude = orderBuilder.Longitude;
        this.total = orderBuilder.total;
        this.name = orderBuilder.name;
        this.rating = orderBuilder.rating;
        this.cart = orderBuilder.cart;
        this.estimatedTime = orderBuilder.estimatedTime;
        this.feedback = orderBuilder.feedback;
        this.paymentMethod = orderBuilder.paymentMethod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_id() {
        return Customer_id;
    }

    public void setCustomer_id(String customer_id) {
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public double getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(double estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    @Override
    public Orders clone() {
        try {
            Orders clone = (Orders) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static class OrderBuilder {

        private String id, Customer_id, name, feedback, paymentMethod;
        private Date Order_date;
        private double Latitude, Longitude, total;
        private float rating;
        private Cart cart;
        private double estimatedTime;

        public OrderBuilder() {
        }

        public OrderBuilder buildId(String id) {
            this.id = id;
            return this;
        }

        public OrderBuilder buildCustomer_id(String Customer_id) {
            this.Customer_id = Customer_id;
            return this;
        }

        public OrderBuilder buildOrder_date(Date Order_date) {
            this.Order_date = Order_date;
            return this;
        }

        public OrderBuilder buildLatitude(double Latitude) {
            this.Latitude = Latitude;
            return this;
        }

        public OrderBuilder buildLongitude(double Longitude) {
            this.Longitude = Longitude;
            return this;
        }

        public OrderBuilder buildTotal(double total) {
            this.total = total;
            return this;
        }

        public OrderBuilder buildName(String name) {
            this.name = name;
            return this;
        }

        public OrderBuilder buildFeedback(String feedback) {
            this.feedback = feedback;
            return this;
        }

        public OrderBuilder buildPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public OrderBuilder buildRating(float rating) {
            this.rating = rating;
            return this;
        }

        public OrderBuilder buildCart(Cart cart) {
            this.cart = cart;
            return this;
        }

        public OrderBuilder buildEstimatedTime(double estimatedTime) {
            this.estimatedTime = estimatedTime;
            return this;
        }

        public Orders build() {
            return new Orders(this);
        }

    }
}
