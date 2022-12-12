package com.example.myshoppingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myshoppingapp.firebase.Categories;
import com.example.myshoppingapp.firebase.Products;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class deleteCat extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private final ArrayList<String> paths = new ArrayList<>();
    private String SelectedCategory;
    FloatingActionButton delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_cat);
        Spinner spinner = findViewById(R.id.spinner);
        delete = findViewById(R.id.editProduct);
        getAllCategories();
        paths.add("Select category");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(deleteCat.this,
                android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        delete.setOnClickListener(v -> {
            if(SelectedCategory.equals("Select category"))
            {
                Toast.makeText(this, "select category first", Toast.LENGTH_SHORT).show();
                return;
            }
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Categories")
                    .whereEqualTo("name", SelectedCategory)
                    .get().addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.getDocuments().size() == 0) {
                            Toast.makeText(deleteCat.this, "this category is already deleted", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Categories temp = queryDocumentSnapshots.getDocuments().get(0).toObject(Categories.class);
                        db.collection("Products").whereEqualTo("catId", temp.getId()).get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                            if (queryDocumentSnapshots1.getDocuments().size() != 0) {
                                for (int i = 0; i < queryDocumentSnapshots1.size(); i++) {
                                    Products deleteTemp = queryDocumentSnapshots1.getDocuments().get(i).toObject(Products.class);
                                    db.collection("Products").document(deleteTemp.getId()).delete();
                                }
                            }
                            db.collection("Categories").document(temp.getId()).delete().addOnSuccessListener(unused -> {
                                Toast.makeText(deleteCat.this, "deleted this category", Toast.LENGTH_SHORT).show();
                                paths.remove(temp.getName());
                                spinner.setSelection(0);
                            });

                        });
                    });
        });
    }

    void getAllCategories() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Categories").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.size() == 0) {
                        Toast.makeText(deleteCat.this, "add a category First ", Toast.LENGTH_SHORT).show();
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
}