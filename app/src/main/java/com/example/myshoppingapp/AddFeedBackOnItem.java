package com.example.myshoppingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myshoppingapp.firebase.Products;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;

public class AddFeedBackOnItem extends AppCompatActivity {
    EditText e_name, e_price, e_FeedBack, e_description;
    FloatingActionButton Submit;
    String Prod_id;
    CircleImageView ProductImage;
    Products ClonedProduct = new Products("", 0, "", 0, "", "", "", new ArrayList<>(), 0);
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feed_back_on_item);
        ProductImage = findViewById(R.id.ProductImageDetails);
        e_name = findViewById(R.id.editTextP_Name);
        e_price = findViewById(R.id.editTextPrice);
        e_FeedBack = findViewById(R.id.FeedBack);
        e_description = findViewById(R.id.Description);
        Submit = findViewById(R.id.SubmitFeedBack);
        Intent ii = getIntent();
        Prod_id = ii.getStringExtra("Prod_id");
        getProduct(Prod_id);
        Submit.setOnClickListener(v -> {
            if (e_FeedBack.getText().toString().equals("")) {
                Toast.makeText(AddFeedBackOnItem.this, "please type your feedback", Toast.LENGTH_SHORT).show();
                return;
            }
            ClonedProduct.getFeedback().add(e_FeedBack.getText().toString());
            Products newTemp = ClonedProduct.clone();
            db.collection("Products").document(ClonedProduct.getId()).set(newTemp).addOnSuccessListener(unused -> {
                Toast.makeText(AddFeedBackOnItem.this, "thanks for your feedback", Toast.LENGTH_SHORT).show();
                finish();
            });

        });
    }

    void getProduct(String Prod_id) {

        db.collection("Products")
                .whereEqualTo("id", Prod_id)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.size() == 0) {
                        Toast.makeText(AddFeedBackOnItem.this, "not found", Toast.LENGTH_SHORT).show();
                    } else {
                        for (DocumentSnapshot d : queryDocumentSnapshots) {
                            ClonedProduct = d.toObject(Products.class);
                            if (ClonedProduct != null) {
                                e_name.setText(ClonedProduct.getName());
                                e_price.setText(String.valueOf(ClonedProduct.getPrice()));
                                e_description.setText(ClonedProduct.getDescription());
                                ProductImage.setImageBitmap(StringToBitMap(ClonedProduct.getPhoto()));
                            }
                        }
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
