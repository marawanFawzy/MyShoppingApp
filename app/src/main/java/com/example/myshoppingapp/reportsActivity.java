package com.example.myshoppingapp;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import com.example.myshoppingapp.firebase.Customers;
import com.example.myshoppingapp.firebase.Orders;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class reportsActivity extends AppCompatActivity {
    private final ArrayList<String> users = new ArrayList<>(), usersIds = new ArrayList<>();
    ArrayAdapter<String> OrderArrayAdapter;
    ListView myList;
    int SelectedPosition = 0;
    DatePickerDialog.OnDateSetListener datePicker;
    EditText date;
    CheckBox allTime;
    Spinner spinner;
    Button getOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        myList = findViewById(R.id.Orders_listview);
        OrderArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        myList.setAdapter(OrderArrayAdapter);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(reportsActivity.this, reportDetails.class);
                startActivity(i);
            }
        });
        allTime = findViewById(R.id.allTimeCheckBox);
        allTime.setOnCheckedChangeListener((buttonView, isChecked) -> {
            date.setEnabled(!isChecked);
            date.setText("");
        });
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
        getOrders.setOnClickListener(v -> {
            OrderArrayAdapter.clear();
            if (date.getText().toString().equals("") && !allTime.isChecked()) {
                Toast.makeText(reportsActivity.this, "please choose date first", Toast.LENGTH_SHORT).show();
                return;
            }
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            if (SelectedPosition == 0) {
                db.collection("Orders").orderBy("customer_id")
                        .get().addOnSuccessListener(queryDocumentSnapshots -> {
                            if (queryDocumentSnapshots.size() == 0) {
                                Toast.makeText(reportsActivity.this, "no Orders", Toast.LENGTH_SHORT).show();
                            } else {
                                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                                    Orders temp = queryDocumentSnapshots.getDocuments().get(i).toObject(Orders.class);
                                    String entry = users.get(usersIds.indexOf(temp.getCustomer_id())) + " | " +
                                            temp.getCart().getProducts().size() + " Product(s) " + " | " +
                                            temp.getTotal() + " EGP " + "| " +
                                            temp.getRating() + " Stars " +"| "+
                                            temp.getOrder_date().getDate() + "/" +
                                            temp.getOrder_date().getMonth() + "/" +
                                            (temp.getOrder_date().getYear()+1900);
                                    if (allTime.isChecked()) {
                                        OrderArrayAdapter.add(entry);
                                    } else {
                                        Date d = new Date(date.getText().toString());
                                        if (d.getDate() == temp.getOrder_date().getDate() && d.getMonth() == temp.getOrder_date().getMonth() && d.getYear() == temp.getOrder_date().getYear())
                                            OrderArrayAdapter.add(entry);
                                    }
                                }
                                if(OrderArrayAdapter.getCount() == 0)
                                    Toast.makeText(reportsActivity.this, "no Orders in this date for this customer", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                db.collection("Orders").whereEqualTo("customer_id", usersIds.get(SelectedPosition))
                        .get().addOnSuccessListener(queryDocumentSnapshots -> {
                            if (queryDocumentSnapshots.size() == 0) {
                                Toast.makeText(reportsActivity.this, "no Orders", Toast.LENGTH_SHORT).show();
                            } else {
                                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                                    Orders temp = queryDocumentSnapshots.getDocuments().get(i).toObject(Orders.class);
                                    String entry = temp.getCart().getProducts().size() + " Product(s) " + " | " +
                                            temp.getTotal() + " EGP " + "| " +
                                            temp.getRating() + " Stars " +"| "+
                                            temp.getOrder_date().getDate() + "/" +
                                            temp.getOrder_date().getMonth() + "/" +
                                            (temp.getOrder_date().getYear()+1900);
                                    if (allTime.isChecked()) {
                                        OrderArrayAdapter.add(entry);
                                    } else {
                                        Date d = new Date(date.getText().toString());
                                        if (d.getDate() == temp.getOrder_date().getDate() && d.getMonth() == temp.getOrder_date().getMonth() && d.getYear() == temp.getOrder_date().getYear())
                                            OrderArrayAdapter.add(entry);
                                    }
                                }
                                if(OrderArrayAdapter.getCount() == 0)
                                    Toast.makeText(reportsActivity.this, "no Orders in this date for this customer", Toast.LENGTH_SHORT).show();
                            }
                        });
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

    void getAllUsers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Customers").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.size() == 0) {
                        Toast.makeText(reportsActivity.this, "no Customers", Toast.LENGTH_SHORT).show();
                        usersIds.clear();
                        users.clear();
                    } else {
                        for (DocumentSnapshot d : queryDocumentSnapshots) {
                            Customers temp = d.toObject(Customers.class);
                            if (temp != null && !temp.isFlag()) {
                                users.add(temp.getName());
                                usersIds.add(temp.getId());
                            }
                        }
                    }
                });
    }

}