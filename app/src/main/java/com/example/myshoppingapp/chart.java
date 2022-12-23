package com.example.myshoppingapp;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myshoppingapp.firebase.Cart;
import com.example.myshoppingapp.firebase.Orders;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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
        final int[] Colors = {
                Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140),
                Color.rgb(140, 234, 255), Color.rgb(255, 140, 157), Color.rgb(193, 37, 82),
                Color.rgb(255, 102, 0), Color.rgb(245, 199, 0), Color.rgb(106, 150, 31),
                Color.rgb(179, 100, 53), Color.rgb(64, 89, 128), Color.rgb(149, 165, 124)
        };
        barChart = findViewById(R.id.barChart);
        xAxisValues = new ArrayList<>();
        v = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Orders")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.getDocuments().size() == 0)
                        Toast.makeText(chart.this, "no orders", Toast.LENGTH_SHORT).show();
                    else {
                        for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                            Orders order = queryDocumentSnapshots.getDocuments().get(i).toObject(Orders.class);
                            Cart temp = order.getCart();
                            for (int j = 0; j < temp.getProducts().size(); j++) {
                                int ret = xAxisValues.indexOf(temp.getProducts().get(j).getName());
                                if (ret == -1) {
                                    xAxisValues.add(temp.getProducts().get(j).getName());
                                    v.add(new BarEntry(xAxisValues.size() - 1, temp.getProducts().get(j).getQuantity()));
                                } else {
                                    v.get(ret).setY(v.get(ret).getY() + temp.getProducts().get(j).getQuantity());
                                }
                            }

                        }
                    }
                    BarDataSet barDataSet = new BarDataSet(v, "products sold ");
                    barDataSet.setColors(Colors);
                    barDataSet.setValueTextColor(Color.BLACK);
                    barDataSet.setValueTextSize(16f);
                    BarData barData = new BarData(barDataSet);
                    barChart.setFitBars(true);
                    barChart.setData(barData);
                    barChart.getDescription().setText("products");
                    barChart.animateY(2000);
                    barChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));
                });

    }
}