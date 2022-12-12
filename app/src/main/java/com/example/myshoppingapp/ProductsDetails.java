package com.example.myshoppingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myshoppingapp.firebase.Cart;
import com.example.myshoppingapp.firebase.Products;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductsDetails extends AppCompatActivity {
    EditText e_name, e_price, e_qty;
    Button add;
    ImageView cart, home , EditProfile;
    String Prod_id, userId;
    CircleImageView ProductImage;
    ImageButton Payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_details);
        ProductImage = findViewById(R.id.ProductImageDetails);
        Payment = findViewById(R.id.Payment);
        e_name = findViewById(R.id.editTextP_Name);
        e_price = findViewById(R.id.editTextPrice);
        e_qty = findViewById(R.id.quantityEditText);
        add = findViewById(R.id.buttonAddtoCart);
        cart = findViewById(R.id.cartbutton);
        home = findViewById(R.id.homebutton);
        EditProfile = findViewById(R.id.EditProfile);
        Intent ii = getIntent();
        Prod_id = ii.getStringExtra("Prod_id");
        userId = ii.getStringExtra("userId");
        getProduct(Prod_id);

        cart.setOnClickListener(v -> {
            Intent i = new Intent(ProductsDetails.this, ShoppingCart.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });

        home.setOnClickListener(v -> {
            Intent i = new Intent(ProductsDetails.this, HomeActivity.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        Payment.setOnClickListener(v->{
            Intent i = new Intent(ProductsDetails.this, AddPayment.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        EditProfile.setOnClickListener(v->{
            Intent i = new Intent(ProductsDetails.this, ShowProfile.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
    }

    void getProduct(String Prod_id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Products")
                .whereEqualTo("id", Prod_id)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.size() == 0) {
                        Toast.makeText(ProductsDetails.this, "not found", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        for (DocumentSnapshot d : queryDocumentSnapshots) {
                            Products temp = d.toObject(Products.class);
                            if (temp != null) {
                                e_name.setText(temp.getName());
                                e_price.setText(String.valueOf(temp.getPrice()));
                                e_qty.setText(String.valueOf(temp.getQuantity()));
                                ProductImage.setImageBitmap(StringToBitMap(temp.getPhoto()));
                            }
                        }
                    }
                    if (e_qty.getText().toString().equals("0")) {
                        add.setEnabled(false);
                        Toast.makeText(getApplicationContext(), "Sorry, The quantity carried out", Toast.LENGTH_SHORT).show();
                    } else {
                        add.setOnClickListener(v -> {
                            add.setEnabled(true);
                            AddCart(Prod_id);
                        });
                    }
                });
    }

    void AddCart(String Prod_id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Cart").whereEqualTo("customerId", userId).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.getDocuments().size() == 0) {
                String id = db.collection("Cart").document().getId().substring(0, 5);
                ArrayList<String> productsQuantity = new ArrayList<>();
                ArrayList<String> prodIds = new ArrayList<>();
                ArrayList<String> prodNames = new ArrayList<>();
                ArrayList<String> prices = new ArrayList<>();
                productsQuantity.add("1");
                prodIds.add(Prod_id);
                prodNames.add(e_name.getText().toString());
                prices.add(e_price.getText().toString());
                Cart newTemp = new Cart(id, userId, productsQuantity, prodIds, prodNames, prices);
                db.collection("Cart").document(id).set(newTemp).addOnSuccessListener(unused -> {
                    Toast.makeText(this, "added product to new cart", Toast.LENGTH_SHORT).show();
                });
            } else {
                DocumentSnapshot d = queryDocumentSnapshots.getDocuments().get(0);
                Cart temp = d.toObject(Cart.class);
                for (String s : temp.getProducts()) {
                    if (s.equals(Prod_id)) {
                        Toast.makeText(this, "this product is already added", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                temp.getProductsQuantity().add("1");
                temp.getProducts().add(Prod_id);
                temp.getNames().add(e_name.getText().toString());
                temp.getPrices().add(e_price.getText().toString());
                db.collection("Cart").document(temp.getId()).set(temp).addOnSuccessListener(unused1 -> {
                    Toast.makeText(this, "added this product to existing cart", Toast.LENGTH_SHORT).show();
                });

            }
        });

    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
