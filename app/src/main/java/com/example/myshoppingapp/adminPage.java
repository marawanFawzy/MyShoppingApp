package com.example.myshoppingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class adminPage extends AppCompatActivity {
    CardView btnChart, newProduct, newCategory,
            buttonDeleteCat, EditCat, deleteProd,
            editProd, report , DeleteUser, CheckCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        btnChart = findViewById(R.id.buttonChart);
        newProduct = findViewById(R.id.addNewProduct);
        newCategory = findViewById(R.id.addNewCategory);
        buttonDeleteCat = findViewById(R.id.DeleteCategory);
        EditCat = findViewById(R.id.EditCategory);
        deleteProd = findViewById(R.id.DeleteProduct);
        editProd = findViewById(R.id.EditProduct);
        report = findViewById(R.id.reports);
        DeleteUser = findViewById(R.id.DeleteUser);
        CheckCredit = findViewById(R.id.CheckCredit);
        btnChart.setOnClickListener(v -> {
            Intent chartIntent = new Intent(adminPage.this, chart.class);
            startActivity(chartIntent);
        });
        newProduct.setOnClickListener(v -> {
            Intent chartIntent = new Intent(adminPage.this, AddNewProduct.class);
            startActivity(chartIntent);
        });
        newCategory.setOnClickListener(v -> {
            Intent chartIntent = new Intent(adminPage.this, AddNewCategory.class);
            startActivity(chartIntent);
        });
        buttonDeleteCat.setOnClickListener(v -> {
            Intent chartIntent = new Intent(adminPage.this, deleteCat.class);
            startActivity(chartIntent);
        });
        EditCat.setOnClickListener(v -> {
            Intent chartIntent = new Intent(adminPage.this, EditCat.class);
            startActivity(chartIntent);
        });
        deleteProd.setOnClickListener(v -> {
            Intent chartIntent = new Intent(adminPage.this, deleteProduct.class);
            startActivity(chartIntent);
        });
        editProd.setOnClickListener(v -> {
            Intent chartIntent = new Intent(adminPage.this, editProduct.class);
            startActivity(chartIntent);
        });
        report.setOnClickListener(v -> {
            Intent chartIntent = new Intent(adminPage.this, reportsActivity.class);
            startActivity(chartIntent);
        });
        CheckCredit.setOnClickListener(v -> {
            Intent chartIntent = new Intent(adminPage.this, CheckCreditCard.class);
            startActivity(chartIntent);
        });
        DeleteUser.setOnClickListener(v -> {
            Intent chartIntent = new Intent(adminPage.this, DeleteUser.class);
            startActivity(chartIntent);
        });

    }
}