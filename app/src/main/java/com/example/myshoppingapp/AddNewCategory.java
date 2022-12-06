package com.example.myshoppingapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myshoppingapp.firebase.Categories;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddNewCategory extends AppCompatActivity {
    EditText catName ;
    Button buttonAddCat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_category);
        catName = findViewById(R.id.catName);
        buttonAddCat = findViewById(R.id.buttonAddCat);
        buttonAddCat.setOnClickListener(v->{
            if(catName.getText().toString().equals("")) {
                Toast.makeText(this, "please type the category name ", Toast.LENGTH_SHORT).show();
                return;
            }
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String id = db.collection("Categories").document().getId().substring(0, 5);
            Categories newTemp = new Categories(id,catName.getText().toString());
            db.collection("Categories").document(id).set(newTemp);
            Toast.makeText(this, "category "+catName.getText().toString() + " is added", Toast.LENGTH_SHORT).show();
        });

    }
}