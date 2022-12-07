package com.example.myshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myshoppingapp.firebase.Products;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProductsDetails extends AppCompatActivity {
    EditText e_name, e_price, e_qty;
    Button add, cart, home;
    String Prod_id, cat_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_details);

        e_name = findViewById(R.id.editTextP_Name);
        e_price = findViewById(R.id.editTextPrice);
        e_qty = findViewById(R.id.quantityEditText);
        add = findViewById(R.id.buttonAddtoCart);
        cart = findViewById(R.id.cartbutton);
        home = findViewById(R.id.homebutton);
        Intent ii = getIntent();
        Prod_id = ii.getStringExtra("Prod_id");
        cat_id = ii.getStringExtra("cat_id");
        String userId = ii.getStringExtra("userId");
        getProduct(Prod_id, cat_id);

        if (e_qty.getText().toString().equals("0")) {
            add.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Sorry, The quantity carried out", Toast.LENGTH_SHORT).show();
        } else {
            add.setOnClickListener(v -> {
                add.setEnabled(true);
                //TODO ADD TO CART
                //sdb.addToCart(Prod_id, cat_id, 1);
                Toast.makeText(this, "added to cart", Toast.LENGTH_SHORT).show();
            });
        }

        cart.setOnClickListener(v -> {
            Intent i = new Intent(ProductsDetails.this, ShoppingCart.class);
            i.putExtra("userId" ,userId);
            startActivity(i);
        });

        home.setOnClickListener(v -> {
            Intent i = new Intent(ProductsDetails.this, HomeActivity.class);
            i.putExtra("userId" ,userId);
            startActivity(i);
        });
    }

    void getProduct(String Prod_id, String cat_id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Products").whereEqualTo("id", Prod_id).whereEqualTo("catId", cat_id).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.size() == 0) {
                        Toast.makeText(ProductsDetails.this, "not found", Toast.LENGTH_SHORT).show();
                    } else {
                        for (DocumentSnapshot d : queryDocumentSnapshots) {
                            Products temp = d.toObject(Products.class);
                            if (temp != null) {
                                e_name.setText(temp.getName());
                                e_price.setText(String.format("%sEGP", temp.getPrice()));
                                e_qty.setText(String.valueOf(temp.getQuantity()));
                            }
                        }
                    }
                });
    }
    void AddCart(){
        //TODO fill this function
    }
}
