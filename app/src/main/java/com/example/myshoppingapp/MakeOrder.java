package com.example.myshoppingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.myshoppingapp.firebase.Cart;
import com.example.myshoppingapp.firebase.Customers;
import com.example.myshoppingapp.firebase.Orders;
import com.example.myshoppingapp.firebase.Products;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MakeOrder extends AppCompatActivity {
    public static final int LOCATION_REQUEST_CODE = 155;
    Button confirm, location;
    ImageView cart, home;
    EditText Longitude, Latitude, nameOfReceiver, feedback;
    String userId, paymentChosen;
    double total;
    private FusedLocationProviderClient fusedLocationClient;
    private final ArrayList<String> paths = new ArrayList<>();

    @AfterPermissionGranted(LOCATION_REQUEST_CODE)
    private void getLocationPermissions() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};

        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(
                    this,
                    "We need location permissions in order to the app to functional correctly",
                    LOCATION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        getLocationPermissions();
        Intent ii = getIntent();
        userId = ii.getStringExtra("userId");
        paths.add("Choose payment method");
        Spinner spinner = findViewById(R.id.spinnerPaymentMethod);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MakeOrder.this, android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paymentChosen = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fillSpinner(userId);
        total = ii.getDoubleExtra("total", 0);
        cart = findViewById(R.id.cartbutton);
        home = findViewById(R.id.homebutton);
        confirm = findViewById(R.id.confirm);
        location = findViewById(R.id.location);
        Longitude = findViewById(R.id.editTextLongitude);
        Latitude = findViewById(R.id.editTextTextLatitude);
        nameOfReceiver = findViewById(R.id.editTextnameOfReceiver);
        feedback = findViewById(R.id.FeedbackEditText);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        RatingBar simpleRatingBar = findViewById(R.id.simpleRatingBar);
        location.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(MakeOrder.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MakeOrder.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MakeOrder.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                return;
            }
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(MakeOrder.this, location1 -> {
                        if (location1 != null) {
                            Toast.makeText(MakeOrder.this, "Location: " + location1.getLatitude() + ", " + location1.getLongitude(), Toast.LENGTH_SHORT).show();
                            Latitude.setText(String.valueOf(location1.getLatitude()));
                            Longitude.setText(String.valueOf(location1.getLongitude()));
                        }
                    });
        });

        confirm.setOnClickListener(v -> {
            if (Longitude.getText().toString().equals("") || Latitude.getText().toString().equals(""))
                Toast.makeText(this, "please press on Location button first", Toast.LENGTH_SHORT).show();
            else if (nameOfReceiver.getText().toString().equals(""))
                Toast.makeText(this, "please type the name of the Receiver", Toast.LENGTH_SHORT).show();
            else if (paymentChosen.equals("Choose payment method"))
                Toast.makeText(this, "please choose payment method", Toast.LENGTH_SHORT).show();
            else {
                Date date = new Date();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Cart").whereEqualTo("customerId", userId).get().addOnSuccessListener(queryDocumentSnapshots -> {
                    String id;
                    if (queryDocumentSnapshots.getDocuments().size() != 0) {
                        id = db.collection("Orders").document().getId().substring(0, 5);
                        Cart temp = queryDocumentSnapshots.getDocuments().get(0).toObject(Cart.class);
                        for (int i = 0; i < temp.getProducts().size(); i++) {
                            int finalI = i;
                            db.collection("Products").whereEqualTo("id", temp.getProducts().get(i)).get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                                Products ProductTemp = queryDocumentSnapshots1.getDocuments().get(0).toObject(Products.class);
                                db.collection("Products").document(temp.getProducts().get(finalI)).update("quantity", ProductTemp.getQuantity() - Integer.parseInt(temp.getProductsQuantity().get(finalI)));
                            });

                        }
                        paymentChosen = (!paymentChosen.equals("Cash")) ? paymentChosen.substring(paymentChosen.indexOf(" ") + 1) : "Cash";
                        Orders newTemp = new Orders(id, userId, date, Double.parseDouble(Latitude.getText().toString()), Double.parseDouble(Longitude.getText().toString()), nameOfReceiver.getText().toString(), temp, paymentChosen, total);
                        newTemp.setRating(simpleRatingBar.getRating());
                        newTemp.setFeedback(feedback.getText().toString());
                        db.collection("Orders").document(id).set(newTemp).addOnSuccessListener(unused -> {
                            Toast.makeText(MakeOrder.this, "your order is created successfully", Toast.LENGTH_SHORT).show();
                            temp.setCustomerId("finished Order");
                            db.collection("Cart").document(temp.getId()).set(temp).addOnSuccessListener(unused1 -> {
                                Latitude.setText("");
                                Longitude.setText("");
                                nameOfReceiver.setText("");
                                feedback.setText("");
                                simpleRatingBar.setRating(0);
                                spinner.setSelection(0);
                            });
                        });

                    } else
                        Toast.makeText(MakeOrder.this, "please fill your cart first", Toast.LENGTH_SHORT).show();
                });
                System.out.println();
                //TODO TO BE CONSIDERED WHEN DR ANSWERS
                //Intent i = new Intent(MakeOrder.this, map.class);
                //i.putExtra("userId" ,userId);
                //startActivity(i);
            }
        });

        cart.setOnClickListener(v -> {
            Intent i = new Intent(MakeOrder.this, ShoppingCart.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });

        home.setOnClickListener(v -> {
            Intent i = new Intent(MakeOrder.this, HomeActivity.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });

    }

    private void fillSpinner(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        paths.add("Cash");
        db.collection("Customers").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Customers temp = documentSnapshot.toObject(Customers.class);
                if (temp.getCreditCard() != null) {
                    if (temp.getCreditCard().getStatus().equals("approved")) {
                        paths.add("Card " + temp.getCreditCard().getNumber());
                    } else if (temp.getCreditCard().getStatus().equals("rejected")) {
                        Toast.makeText(MakeOrder.this, "your credit card is rejected please edit it", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MakeOrder.this, "your credit card is not reviewed yet", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
