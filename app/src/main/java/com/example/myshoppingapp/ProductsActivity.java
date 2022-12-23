package com.example.myshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myshoppingapp.firebase.Products;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity {
    ListView mylist;
    TextView t;
    ImageView cart, home, EditProfile, Orders;
    ArrayList<String> ids = new ArrayList<>();
    ArrayAdapter<String> arr;
    ImageButton payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        payment = findViewById(R.id.Payment);
        t = findViewById(R.id.cat_n_Textview);
        mylist = findViewById(R.id.Products_list);
        cart = findViewById(R.id.cartbutton);
        home = findViewById(R.id.homebutton);
        EditProfile = findViewById(R.id.EditProfile);
        Orders = findViewById(R.id.Orders);
        arr = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mylist.setAdapter(arr);

        String cat_id;
        String cat_name;
        Intent ii = getIntent();
        cat_id = ii.getStringExtra("cat_id");
        cat_name = ii.getStringExtra("cat_name");
        String userId = ii.getStringExtra("userId");
        t.setText(cat_name);
        getAllProducts(cat_id);
        mylist.setOnItemClickListener((parent, view, position, id) -> {
            String pname = ((TextView) view).getText().toString();
            Intent products_Det = new Intent(ProductsActivity.this, ProductsDetails.class);
            products_Det.putExtra("Prod_name", pname);
            products_Det.putExtra("Prod_id", ids.get(position));
            products_Det.putExtra("userId", userId);
            startActivity(products_Det);
        });

        cart.setOnClickListener(v -> {
            Intent i = new Intent(ProductsActivity.this, ShoppingCart.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        Orders.setOnClickListener(v -> {
            Intent i = new Intent(ProductsActivity.this, Current_Orders.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        home.setOnClickListener(v -> {
            Intent i = new Intent(ProductsActivity.this, HomeActivity.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        payment.setOnClickListener(v -> {
            Intent i = new Intent(ProductsActivity.this, AddPayment.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        EditProfile.setOnClickListener(v -> {
            Intent i = new Intent(ProductsActivity.this, ShowProfile.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
    }

    void getAllProducts(String cat_id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Products")
                .whereEqualTo("catId", cat_id)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.size() == 0) {
                        Toast.makeText(ProductsActivity.this, "add a Product First ", Toast.LENGTH_SHORT).show();
                    } else {
                        for (DocumentSnapshot d : queryDocumentSnapshots) {
                            Products temp = d.toObject(Products.class);
                            if (temp != null) {
                                ids.add(temp.getId());
                                arr.add(temp.getName());
                            }
                        }
                    }
                });
    }
}
