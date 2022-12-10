package com.example.myshoppingapp.firebase;

public class CreditCard {
    private String number , expireDate , CCV;
    private boolean validated ;
    private double amount;
    public CreditCard(){

    }
    public CreditCard(String number, String expireDate, String CCV) {
        this.number = number;
        this.expireDate = expireDate;
        this.CCV = CCV;
        this.validated = false;
        this.amount = 1000;
    }

    public CreditCard(String number, String expireDate, String CCV, boolean validated, double amount) {
        this.number = number;
        this.expireDate = expireDate;
        this.CCV = CCV;
        this.validated = validated;
        this.amount = amount;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getCCV() {
        return CCV;
    }

    public void setCCV(String CCV) {
        this.CCV = CCV;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
