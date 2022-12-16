package com.example.myshoppingapp.helpers;

import com.example.myshoppingapp.firebase.Customers;

public interface Login {
    boolean generateAccess(Customers temp , String Password);
}
