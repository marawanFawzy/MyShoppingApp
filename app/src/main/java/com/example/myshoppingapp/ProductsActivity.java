package com.example.myshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myshoppingapp.firebase.Products;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity {
    ListView mylist;
    TextView t;
    Button cart;
    Button home;
    ArrayList<String> ids = new ArrayList<>();
    ArrayAdapter<String> arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        t = findViewById(R.id.cat_n_Textview);
        mylist = findViewById(R.id.Products_list);
        cart = findViewById(R.id.cartbutton);
        home = findViewById(R.id.homebutton);
        arr = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mylist.setAdapter(arr);

        String cat_id;
        String cat_name;
        Intent ii = getIntent();
        cat_id = ii.getStringExtra("cat_id");
        cat_name = ii.getStringExtra("cat_name");
        t.setText(cat_name);
        getAllProducts(cat_id);
        mylist.setOnItemClickListener((parent, view, position, id) -> {
            String pname = ((TextView) view).getText().toString();
            Intent products_Det = new Intent(ProductsActivity.this, ProductsDetails.class);
            products_Det.putExtra("Prod_name", pname);
            products_Det.putExtra("Prod_id", ids.get(position));
            products_Det.putExtra("cat_id", cat_id);
            startActivity(products_Det);
        });

        cart.setOnClickListener(v -> {
            Intent i1 = new Intent(ProductsActivity.this, ShoppingCart.class);
            startActivity(i1);
        });

        home.setOnClickListener(v -> {
            Intent i12 = new Intent(ProductsActivity.this, HomeActivity.class);
            startActivity(i12);
        });
    }
    void getAllProducts(String cat_id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Products")
                .whereEqualTo("catId" , cat_id)
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
