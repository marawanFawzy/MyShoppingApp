package com.example.myshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myshoppingapp.firebase.Cart;
import com.example.myshoppingapp.firebase.Products;
import com.example.myshoppingapp.firebase.order_details;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ProductsDetails extends AppCompatActivity {
    EditText e_name, e_price, e_qty;
    Button add, cart, home;
    String Prod_id, cat_id ,userId;

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
        //cat_id = ii.getStringExtra("cat_id");
        userId = ii.getStringExtra("userId");
        getProduct(Prod_id, cat_id);

        if (e_qty.getText().toString().equals("0")) {
            add.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Sorry, The quantity carried out", Toast.LENGTH_SHORT).show();
        } else {
            add.setOnClickListener(v -> {
                add.setEnabled(true);
                //TODO ADD TO CART
                AddCart(Prod_id);
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
        db.collection("Products")
                .whereEqualTo("id", Prod_id)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
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
    void AddCart(String Prod_id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Cart").whereEqualTo("customerId", userId).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if(queryDocumentSnapshots.getDocuments().size() ==0)
            {
                String orderId = db.collection("order_details").document().getId().substring(0,5);
                String id = db.collection("Cart").document().getId().substring(0,5);
                ArrayList<String> ids = new ArrayList<>();
                ArrayList<String> prodids = new ArrayList<>();
                ids.add(orderId);
                prodids.add(Prod_id);
                Cart newTemp = new Cart(id, userId  , ids , prodids);
                db.collection("Cart").document(id).set(newTemp).addOnSuccessListener(unused -> {
                    order_details newOrder = new order_details(orderId,id ,Prod_id , "1");
                    db.collection("order_details").document(orderId).set(newOrder).addOnSuccessListener(unused1 -> {
                        Toast.makeText(this, "added product to new cart", Toast.LENGTH_SHORT).show();
                    });
                });
            }
            else
            {
                DocumentSnapshot d = queryDocumentSnapshots.getDocuments().get(0);
                Cart temp = d.toObject(Cart.class);
                for(String s: temp.getProducts())
                {
                    if (s.equals(Prod_id))
                    {
                        Toast.makeText(this, "this product is already added", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                String orderId = db.collection("order_details").document().getId().substring(0,5);
                temp.getOrdDetId().add(orderId);
                temp.getProducts().add(Prod_id);
                order_details newOrder = new order_details(orderId,temp.getId() ,Prod_id , "1");
                db.collection("order_details").document(orderId).set(newOrder).addOnSuccessListener(unused -> {
                    db.collection("Cart").document(temp.getId()).set(temp).addOnSuccessListener(unused1 -> {
                        Toast.makeText(this, "added this product to existing cart", Toast.LENGTH_SHORT).show();
                    });
                });

            }
        });

    }
}
