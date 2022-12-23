package com.example.myshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myshoppingapp.firebase.Products;
import com.example.myshoppingapp.helpers.Check;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;


public class ProductsSearch extends AppCompatActivity {

    public String res = "", id, userId;
    ArrayList<String> ids = new ArrayList<>();
    ImageView btn_scan;
    String bar = "";
    ImageView iv_mic;
    ListView mylist;
    ArrayAdapter<String> arr;
    EditText search;
    ImageView searchButton;
    ImageView cart, home, EditProfile, Orders;
    ImageButton payment;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_search);
        payment = findViewById(R.id.Payment);
        cart = findViewById(R.id.cartbutton);
        home = findViewById(R.id.homebutton);
        EditProfile = findViewById(R.id.EditProfile);
        Orders = findViewById(R.id.Orders);
        mylist = findViewById(R.id.Products_listview);
        arr = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mylist.setAdapter(arr);
        Intent ii = getIntent();
        userId = ii.getStringExtra("userId");
        search = findViewById(R.id.search);
        searchButton = findViewById(R.id.imageView3);
        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(view -> scanCode());
        iv_mic = findViewById(R.id.iv_mic);
        iv_mic.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");
            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
            } catch (Exception e) {
                Toast.makeText(ProductsSearch.this, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        searchButton.setOnClickListener(v -> getAllProducts());
        mylist.setOnItemClickListener((parent, view, position, id) -> {
            Intent products_Det = new Intent(ProductsSearch.this, ProductsDetails.class);
            products_Det.putExtra("Prod_name", arr.getItem(position));
            products_Det.putExtra("Prod_id", ids.get(position));
            products_Det.putExtra("userId", userId);
            startActivity(products_Det);
        });
        cart.setOnClickListener(v -> {
            Intent i = new Intent(ProductsSearch.this, ShoppingCart.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        Orders.setOnClickListener(v -> {
            Intent i = new Intent(ProductsSearch.this, Current_Orders.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        home.setOnClickListener(v -> {
            Intent i = new Intent(ProductsSearch.this, HomeActivity.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        payment.setOnClickListener(v -> {
            Intent i = new Intent(ProductsSearch.this, AddPayment.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        EditProfile.setOnClickListener(v -> {
            Intent i = new Intent(ProductsSearch.this, ShowProfile.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                res = Objects.requireNonNull(result).get(0);
                search.setText(res);
            }
        }
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result ->
    {
        if (result.getContents() != null) {
            bar = result.getContents();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Products")
                    .whereEqualTo("id", bar)
                    .get().addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.size() == 0) {
                            Toast.makeText(ProductsSearch.this, "add a Product First ", Toast.LENGTH_SHORT).show();
                        } else {
                            Products temp = queryDocumentSnapshots.getDocuments().get(0).toObject(Products.class);
                            if (temp != null) {
                                id = temp.getId();
                                Intent products_Det = new Intent(ProductsSearch.this, ProductsDetails.class);
                                products_Det.putExtra("Prod_name", temp.getName());
                                products_Det.putExtra("Prod_id", temp.getId());
                                products_Det.putExtra("userId", userId);
                                startActivity(products_Det);
                            }
                        }
                    });
        }
    });

    void getAllProducts() {
        arr.clear();
        Check errorChecker = new Check();
        if (!errorChecker.EditTextIsEmpty(search).equals("")) {
            Toast.makeText(this, "please type any search text first", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Products").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.size() == 0) {
                        Toast.makeText(ProductsSearch.this, "no product with this name", Toast.LENGTH_SHORT).show();
                    } else {
                        for (DocumentSnapshot d1 : queryDocumentSnapshots) {
                            Products getTemp = d1.toObject(Products.class);
                            if (getTemp != null && getTemp.getName().contains(search.getText().toString())) {
                                ids.add(getTemp.getId());
                                arr.add(getTemp.getName());
                            }
                        }
                    }
                });

    }
}