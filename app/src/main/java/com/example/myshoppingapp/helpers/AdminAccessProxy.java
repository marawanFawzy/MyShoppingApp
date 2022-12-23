package com.example.myshoppingapp.helpers;

import com.example.myshoppingapp.firebase.Admin;
import com.example.myshoppingapp.firebase.Customers;

public class AdminAccessProxy implements AdminAccess {
    @Override
    public boolean AdminRouter(Customers temp) {

        if (temp.isFlag()) {
            Admin.getAdmin().setUsername(temp.getUsername());
            return true;
        } else {
            return false;
        }
    }
}
