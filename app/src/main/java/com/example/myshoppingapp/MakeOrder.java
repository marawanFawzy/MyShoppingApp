package com.example.myshoppingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.myshoppingapp.firebase.Cart;
import com.example.myshoppingapp.firebase.Orders;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MakeOrder extends AppCompatActivity {
    public static final int LOCATION_REQUEST_CODE = 155;
    Button confirm, location, cart, home;
    EditText Longitude, Latitude, nameOfReceiver , feedback;
    String userId;
    double total;
    ShoppingDatabase sdb = new ShoppingDatabase(this);
    private FusedLocationProviderClient fusedLocationClient;

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
            else {
                Date date = new Date();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Cart").whereEqualTo("customerId", userId).get().addOnSuccessListener(queryDocumentSnapshots -> {
                    String id;
                    if (queryDocumentSnapshots.getDocuments().size() != 0) {
                        id = db.collection("Orders").document().getId().substring(0, 5);
                        Cart temp = queryDocumentSnapshots.getDocuments().get(0).toObject(Cart.class);
                        Orders newTemp = new Orders(id, userId, date, Double.parseDouble(Latitude.getText().toString()), Double.parseDouble(Longitude.getText().toString()), nameOfReceiver.getText().toString(), temp , total);
                        newTemp.setRating(simpleRatingBar.getRating());
                        newTemp.setFeedback(feedback.getText().toString());
                        db.collection("Orders").document(id).set(newTemp).addOnSuccessListener(unused -> {
                            Toast.makeText(MakeOrder.this, "your order is created successfully", Toast.LENGTH_SHORT).show();
                            temp.setCustomerId("finished Order");
                            db.collection("Cart").document(temp.getId()).set(temp);
                        });
                    }
                    else Toast.makeText(MakeOrder.this, "please fill your cart first", Toast.LENGTH_SHORT).show();
                });
                System.out.println();
                //TODO TO BE CONSIDERED WHEN DR ANSWERS
//                Intent i = new Intent(MakeOrder.this, map.class);
//                i.putExtra("userId" ,userId);
//                startActivity(i);
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
}
