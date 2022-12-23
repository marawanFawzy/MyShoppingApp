package com.example.myshoppingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myshoppingapp.firebase.Cart;
import com.example.myshoppingapp.firebase.Orders;
import com.example.myshoppingapp.firebase.Products;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class reportDetails extends AppCompatActivity {
    ListView myList;
    ArrayList<Products> arrayOfProducts;
    TextView name, date, totalText;
    CustomAdapter adapter;
    String orderId, userId;
    RatingBar rate;
    EditText FeedbackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);
        name = findViewById(R.id.nameText);
        date = findViewById(R.id.dateText);
        arrayOfProducts = new ArrayList<>();
        FeedbackView = findViewById(R.id.FeedbackView);
        totalText = findViewById(R.id.TotalText);
        Intent ii = getIntent();
        orderId = ii.getStringExtra("orderId");
        myList = findViewById(R.id.OrderList);
        rate = findViewById(R.id.RatingBarView);
        rate.setEnabled(false);
        getOrder(orderId, arrayOfProducts);
    }

    public void InsertIntoAdapter(String userId) {
        adapter = new CustomAdapter(this, 0, arrayOfProducts, true, true, userId, "Cart");
        myList.setAdapter(adapter);
    }

    @SuppressLint("SetTextI18n")
    void getOrder(String orderId, ArrayList<Products> products) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Orders")
                .whereEqualTo("id", orderId)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    Cart temp;
                    if (queryDocumentSnapshots.getDocuments().size() == 0) {
                        Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        DocumentSnapshot d = queryDocumentSnapshots.getDocuments().get(0);
                        Orders order = d.toObject(Orders.class);
                        rate.setRating(order.getRating());
                        totalText.setText(order.getTotal() + " EGP");
                        name.setText(order.getName());
                        date.setText(String.valueOf(order.getOrder_date()));
                        FeedbackView.setText(order.getFeedback());
                        temp = order.getCart();
                        userId = temp.getCustomerId();
                        products.addAll(temp.getProducts());
                    }
                    InsertIntoAdapter(temp.getCustomerId());
                });
    }
}