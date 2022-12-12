package com.example.myshoppingapp;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddNewProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private final ArrayList<String> paths = new ArrayList<>();
    EditText ProductName, ProductQuantity, price;
    private String SelectedCategory, SelectedCategoryId;
    Button ButtonUpload;
    FloatingActionButton buttonAddProduct;
    Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    CircleImageView ProductImage;
    String photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        ProductImage = findViewById(R.id.ProductImage);
        buttonAddProduct = findViewById(R.id.buttonAddProduct);
        ButtonUpload = findViewById(R.id.ButtonUpload);
        ButtonUpload.setOnClickListener(v -> SelectImage());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        ProductName = findViewById(R.id.ProductNameEdit);
        ProductQuantity = findViewById(R.id.ProductQuantityAdd);
        price = findViewById(R.id.priceAdd);
        getAllCategories();
        paths.add("Select Category");
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddNewProduct.this, android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        buttonAddProduct.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            if (SelectedCategory.equals("Select Category"))
                Toast.makeText(this, "please choose a category first", Toast.LENGTH_SHORT).show();
            else {
                if (ProductName.getText().toString().equals(""))
                    Toast.makeText(this, "please choose a name first", Toast.LENGTH_SHORT).show();
                else if (ProductQuantity.getText().toString().equals(""))
                    Toast.makeText(this, "please choose a Quantity first", Toast.LENGTH_SHORT).show();
                else if (price.getText().toString().equals(""))
                    Toast.makeText(this, "please choose a price first", Toast.LENGTH_SHORT).show();
                else {
                    db.collection("Categories")
                            .whereEqualTo("name", SelectedCategory)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                DocumentSnapshot d = queryDocumentSnapshots.getDocuments().get(0);
                                Categories temp = d.toObject(Categories.class);
                                SelectedCategoryId = temp.getId();
                                db.collection("Products")
                                        .whereEqualTo("name", ProductName.getText().toString())
                                        .whereEqualTo("catId", SelectedCategoryId)
                                        .get()
                                        .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                            if (queryDocumentSnapshots1.getDocuments().size() != 0) {
                                                Toast.makeText(AddNewProduct.this, "this Product is already added", Toast.LENGTH_SHORT).show();
                                                ProductName.setText("");
                                                return;
                                            }
                                            String id = db.collection("Products").document().getId().substring(0, 5);
                                            Products newtemp = new Products(id, Integer.parseInt(ProductQuantity.getText().toString()), SelectedCategoryId, Integer.parseInt(price.getText().toString()), ProductName.getText().toString(), photo);

                                            db.collection("Products").document(id).set(newtemp).addOnSuccessListener(unused -> {
                                                Toast.makeText(AddNewProduct.this, "added", Toast.LENGTH_SHORT).show();
                                                ProductQuantity.setText("");
                                                price.setText("");
                                                ProductName.setText("");
                                                spinner.setSelection(0);
                                                Drawable myDrawable = getResources().getDrawable(R.drawable.ic_baseline_image_200);
                                                ProductImage.setImageDrawable(myDrawable);
                                            });
                                        });
                            });
                }
            }
        });
    }

    void getAllCategories() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Categories").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.size() == 0) {
                        Toast.makeText(AddNewProduct.this, "add a category First ", Toast.LENGTH_SHORT).show();
                    } else {
                        for (DocumentSnapshot d : queryDocumentSnapshots) {
                            Categories temp = d.toObject(Categories.class);
                            if (temp != null)
                                paths.add(temp.getName());
                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SelectedCategory = parent.getItemAtPosition(position).toString();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
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
                photo = BitMapToString(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 15, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}