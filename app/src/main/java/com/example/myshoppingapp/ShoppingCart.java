package com.example.myshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myshoppingapp.firebase.Cart;
import com.example.myshoppingapp.firebase.Products;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class ShoppingCart extends AppCompatActivity {
    ListView myList;
    ArrayList<Products> arrayOfProducts = new ArrayList<>();
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
        getCart(userId, arrayOfProducts);
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
        total = 0;
        Time = 0;
        getCart(userId, arrayOfProducts);
    }

    public void InsertIntoAdapter(String userId, ArrayList<Products> products) {
        for (int i = 0; i < products.size(); i++) {
            total += products.get(i).getPrice() * products.get(i).getQuantity() - (products.get(i).getPrice() * products.get(i).getQuantity() * products.get(i).getDiscount() / 100);
            if (products.get(i).getDays_For_Delivery() > Time)
                Time = products.get(i).getDays_For_Delivery();
        }
        adapter = new CustomAdapter(this, 0, arrayOfProducts, false, false, userId, "Cart");
        adapter.total = total;
        adapter.time = Time;
        myList.setAdapter(adapter);
    }

    void getCart(String userId, ArrayList<Products> products) {
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
                        products.addAll(temp.getProducts());
                    }
                    InsertIntoAdapter(userId, products);
                });
    }

}
