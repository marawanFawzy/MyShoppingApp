package com.example.myshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myshoppingapp.firebase.Orders;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class Current_Orders extends AppCompatActivity {
    String userId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayAdapter<String> OrderArrayAdapter;
    private final ArrayList<String> ordersId = new ArrayList<>();
    private final ArrayList<Boolean> ordersDelivered = new ArrayList<>();
    ListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_orders);
        Intent ii = getIntent();
        userId = ii.getStringExtra("userId");
        getAllOrders();
        myList = findViewById(R.id.Orders_listview);
        OrderArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        myList.setAdapter(OrderArrayAdapter);
        myList.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(Current_Orders.this, Edit_Order.class);
            i.putExtra("orderId", ordersId.get(position));
            i.putExtra("Delivered", ordersDelivered.get(position));
            startActivity(i);
        });
    }

    void getAllOrders() {
        db.collection("Orders").whereEqualTo("customer_id", userId)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.size() == 0) {
                        Toast.makeText(Current_Orders.this, "no Orders", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                            Orders temp = queryDocumentSnapshots.getDocuments().get(i).toObject(Orders.class);
                            if(temp.getCart().getProducts().size() != 0) {
                                Date now = new Date();
                                Date deliver_date = new Date((long) (temp.getOrder_date().getTime() + (temp.getEstimatedTime() * 1000 * 24 * 60 * 60)));
                                String entry = temp.getCart().getProducts().size() + " Product(s) " + " | " + (now.before(deliver_date) ? "preparing" : "delivered");
                                OrderArrayAdapter.add(entry);
                                ordersId.add(temp.getId());
                                ordersDelivered.add(!now.before(deliver_date));
                            }
                        }
                    }
                });
    }
}