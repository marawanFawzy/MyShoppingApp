package com.example.myshoppingapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myshoppingapp.firebase.Categories;
import com.example.myshoppingapp.firebase.Products;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class deleteProduct extends AppCompatActivity {
    private final ArrayList<String> paths = new ArrayList<>();
    private final ArrayList<String> pathsProducts = new ArrayList<>();
    private String SelectedCategory, SelectedProduct, SelectedCategoryId;
    Button delete;
    Spinner spinner, spinnerProducts;
    CircleImageView ProductImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);
        ProductImage = findViewById(R.id.ProductImageDelete);
        spinner = findViewById(R.id.spinner);
        paths.add("");
        pathsProducts.add("");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(deleteProduct.this,
                android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinnerProducts = findViewById(R.id.spinnerProduct);
        spinnerProducts.setEnabled(false);
        ArrayAdapter<String> adapterProducts = new ArrayAdapter<>(deleteProduct.this,
                android.R.layout.simple_spinner_item, pathsProducts);
        adapterProducts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProducts.setAdapter(adapterProducts);
        delete = findViewById(R.id.editProduct);
        delete.setEnabled(false);
        getAllCategories();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedCategory = parent.getItemAtPosition(position).toString();
                if (!SelectedCategory.equals("")) {
                    pathsProducts.clear();
                    pathsProducts.add("");
                    getAllProducts();
                    spinnerProducts.setEnabled(true);
                } else {
                    pathsProducts.clear();
                    pathsProducts.add("");
                    spinnerProducts.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerProducts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedProduct = parent.getItemAtPosition(position).toString();
                delete.setEnabled(!SelectedCategory.equals(""));
                if(!SelectedCategory.equals(""))
                {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Products")
                            .whereEqualTo("name", SelectedProduct)
                            .whereEqualTo("catId", SelectedCategoryId)
                            .get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                                Products Temp = queryDocumentSnapshots1.getDocuments().get(0).toObject(Products.class);
                                ProductImage.setImageBitmap(StringToBitMap(Temp.getPhoto()));
                            });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        delete.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Products")
                    .whereEqualTo("name", SelectedProduct)
                    .whereEqualTo("catId", SelectedCategoryId)
                    .get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                        Products deleteTemp = queryDocumentSnapshots1.getDocuments().get(0).toObject(Products.class);
                        db.collection("Products").document(deleteTemp.getId()).delete().addOnSuccessListener(unused -> {
                            pathsProducts.remove(SelectedProduct);
                            spinnerProducts.setSelection(0);
                            Drawable myDrawable = getResources().getDrawable(R.drawable.ic_baseline_image_200);
                            ProductImage.setImageDrawable(myDrawable);
                            Toast.makeText(this, "deleted " + SelectedProduct, Toast.LENGTH_SHORT).show();
                        });
                    });

        });
    }

    void getAllCategories() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Categories").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.size() == 0) {
                        Toast.makeText(deleteProduct.this, "add a category First ", Toast.LENGTH_SHORT).show();
                    } else {
                        for (DocumentSnapshot d : queryDocumentSnapshots) {
                            Categories temp = d.toObject(Categories.class);
                            if (temp != null)
                                paths.add(temp.getName());
                        }
                    }
                });
    }

    void getAllProducts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Categories").whereEqualTo("name", SelectedCategory).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                DocumentSnapshot d = queryDocumentSnapshots.getDocuments().get(0);
                Categories temp = d.toObject(Categories.class);
                SelectedCategoryId = temp.getId();
                db.collection("Products")
                        .whereEqualTo("catId", SelectedCategoryId).get()
                        .addOnSuccessListener(queryDocumentSnapshots1 -> {
                            if (queryDocumentSnapshots1.size() == 0) {
                                Toast.makeText(deleteProduct.this, "add a product First ", Toast.LENGTH_SHORT).show();
                            } else {
                                for (DocumentSnapshot d1 : queryDocumentSnapshots1) {
                                    Products getTemp = d1.toObject(Products.class);
                                    if (getTemp != null)
                                        pathsProducts.add(getTemp.getName());
                                }
                            }
                        });
            }
        });

    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}