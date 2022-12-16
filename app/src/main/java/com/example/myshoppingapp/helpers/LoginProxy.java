package com.example.myshoppingapp.helpers;

import com.example.myshoppingapp.firebase.Customers;

public class LoginProxy implements Login {

    @Override
    public boolean generateAccess(Customers temp , String Password) {
        if(!temp.getPassword().equals(Password))
            return false;
        return temp.isStatus();
    }
}
