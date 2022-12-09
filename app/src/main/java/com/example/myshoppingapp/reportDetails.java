package com.example.myshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myshoppingapp.firebase.Cart;
import com.example.myshoppingapp.firebase.Orders;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class reportDetails extends AppCompatActivity {
    ListView myList;
    ArrayList<ProductClass> arrayOfProducts;
    ArrayList<String> ids, NamesArray, quantityArray, PricesArray;
    TextView name, date , totalText;
    CustomAdapter adapter;
    String orderId;
    RatingBar rate;
    EditText FeedbackView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);
        name = findViewById(R.id.nameText);
        date = findViewById(R.id.dateText);
        FeedbackView = findViewById(R.id.FeedbackView);
        totalText = findViewById(R.id.TotalText);
        Intent ii = getIntent();
        orderId = ii.getStringExtra("orderId");
        myList = findViewById(R.id.OrderList);
        rate = findViewById(R.id.RatingBarView);
        rate.setEnabled(false);
        ids = new ArrayList<>();
        NamesArray = new ArrayList<>();
        PricesArray = new ArrayList<>();
        quantityArray = new ArrayList<>();
        getOrder(orderId , NamesArray , PricesArray , quantityArray , ids);
    }

    public void InsertIntoAdapter(String userId, ArrayList<String> ids, ArrayList<String> namesArray, ArrayList<String> pricesArray, ArrayList<String> quantityArray) {
        ProductClass product;
        arrayOfProducts = new ArrayList<>();
        for (int i = 0; i < namesArray.size(); i++) {
            String id = ids.get(i);
            String name = namesArray.get(i);
            String price = pricesArray.get(i);
            String quantity = quantityArray.get(i);
            product = new ProductClass(userId, id, name, price, quantity);
            arrayOfProducts.add(product);
        }
        adapter = new CustomAdapter(this, 0, arrayOfProducts , true);
        myList.setAdapter(adapter);
    }

    void getOrder(String orderId, ArrayList<String> NamesArray, ArrayList<String> PricesArray, ArrayList<String> quantityArray, ArrayList<String> ids) {
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
                        totalText.setText(String.valueOf(order.getTotal()) + " EGP");
                        name.setText(order.getName());
                        date.setText(String.valueOf(order.getOrder_date()));
                        FeedbackView.setText(order.getFeedback());
                        temp = order.getCart();
                        NamesArray.addAll(temp.getNames());
                        PricesArray.addAll(temp.getPrices());
                        quantityArray.addAll(temp.getProductsQuantity());
                        ids.addAll(temp.getProducts());
                    }
                    InsertIntoAdapter(temp.getCustomerId(), ids, NamesArray, PricesArray, quantityArray);
                });
    }
}