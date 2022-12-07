package com.example.myshoppingapp.firebase;

public class order_details {
    private String id ,cart_id  , prod_ID , cat_id , quantity;
    public order_details(){}

    public order_details(String id, String cart_id, String prod_ID, String cat_id, String quantity) {
        this.id = id;
        this.cart_id = cart_id;
        this.prod_ID = prod_ID;
        this.cat_id = cat_id;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getProd_ID() {
        return prod_ID;
    }

    public void setProd_ID(String prod_ID) {
        this.prod_ID = prod_ID;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
