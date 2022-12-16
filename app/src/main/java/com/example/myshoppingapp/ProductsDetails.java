package com.example.myshoppingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myshoppingapp.firebase.Cart;
import com.example.myshoppingapp.firebase.Products;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductsDetails extends AppCompatActivity {
    EditText e_name, e_price, e_qty, e_description, e_time;
    FloatingActionButton add;
    ImageView cart, home, EditProfile, Orders;
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
        e_description = findViewById(R.id.Description);
        e_time = findViewById(R.id.estimatedTime);
        add = findViewById(R.id.buttonAddtoCart);
        cart = findViewById(R.id.cartbutton);
        home = findViewById(R.id.homebutton);
        Orders = findViewById(R.id.Orders);
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
        Orders.setOnClickListener(v -> {
            Intent i = new Intent(ProductsDetails.this, Current_Orders.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        home.setOnClickListener(v -> {
            Intent i = new Intent(ProductsDetails.this, HomeActivity.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        Payment.setOnClickListener(v -> {
            Intent i = new Intent(ProductsDetails.this, AddPayment.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        EditProfile.setOnClickListener(v -> {
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
                                e_description.setText(temp.getDescription());
                                e_time.setText(String.valueOf(temp.getDays_For_Delivery()));
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
        db.collection("Products").document(Prod_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Products ProductTemp = documentSnapshot.toObject(Products.class);
                ProductTemp.setQuantity(1);
                db.collection("Cart").whereEqualTo("customerId", userId).get().addOnSuccessListener(queryDocumentSnapshots -> {
                    Cart temp;
                    if (queryDocumentSnapshots.getDocuments().size() == 0) {
                        ArrayList<Products> newCartProduct = new ArrayList<>();
                        newCartProduct.add(ProductTemp);
                        String id = db.collection("Cart").document().getId().substring(0, 5);
                        temp = new Cart(id, userId, newCartProduct);
                        db.collection("Cart").document(id).set(temp).addOnSuccessListener(unused -> {
                            Toast.makeText(ProductsDetails.this, "added product to new cart", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        DocumentSnapshot d = queryDocumentSnapshots.getDocuments().get(0);
                        temp = d.toObject(Cart.class);
                        for (Products s : temp.getProducts()) {
                            if (s.getId().equals(Prod_id)) {
                                Toast.makeText(ProductsDetails.this, "this product is already added", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        temp.getProducts().add(ProductTemp);
                        db.collection("Cart").document(temp.getId()).set(temp).addOnSuccessListener(unused1 -> {
                            Toast.makeText(ProductsDetails.this, "added this product to existing cart", Toast.LENGTH_SHORT).show();
                        });
                    }
                });

            }
        });

    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
