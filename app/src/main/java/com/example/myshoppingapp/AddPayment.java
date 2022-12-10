package com.example.myshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddPayment extends AppCompatActivity {
    EditText CardNumber ,ExpireDateMonth,ExpireDateYear , CVV;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);
        Intent ii = getIntent();
        userId = ii.getStringExtra("userId");
    }
}