package com.example.myshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class adminPage extends AppCompatActivity {
    Button btnChart , newProduct , newCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        btnChart = findViewById(R.id.buttonChart);
        newProduct = findViewById(R.id.addNewProduct);
        newCategory = findViewById(R.id.addNewCategory);
        btnChart.setOnClickListener(v -> {
            //TODO add extras or create them in chart page directly
            Toast.makeText(adminPage.this, "starting the chart", Toast.LENGTH_SHORT).show();
            Intent chartIntent = new Intent(adminPage.this, chart.class);
            startActivity(chartIntent);
        });
        newProduct.setOnClickListener(v->{
            Toast.makeText(adminPage.this, "new Product", Toast.LENGTH_SHORT).show();
            Intent chartIntent = new Intent(adminPage.this, AddNewProduct.class);
            startActivity(chartIntent);
        });
        newCategory.setOnClickListener(v->{
            Toast.makeText(adminPage.this, "new category", Toast.LENGTH_SHORT).show();
            Intent chartIntent = new Intent(adminPage.this, AddNewCategory.class);
            startActivity(chartIntent);
        });
    }
}