package com.example.myshoppingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myshoppingapp.firebase.Categories;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EditCat extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ArrayList<String> paths = new ArrayList<>();
    private String SelectedCategory;
    FloatingActionButton edit;
    EditText editTextEdit;
    Spinner spinner;
    int selectedPosition = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cat);
        spinner = findViewById(R.id.spinner);
        edit = findViewById(R.id.edit);
        editTextEdit = findViewById(R.id.editTextTextPersonName);
        getAllCategories();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditCat.this,
                android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        edit.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            if (SelectedCategory.equals("Select category"))
                Toast.makeText(this, "please choose a category first", Toast.LENGTH_SHORT).show();
            else {
                if (editTextEdit.getText().toString().equals(""))
                    Toast.makeText(this, "please choose a new name first", Toast.LENGTH_SHORT).show();
                else {
                    db.collection("Categories")
                            .whereEqualTo("name", editTextEdit.getText().toString())
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                                if (queryDocumentSnapshots.getDocuments().size() == 0) {
                                    db.collection("Categories")
                                            .whereEqualTo("name", SelectedCategory)
                                            .get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                                                DocumentSnapshot d = queryDocumentSnapshots1.getDocuments().get(0);
                                                Categories temp = d.toObject(Categories.class);
                                                temp.setName(editTextEdit.getText().toString());
                                                db.collection("Categories").document(temp.getId()).set(temp).addOnSuccessListener(unused -> {
                                                    Toast.makeText(this, "category name is edited", Toast.LENGTH_SHORT).show();
                                                    paths.add(selectedPosition ,editTextEdit.getText().toString() );
                                                    paths.remove(selectedPosition+1);
                                                    editTextEdit.setText("");
                                                    spinner.setSelection(0);
                                                });
                                            });
                                } else {
                                    Toast.makeText(this, "this name is already used", Toast.LENGTH_SHORT).show();
                                    editTextEdit.setText("");
                                }
                            });


                }
            }
        });
    }

    void getAllCategories() {
        paths = new ArrayList<>();
        paths.add("Select category");
        spinner.setSelection(0);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Categories").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.size() == 0) {
                        Toast.makeText(EditCat.this, "add a category First ", Toast.LENGTH_SHORT).show();
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
        selectedPosition = position;
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }
}