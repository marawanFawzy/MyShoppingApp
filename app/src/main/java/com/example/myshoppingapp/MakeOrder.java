package com.example.myshoppingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MakeOrder extends AppCompatActivity {
    public static final int LOCATION_REQUEST_CODE = 155;
    ArrayList<String> iDArray;
    ArrayList<String> quantityArray;
    Button confirm, location;
    Button cart;
    Button home;
    ShoppingDatabase sdb = new ShoppingDatabase(this);
    private FusedLocationProviderClient fusedLocationClient;

    @AfterPermissionGranted(LOCATION_REQUEST_CODE)
    private boolean getLocationPermissions() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};

        if (EasyPermissions.hasPermissions(this, perms)) {
            return true;
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "We need location permissions in order to the app to functional correctly",
                    LOCATION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        getLocationPermissions();
        cart = (Button) findViewById(R.id.cartbutton);
        home = (Button) findViewById(R.id.homebutton);
        confirm = (Button) findViewById(R.id.confirm);
        location = (Button) findViewById(R.id.location);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        iDArray = new ArrayList<String>();
        iDArray = getIntent().getStringArrayListExtra("productsID");
        quantityArray = new ArrayList<String>();
        quantityArray = getIntent().getStringArrayListExtra("productsQuantity");

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MakeOrder.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MakeOrder.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(MakeOrder.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                    return;
                }
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(MakeOrder.this, location1 -> {
                            if (location1 != null) {
                                // Logic to handle location object
                                // toast location
                                Toast.makeText(MakeOrder.this, "Location: " + location1.getLatitude() + ", " + location1.getLongitude(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MakeOrder.this, ShoppingCart.class);
                startActivity(i);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MakeOrder.this, HomeActivity.class);
                startActivity(i);
            }
        });

    }
}
