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
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myshoppingapp.firebase.Categories;
import com.example.myshoppingapp.firebase.Products;
import com.example.myshoppingapp.helpers.Check;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import de.hdodenhof.circleimageview.CircleImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class editProduct extends AppCompatActivity {
    private final ArrayList<String> paths = new ArrayList<>();
    private final ArrayList<String> pathsProducts = new ArrayList<>();
    private String SelectedCategory, SelectedProduct, SelectedCategoryId;
    Button changePhoto;
    FloatingActionButton edit;
    Spinner spinner, spinnerProducts;
    EditText name, quantity, price, discount;
    Uri filePath;
    CircleImageView ProductImage;
    String newPhoto = "";
    ImageButton ShowFeedBack;
    CheckBox addDiscount;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Products ClonedProduct = new Products("", 0, "", 0, "", "", "", new ArrayList<>(), 0);
    Check errorChecker = new Check();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        ProductImage = findViewById(R.id.ProductImageEdit);
        ShowFeedBack = findViewById(R.id.ShowFeedBack);
        name = findViewById(R.id.ProductNameEdit);
        quantity = findViewById(R.id.ProductQuantityEdit);
        price = findViewById(R.id.priceEdit);
        discount = findViewById(R.id.Discount);
        addDiscount = findViewById(R.id.addDiscount);
        addDiscount.setOnCheckedChangeListener((buttonView, isChecked) -> discount.setEnabled(isChecked));
        spinner = findViewById(R.id.spinner);
        paths.add("Select category");
        spinner.setSelection(0);
        pathsProducts.add("Select Product");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(editProduct.this,
                android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinnerProducts = findViewById(R.id.spinnerProduct);
        spinnerProducts.setEnabled(false);
        spinnerProducts.setSelection(0);
        ArrayAdapter<String> adapterProducts = new ArrayAdapter<>(editProduct.this,
                android.R.layout.simple_spinner_item, pathsProducts);
        adapterProducts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProducts.setAdapter(adapterProducts);
        edit = findViewById(R.id.editProduct);
        changePhoto = findViewById(R.id.ChangePhoto);
        changePhoto.setOnClickListener(v -> SelectImage());
        edit.setEnabled(false);
        getAllCategories();
        ShowFeedBack.setOnClickListener(v -> {
            Intent i = new Intent(editProduct.this, ShowFeedBack.class);
            i.putExtra("Prod_id", ClonedProduct.getId());
            startActivity(i);
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedCategory = parent.getItemAtPosition(position).toString();
                changePhoto.setEnabled(false);
                ShowFeedBack.setEnabled(false);
                if (!SelectedCategory.equals("Select category")) {
                    pathsProducts.clear();
                    pathsProducts.add("Select Product");
                    spinnerProducts.setSelection(-1);
                    getAllProducts();
                    spinnerProducts.setEnabled(true);
                } else {
                    pathsProducts.clear();
                    pathsProducts.add("Select Product");
                    spinnerProducts.setSelection(0);
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
                edit.setEnabled(!SelectedProduct.equals("Select Product"));
                if (!SelectedProduct.equals("Select Product")) {
                    changePhoto.setEnabled(true);
                    ShowFeedBack.setEnabled(true);
                    db.collection("Products")
                            .whereEqualTo("name", SelectedProduct)
                            .whereEqualTo("catId", SelectedCategoryId)
                            .get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                                ClonedProduct = queryDocumentSnapshots1.getDocuments().get(0).toObject(Products.class);
                                name.setText(ClonedProduct.getName());
                                quantity.setText(String.valueOf(ClonedProduct.getQuantity()));
                                price.setText(String.valueOf(ClonedProduct.getPrice()));
                                ProductImage.setImageBitmap(StringToBitMap(ClonedProduct.getPhoto()));
                                discount.setText(String.valueOf(ClonedProduct.getDiscount()));

                            });
                } else {
                    name.setText("");
                    quantity.setText("");
                    price.setText("");
                    newPhoto = "";
                    Drawable myDrawable = getResources().getDrawable(R.drawable.ic_baseline_image_200);
                    ProductImage.setImageDrawable(myDrawable);
                    changePhoto.setEnabled(false);
                    ShowFeedBack.setEnabled(false);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        edit.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String checkerResult = errorChecker.EditTextIsEmpty(name, quantity, price, discount);
            if (!checkerResult.equals("")) {
                Toast.makeText(editProduct.this, "Please fill " + checkerResult + " Data ", Toast.LENGTH_SHORT).show();
            } else {
                db.collection("Products")
                        .whereEqualTo("name", SelectedProduct)
                        .whereEqualTo("catId", SelectedCategoryId)
                        .get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                            ClonedProduct = queryDocumentSnapshots1.getDocuments().get(0).toObject(Products.class);
                            ClonedProduct.setName(name.getText().toString());
                            ClonedProduct.setQuantity(Integer.parseInt(quantity.getText().toString()));
                            ClonedProduct.setPrice(Double.parseDouble(price.getText().toString()));
                            if (addDiscount.isChecked())
                                ClonedProduct.setDiscount(Double.parseDouble(discount.getText().toString()));
                            if (!newPhoto.equals(""))
                                ClonedProduct.setPhoto(newPhoto);
                            Products newTemp = ClonedProduct.clone();
                            db.collection("Products").document(newTemp.getId()).set(newTemp).addOnSuccessListener(unused -> {
                                spinnerProducts.setSelection(0);
                                discount.setText("0");
                                addDiscount.setChecked(false);
                                newPhoto = "";
                                Drawable myDrawable = getResources().getDrawable(R.drawable.ic_baseline_image_200);
                                ProductImage.setImageDrawable(myDrawable);
                                Toast.makeText(this, "updated " + SelectedProduct, Toast.LENGTH_SHORT).show();
                            });
                        });
            }
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

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}