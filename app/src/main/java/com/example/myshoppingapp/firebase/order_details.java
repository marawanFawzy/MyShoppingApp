package com.example.myshoppingapp.firebase;

public class order_details {
    private int order_id  , prod_ID , cat_id , quantity;
    public order_details(){}

    public order_details(int order_id, int prod_ID, int cat_id, int quantity) {
        this.order_id = order_id;
        this.prod_ID = prod_ID;
        this.cat_id = cat_id;
        this.quantity = quantity;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getProd_ID() {
        return prod_ID;
    }

    public void setProd_ID(int prod_ID) {
        this.prod_ID = prod_ID;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
