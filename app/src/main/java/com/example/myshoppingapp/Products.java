package com.example.myshoppingapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Products extends AppCompatActivity {
    ShoppingDatabase sdb = new ShoppingDatabase(this);
    ListView mylist;
    TextView t;
    Button cart;
    Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        t = findViewById(R.id.cat_n_Textview);
        mylist = findViewById(R.id.Products_list);
        cart = findViewById(R.id.cartbutton);
        home = findViewById(R.id.homebutton);
        final ArrayAdapter<String> arr = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mylist.setAdapter(arr);

        int i;
        String x;
        Intent ii = getIntent();
        i = ii.getIntExtra("cat_id", 0);
        x = ii.getStringExtra("cat_name");
        t.setText(x);

        Cursor cc = sdb.Select_Products(i);
        while (!cc.isAfterLast()) {
            arr.add(cc.getString(1));
            cc.moveToNext();
        }

        mylist.setOnItemClickListener((parent, view, position, id) -> {
            String pname = ((TextView) view).getText().toString();
            Intent products_Det = new Intent(Products.this, ProductsDetails.class);
            products_Det.putExtra("Prod_name", pname); //ana 3ayza name el product 3shan a select el data 3la asaso
            products_Det.putExtra("Prod_id", position + 1);
            products_Det.putExtra("cat_id", i);
            startActivity(products_Det);
        });

        cart.setOnClickListener(v -> {
            Intent i1 = new Intent(Products.this, ShoppingCart.class);
            startActivity(i1);
        });

        home.setOnClickListener(v -> {
            Intent i12 = new Intent(Products.this, HomeActivity.class);
            startActivity(i12);
        });
    }
}
