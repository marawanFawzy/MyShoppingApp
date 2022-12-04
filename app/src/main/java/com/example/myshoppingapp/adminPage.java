package com.example.myshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class adminPage extends AppCompatActivity {
    Button btnChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        btnChart = findViewById(R.id.button);
        btnChart.setOnClickListener(v -> {
            //TODO add extras or create them in chart page directly
            Toast.makeText(adminPage.this, "starting the chart", Toast.LENGTH_LONG).show();
            Intent chartIntent = new Intent(adminPage.this, chart.class);
            startActivity(chartIntent);
        });
    }
}