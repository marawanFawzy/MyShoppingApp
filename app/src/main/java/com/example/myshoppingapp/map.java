package com.example.myshoppingapp;

import static com.google.android.gms.maps.CameraUpdateFactory.*;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class map extends AppCompatActivity implements OnMapReadyCallback {
    boolean granted;
    GoogleMap gMap;
    private Marker marker;
    private final static int request_code = 100;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        fab = findViewById(R.id.fab_save_location);
        checkMyPermission();
        if (granted) {
            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            supportMapFragment.getMapAsync(this);
        }
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(map.this, MainActivity.class);
            intent.putExtra("result", marker.getPosition());
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }

    void checkMyPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                granted = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                Intent i = new Intent();
                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
                i.setData(uri);
                startActivity(i);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        gMap.setMyLocationEnabled(true);
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        gMap.setOnMapLongClickListener(latLng -> {
            gMap.clear(); // to remove previous marker

            String snippet = String.format("Lat: %1$.5f, Long: %2$.5f", latLng.latitude, latLng.longitude);

            marker = gMap.addMarker(new MarkerOptions().title("Destination").position(latLng).snippet(snippet));

            Toast.makeText(this, latLng.toString(), Toast.LENGTH_SHORT).show();

        });
        gMap.setOnPoiClickListener(pointOfInterest -> {
            gMap.clear();

            marker = gMap.addMarker(new MarkerOptions().title(pointOfInterest.name).position(pointOfInterest.latLng));

            gMap.animateCamera(newLatLngZoom(pointOfInterest.latLng, 20));
        });
    }
}