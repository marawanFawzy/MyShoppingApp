package com.example.myshoppingapp.firebase;

import com.example.myshoppingapp.helpers.Payment;

public class CreditCard implements Payment {
    private String number, expireDateMonth, expireDateYear;
    private int CVV;
    private String status;
    private double amount = 1000;

    public CreditCard() {

    }

    public CreditCard(String number, String expireDateMonth, String expireDateYear, int CVV, String status) {
        this.number = number;
        this.expireDateMonth = expireDateMonth;
        this.expireDateYear = expireDateYear;
        this.CVV = CVV;
        this.status = status;
    }

    public CreditCard(String number, String expireDateMonth, String expireDateYear, int CVV) {
        this.number = number;
        this.expireDateMonth = expireDateMonth;
        this.expireDateYear = expireDateYear;
        this.CVV = CVV;
        status = "waiting";
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCVV() {
        return CVV;
    }

    public void setCVV(int CVV) {
        this.CVV = CVV;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getExpireDateMonth() {
        return expireDateMonth;
    }

    public void setExpireDateMonth(String expireDateMonth) {
        this.expireDateMonth = expireDateMonth;
    }

    public String getExpireDateYear() {
        return expireDateYear;
    }

    public void setExpireDateYear(String expireDateYear) {
        this.expireDateYear = expireDateYear;
    }

    @Override
    public boolean withdraw(double amount) {
        return true;
    }
}
