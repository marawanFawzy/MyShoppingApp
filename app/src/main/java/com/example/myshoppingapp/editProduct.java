package com.example.myshoppingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myshoppingapp.firebase.Categories;
import com.example.myshoppingapp.firebase.Products;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class editProduct extends AppCompatActivity {
    private final ArrayList<String> paths = new ArrayList<>();
    private final ArrayList<String> pathsProducts = new ArrayList<>();
    private String SelectedCategory, SelectedProduct, SelectedCategoryId;
    Button edit, changePhoto;
    Spinner spinner, spinnerProducts;
    EditText name, quantity, price;
    Uri filePath;
    CircleImageView ProductImage;
    String newPhoto = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        ProductImage = findViewById(R.id.ProductImageEdit);

        name = findViewById(R.id.ProductNameEdit);
        quantity = findViewById(R.id.ProductQuantityEdit);
        price = findViewById(R.id.priceEdit);
        spinner = findViewById(R.id.spinner);
        paths.add("");
        pathsProducts.add("");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(editProduct.this,
                android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinnerProducts = findViewById(R.id.spinnerProduct);
        spinnerProducts.setEnabled(false);
        ArrayAdapter<String> adapterProducts = new ArrayAdapter<>(editProduct.this,
                android.R.layout.simple_spinner_item, pathsProducts);
        adapterProducts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProducts.setAdapter(adapterProducts);
        edit = findViewById(R.id.editProduct);
        changePhoto = findViewById(R.id.ChangePhoto);
        changePhoto.setOnClickListener(v -> SelectImage());
        edit.setEnabled(false);
        getAllCategories();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedCategory = parent.getItemAtPosition(position).toString();
                if (!SelectedCategory.equals("")) {
                    pathsProducts.clear();
                    pathsProducts.add("");
                    spinnerProducts.setSelection(0);
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
                edit.setEnabled(!SelectedProduct.equals(""));
                if (!SelectedProduct.equals("")) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Products")
                            .whereEqualTo("name", SelectedProduct)
                            .whereEqualTo("catId", SelectedCategoryId)
                            .get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                                Products EditTemp = queryDocumentSnapshots1.getDocuments().get(0).toObject(Products.class);
                                name.setText(EditTemp.getName());
                                quantity.setText(String.valueOf(EditTemp.getQuantity()));
                                price.setText(String.valueOf(EditTemp.getPrice()));
                                ProductImage.setImageBitmap(StringToBitMap(EditTemp.getPhoto()));
                            });
                } else {
                    name.setText("");
                    quantity.setText("");
                    price.setText("");
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        edit.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Products")
                    .whereEqualTo("name", SelectedProduct)
                    .whereEqualTo("catId", SelectedCategoryId)
                    .get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                        Products EditTemp = queryDocumentSnapshots1.getDocuments().get(0).toObject(Products.class);
                        EditTemp.setName(name.getText().toString());
                        EditTemp.setQuantity(Integer.parseInt(quantity.getText().toString()));
                        EditTemp.setPrice(Double.parseDouble(price.getText().toString()));
                        if(!newPhoto.equals(""))
                        {
                            EditTemp.setPhoto(newPhoto);
                        }
                        db.collection("Products").document(EditTemp.getId()).set(EditTemp).addOnSuccessListener(unused -> {
                            spinnerProducts.setSelection(0);
                            newPhoto = "";
                            Drawable myDrawable = getResources().getDrawable(R.drawable.ic_baseline_image_200);
                            ProductImage.setImageDrawable(myDrawable);
                            Toast.makeText(this, "updated " + SelectedProduct, Toast.LENGTH_SHORT).show();
                        });
                    });

        });
    }

    void getAllCategories() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Categories").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.size() == 0) {
                        Toast.makeText(editProduct.this, "add a category First ", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(editProduct.this, "add a product First ", Toast.LENGTH_SHORT).show();
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

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), 22);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ProductImage.setImageBitmap(bitmap);
                newPhoto = BitMapToString(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}