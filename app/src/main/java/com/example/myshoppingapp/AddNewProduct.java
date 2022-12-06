package com.example.myshoppingapp;

import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AddNewProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private ArrayList<String> paths = new ArrayList<>();
    EditText ProductName, ProductQuantity, price;
    private String SelectedCategory, SelectedCategoryId;
    Button buttonAddProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        buttonAddProduct = findViewById(R.id.buttonAddProduct);
        ProductName = findViewById(R.id.ProductName);
        ProductQuantity = findViewById(R.id.ProductQuantity);
        price = findViewById(R.id.price);
        getAllCategories();
        paths.add("");
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddNewProduct.this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        buttonAddProduct.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            if (SelectedCategory.equals(""))
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
                                                return;
                                            }
                                            String id = db.collection("Products").document().getId().substring(0,5);
                                            Products newtemp = new Products(id, Integer.parseInt(ProductQuantity.getText().toString()), SelectedCategoryId, Integer.parseInt(price.getText().toString()), ProductName.getText().toString());

                                            db.collection("Products").document(id).set(newtemp).addOnSuccessListener(unused -> Toast.makeText(AddNewProduct.this, "added", Toast.LENGTH_SHORT).show());
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
        // TODO Auto-generated method stub

    }
}