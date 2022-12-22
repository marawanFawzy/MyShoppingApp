package com.example.myshoppingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myshoppingapp.firebase.Products;
import com.google.firebase.firestore.FirebaseFirestore;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;

public class ShowFeedBack extends AppCompatActivity {
    ArrayAdapter<String> FeedBackList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView myList;
    String Prod_id;
    EditText e_name, e_price, e_description;
    CircleImageView ProductImage;
    Products ClonedProduct = new Products("", 0, "", 0, "", "","" ,new ArrayList<>(), 0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_feed_back);
        e_name = findViewById(R.id.editTextP_Name);
        e_price = findViewById(R.id.editTextPrice);
        e_description = findViewById(R.id.Description);
        ProductImage = findViewById(R.id.ProductImageDetails);
        myList = findViewById(R.id.FeedBack_listview);
        FeedBackList = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        myList.setAdapter(FeedBackList);
        Intent ii = getIntent();
        Prod_id = ii.getStringExtra("Prod_id");
        getProduct(Prod_id);
    }
    void getProduct(String Prod_id) {

        db.collection("Products")
                .whereEqualTo("id", Prod_id)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.size() == 0) {
                        Toast.makeText(ShowFeedBack.this, "not found", Toast.LENGTH_SHORT).show();
                    } else {
                        ClonedProduct = queryDocumentSnapshots.getDocuments().get(0).toObject(Products.class);
                        if (ClonedProduct != null) {
                            e_name.setText(ClonedProduct.getName());
                            e_price.setText(String.valueOf(ClonedProduct.getPrice()));
                            e_description.setText(ClonedProduct.getDescription());
                            ProductImage.setImageBitmap(StringToBitMap(ClonedProduct.getPhoto()));
                            FeedBackList.addAll(ClonedProduct.getFeedback());
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