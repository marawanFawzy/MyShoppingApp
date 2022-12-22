package com.example.myshoppingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myshoppingapp.firebase.Orders;
import com.example.myshoppingapp.firebase.Products;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class Edit_Order extends AppCompatActivity {
    String orderId;
    boolean Delivered;
    ListView myList;
    ArrayList<Products> arrayOfProducts = new ArrayList<>();
    TextView date;
    CustomAdapter adapter;
    Orders cache;
    FloatingActionButton ConfirmEdit;
    RelativeLayout confirm;
    Button totalPrice;
    boolean changes = false ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);
        Intent ii = getIntent();
        orderId = ii.getStringExtra("orderId");
        Delivered = ii.getBooleanExtra("Delivered", false);
        date = findViewById(R.id.dateText);
        totalPrice = findViewById(R.id.totalpricebutton3);
        myList = findViewById(R.id.OrderList);
        getOrder(orderId);
        ConfirmEdit = findViewById(R.id.buttonAddProduct);
        confirm = findViewById(R.id.confirm);
        if(!Delivered) {
            confirm.setVisibility(View.VISIBLE);
            ConfirmEdit.setOnClickListener(v -> {
                changes = true;
                db.collection("Orders").document(orderId).get().addOnSuccessListener(documentSnapshot -> {
                    Orders temp = documentSnapshot.toObject(Orders.class);
                    temp.setEstimatedTime(adapter.time);
                    temp.setOrder_date(new Date());
                    //TODO UPDATE REMAINING QUANTITY
                    db.collection("Orders").document(orderId).set(temp).addOnSuccessListener(unused -> {
                        Toast.makeText(Edit_Order.this, "your order is edited and it will be delivered in " + adapter.time + " days", Toast.LENGTH_SHORT).show();
                        Edit_Order.this.finish();
                    });
                });

            });
        }
        else {
            confirm.setVisibility(View.GONE);
        }
        totalPrice.setOnClickListener(v -> {
            try {
                Toast.makeText(getApplicationContext(), "Total Price is " + adapter.total + "EGP", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(Edit_Order.this, "no products yet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!changes) {
            db.collection("Orders").document(orderId).set(cache).addOnSuccessListener(unused -> {
                Toast.makeText(Edit_Order.this, "your order is not edited", Toast.LENGTH_SHORT).show();
            });
        }
    }
    public void InsertIntoAdapter(String userId) {
        adapter = new CustomAdapter(this, 0, arrayOfProducts, false, Delivered, userId, orderId);
        adapter.time = cache.getEstimatedTime();
        adapter.total = cache.getTotal();
        myList.setAdapter(adapter);
    }

    @SuppressLint("SetTextI18n")
    void getOrder(String orderId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Orders")
                .whereEqualTo("id", orderId)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.getDocuments().size() == 0) {
                        Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        DocumentSnapshot d = queryDocumentSnapshots.getDocuments().get(0);
                        Orders order = d.toObject(Orders.class);
                        date.setText(String.valueOf(order.getOrder_date()));
                        cache = order.clone();
                        for (Products p: cache.getCart().getProducts()) {
                            arrayOfProducts.add(new Products(p));
                        }

                    }
                    InsertIntoAdapter(cache.getCustomer_id());
                });
    }

}