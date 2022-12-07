package com.example.myshoppingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Date;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MakeOrder extends AppCompatActivity {
    public static final int LOCATION_REQUEST_CODE = 155;
    ArrayList<String> iDArray;
    ArrayList<String> quantityArray;
    ArrayList<String> cat_ids;
    Button confirm, location, cart, home;
    EditText Longitude, Latitude, nameOfReceiver;
    String userId;
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
        cart = findViewById(R.id.cartbutton);
        home = findViewById(R.id.homebutton);
        confirm = findViewById(R.id.confirm);
        location = findViewById(R.id.location);
        Longitude = findViewById(R.id.editTextLongitude);
        Latitude = findViewById(R.id.editTextTextLatitude);
        nameOfReceiver = findViewById(R.id.editTextnameOfReceiver);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        iDArray = new ArrayList<>();
        iDArray = getIntent().getStringArrayListExtra("productsID");
        quantityArray = new ArrayList<>();
        quantityArray = getIntent().getStringArrayListExtra("productsQuantity");
        cat_ids = new ArrayList<>();
        cat_ids = getIntent().getStringArrayListExtra("products_cat_ids");

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
                Toast.makeText(this, "your order is created successfully", Toast.LENGTH_SHORT).show();
                //TODO remove hard coded custID
                sdb.CreateNewOrder(1, date, Latitude.getText().toString(), Longitude.getText().toString(), nameOfReceiver.getText().toString());
                int ret = sdb.getLastOrderID();
                for (int i = 0; i < iDArray.size(); i++) {
                    sdb.OrderDetails(ret, Integer.parseInt(iDArray.get(i)), Integer.parseInt(quantityArray.get(i)), Integer.parseInt(cat_ids.get(i)));
                }
                ret = sdb.test();
                System.out.println();
                Intent i = new Intent(MakeOrder.this, HomeActivity.class);
                i.putExtra("userId" ,userId);
                startActivity(i);
            }
        });

        cart.setOnClickListener(v -> {
            Intent i = new Intent(MakeOrder.this, ShoppingCart.class);
            i.putExtra("userId" ,userId);
            startActivity(i);
        });

        home.setOnClickListener(v -> {
            Intent i = new Intent(MakeOrder.this, HomeActivity.class);
            i.putExtra("userId" ,userId);
            startActivity(i);
        });

    }
}
