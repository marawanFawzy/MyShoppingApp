package com.example.myshoppingapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myshoppingapp.firebase.Customers;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DeleteUser extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button buttonActivate , buttonBan;
    FloatingActionButton buttonDelete;
    ArrayList<Customers>LoadedUser = new ArrayList<>();
    Spinner spinner;
    int SelectedUser;
    private final ArrayList<String> paths = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);
        buttonActivate = findViewById(R.id.buttonActivate);
        buttonBan = findViewById(R.id.buttonBan);
        buttonDelete = findViewById(R.id.buttonDelete);
        spinner = findViewById(R.id.spinner);
        paths.add("Select User");
        LoadAllUsers();
        LoadedUser.add(new Customers());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(DeleteUser.this, android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    buttonActivate.setEnabled(false);
                    buttonBan.setEnabled(false);
                    buttonDelete.setEnabled(false);

                }
                else{
                    SelectedUser = position;
                    buttonActivate.setEnabled(!LoadedUser.get(position).isStatus());
                    buttonBan.setEnabled(LoadedUser.get(position).isStatus());
                    buttonDelete.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        buttonActivate.setOnClickListener(v -> {
            LoadedUser.get(SelectedUser).setStatus(true);
            db.collection("Customers")
                    .document(LoadedUser.get(SelectedUser).getId())
                    .set(LoadedUser.get(SelectedUser)).addOnSuccessListener(unused -> {
                        Toast.makeText(DeleteUser.this, "user is activated", Toast.LENGTH_SHORT).show();
                        spinner.setSelection(0);
                    });
        });
        buttonBan.setOnClickListener(v -> {
            LoadedUser.get(SelectedUser).setStatus(false);
            db.collection("Customers")
                    .document(LoadedUser.get(SelectedUser).getId())
                    .set(LoadedUser.get(SelectedUser)).addOnSuccessListener(unused -> {
                        Toast.makeText(DeleteUser.this, "user is Baned", Toast.LENGTH_SHORT).show();
                        spinner.setSelection(0);
                    });
        });
        buttonDelete.setOnClickListener(v -> db.collection("Customers")
                .document(LoadedUser.get(SelectedUser).getId())
                .delete().addOnSuccessListener(unused -> {
                    Toast.makeText(DeleteUser.this, "user is Deleted", Toast.LENGTH_SHORT).show();
                    paths.remove(SelectedUser);
                    spinner.setSelection(0);
                }));
    }
    void LoadAllUsers()
    {
        db.collection("Customers").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.size() == 0) {
                        Toast.makeText(DeleteUser.this, "there is no Current users ", Toast.LENGTH_SHORT).show();
                    } else {
                        for (DocumentSnapshot d : queryDocumentSnapshots) {
                            Customers temp = d.toObject(Customers.class);
                            if(temp.isFlag())
                                continue;
                            LoadedUser.add(temp);
                            paths.add(temp.getUsername());
                        }
                    }
                });
    }
}