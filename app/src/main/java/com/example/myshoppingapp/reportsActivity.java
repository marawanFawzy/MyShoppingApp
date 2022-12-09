package com.example.myshoppingapp;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myshoppingapp.firebase.Categories;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;

public class reportsActivity extends AppCompatActivity {
    private final ArrayList<String> users = new ArrayList<>()
            , usersIds = new ArrayList<>();
    ArrayAdapter<String> OrderArrayAdapter;
    ListView mylist;
    int SelectedPosition = 0 ;
    DatePickerDialog.OnDateSetListener datePicker;
    EditText date;
    CheckBox allTime;
    Spinner spinner;
    Button getOrders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        mylist = findViewById(R.id.Products_listview);
        OrderArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mylist.setAdapter(OrderArrayAdapter);
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO move to the next activity to show order details
            }
        });
        allTime = findViewById(R.id.allTimeCheckBox);
        allTime.setOnCheckedChangeListener((buttonView, isChecked) -> date.setEnabled(!isChecked));
        spinner = findViewById(R.id.spinnerUsers);
        users.add("all users");
        usersIds.add("");
        getAllUsers();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(reportsActivity.this,
                android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setSelection(0);
        date = findViewById(R.id.dateForOrders);
        date.setEnabled(!allTime.isChecked());
        date.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(reportsActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, datePicker, year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
        disableSoftInputFromAppearing(date);
        datePicker = (datePicker, year, month, day) -> {
            month = month + 1;
            String PickedDate = month + "/" + day + "/" + year;
            date.setText(PickedDate);
        };
        getOrders = findViewById(R.id.ButtonFindOrders);
        //TODO decide data to be added to the List view
        getOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedPosition ==0)
                {
                    //TODO get all orders
                }
                else {
                    //TODO get user's orders
                }
            }
        });
    }
    public static void disableSoftInputFromAppearing(EditText editText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setShowSoftInputOnFocus(false);
        } else {
            try {
                final Method method = EditText.class.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {
                // handle error
            }
        }
    }
    void getAllUsers()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Products").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.size() == 0) {
                        Toast.makeText(reportsActivity.this, "no Customers", Toast.LENGTH_SHORT).show();
                        usersIds.clear();
                        users.clear();
                    } else {
                        for (DocumentSnapshot d : queryDocumentSnapshots) {
                            Categories temp = d.toObject(Categories.class);
                            if (temp != null) {
                                users.add(temp.getName());
                                usersIds.add(temp.getId());
                            }
                        }
                    }
                });
    }

}