package com.example.myshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myshoppingapp.firebase.Cart;
import com.example.myshoppingapp.firebase.Products;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class ShoppingCart extends AppCompatActivity {
    ListView myList;
    ArrayList<ProductClass> arrayOfProducts;
    ArrayList<String> ids, NamesArray, quantityArray, PricesArray, times;
    String userId;
    CustomAdapter adapter;
    double total = 0.0, Time;
    Button addNewItem, makeOrder, showPrice;
    ImageButton payment;
    ImageView home, EditProfile, Orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        payment = findViewById(R.id.Payment);
        addNewItem = findViewById(R.id.addnewbutton);
        makeOrder = findViewById(R.id.Orderbutton2);
        showPrice = findViewById(R.id.totalpricebutton3);
        home = findViewById(R.id.homebutton);
        Orders = findViewById(R.id.Orders);
        EditProfile = findViewById(R.id.EditProfile);
        Intent ii = getIntent();
        userId = ii.getStringExtra("userId");
        myList = findViewById(R.id.mylist);
        ids = new ArrayList<>();
        NamesArray = new ArrayList<>();
        PricesArray = new ArrayList<>();
        quantityArray = new ArrayList<>();
        times = new ArrayList<>();
        getCart(userId, NamesArray, PricesArray, quantityArray, ids);
        makeOrder.setOnClickListener(v -> {
            if (myList.getCount() > 0) {
                Intent i = new Intent(ShoppingCart.this, MakeOrder.class);
                i.putExtra("total", adapter.total);
                i.putExtra("time", adapter.time);
                i.putExtra("userId", userId);
                startActivity(i);
            } else
                Toast.makeText(getApplicationContext(), "Shopping Cart Is Empty", Toast.LENGTH_SHORT).show();

        });

        showPrice.setOnClickListener(v -> {
            try {
                Toast.makeText(getApplicationContext(), "Total Price is " + adapter.total + "EGP", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "no products yet", Toast.LENGTH_SHORT).show();
            }
        });

        home.setOnClickListener(v -> {
            Intent i = new Intent(ShoppingCart.this, HomeActivity.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        addNewItem.setOnClickListener(v -> {
            Intent i = new Intent(ShoppingCart.this, HomeActivity.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        payment.setOnClickListener(v -> {
            Intent i = new Intent(ShoppingCart.this, AddPayment.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        EditProfile.setOnClickListener(v -> {
            Intent i = new Intent(ShoppingCart.this, ShowProfile.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        Orders.setOnClickListener(v -> {
            Intent i = new Intent(ShoppingCart.this, Current_Orders.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        arrayOfProducts = new ArrayList<>();
        NamesArray = new ArrayList<>();
        PricesArray = new ArrayList<>();
        quantityArray = new ArrayList<>();
        times = new ArrayList<>();
        ids = new ArrayList<>();
        total = 0;
        Time = 0;
        getCart(userId, NamesArray, PricesArray, quantityArray, ids);
    }

    public void InsertIntoAdapter(String userId, ArrayList<String> ids, ArrayList<String> namesArray, ArrayList<String> pricesArray, ArrayList<String> quantityArray, ArrayList<String> times) {
        arrayOfProducts = new ArrayList<>();
        for (int i = 0; i < namesArray.size(); i++) {
            String id = ids.get(i);
            String name = namesArray.get(i);
            String price = pricesArray.get(i);
            String quantity = quantityArray.get(i);
            String time = times.get(i);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            total += Double.parseDouble(price) * Double.parseDouble(quantity);
            if (Double.parseDouble(time) > Time)
                Time = Double.parseDouble(time);
            int finalI = i;
            db.collection("Products").document(ids.get(i)).get().addOnSuccessListener(documentSnapshot -> {
                ProductClass product;
                String image;
                Products temp = documentSnapshot.toObject(Products.class);
                image = temp.getPhoto();
                product = new ProductClass(userId, id, name, price, quantity, image, Double.parseDouble(time));
                arrayOfProducts.add(product);
                if (finalI == namesArray.size() - 1) {
                    adapter = new CustomAdapter(this, 0, arrayOfProducts, false);
                    adapter.total = total;
                    adapter.time = Time;
                    myList.setAdapter(adapter);
                }
            });

        }
    }

    void getCart(String userId, ArrayList<String> NamesArray, ArrayList<String> PricesArray, ArrayList<String> quantityArray, ArrayList<String> ids) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Cart")
                .whereEqualTo("customerId", userId)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.getDocuments().size() == 0) {
                        Toast.makeText(this, "add any product to your cart first", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        DocumentSnapshot d = queryDocumentSnapshots.getDocuments().get(0);
                        Cart temp = d.toObject(Cart.class);
                        NamesArray.addAll(temp.getNames());
                        PricesArray.addAll(temp.getPrices());
                        quantityArray.addAll(temp.getProductsQuantity());
                        ids.addAll(temp.getProducts());
                        times.addAll(temp.getTimes());
                    }
                    InsertIntoAdapter(userId, ids, NamesArray, PricesArray, quantityArray, times);
                });
    }

}
