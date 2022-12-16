package com.example.myshoppingapp.helpers;

import com.example.myshoppingapp.firebase.CreditCard;

public class ProxyCheck implements Payment {
    private CreditCard c1;
    private int Cvv;

    public ProxyCheck(int Cvv, CreditCard c1) {
        this.Cvv = Cvv;
        this.c1 = c1;
    }

    @Override
    public boolean withdraw(double amount) {
        if (Cvv == c1.getCVV()) {
            c1.withdraw(amount);
            return true;
        } else {
            return false;
        }
    }


}
