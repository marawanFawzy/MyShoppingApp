package com.example.myshoppingapp;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myshoppingapp.firebase.Cart;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class chart extends AppCompatActivity {
    BarChart barChart;
    List<String> xAxisValues;
    ArrayList<BarEntry> v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        barChart = findViewById(R.id.barChart);
        xAxisValues = new ArrayList<>();
        v = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Cart")
                .whereEqualTo("customerId", "finished Order")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.getDocuments().size() == 0)
                        Toast.makeText(chart.this, "no orders", Toast.LENGTH_SHORT).show();
                    else {
                        for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                            Cart temp = queryDocumentSnapshots.getDocuments().get(i).toObject(Cart.class);
                            for (int j = 0; j < temp.getNames().size(); j++) {
                                int ret = xAxisValues.indexOf(temp.getNames().get(j));
                                if (ret == -1) {
                                    xAxisValues.add(temp.getNames().get(j));
                                    v.add(new BarEntry(xAxisValues.size() - 1, Float.parseFloat(temp.getProductsQuantity().get(j))));
                                } else {
                                    v.get(ret).setY(v.get(ret).getY() + Float.parseFloat(temp.getProductsQuantity().get(j)));
                                }
                            }

                        }
                    }
                    BarDataSet barDataSet = new BarDataSet(v, "test ");
                    barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    barDataSet.setValueTextColor(Color.BLACK);
                    barDataSet.setValueTextSize(16f);
                    BarData barData = new BarData(barDataSet);
                    barChart.setFitBars(true);
                    barChart.setData(barData);
                    barChart.getDescription().setText("test bar");
                    barChart.animateY(2000);
                    barChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));
                });

    }
}