package com.example.myshoppingapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProductsDetails extends AppCompatActivity {
    EditText e_name;
    EditText e_price;
    EditText e_qty;
    Button add;
    Button cart;
    Button home;
    ShoppingDatabase sdb = new ShoppingDatabase(this);
    int x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_details);

        e_name = (EditText) findViewById(R.id.editTextP_Name);
        e_price = (EditText) findViewById(R.id.editTextPrice);
        e_qty = (EditText) findViewById(R.id.quantityeditText);
        add = (Button) findViewById(R.id.buttonAddtoCart);
        cart = (Button) findViewById(R.id.cartbutton);
        home = (Button) findViewById(R.id.homebutton);

        Intent i = getIntent();
        String y = i.getStringExtra("Prod_name");
        x = i.getIntExtra("Prod_id", 0);
        String z;
        String k;

        Cursor cur = sdb.Get_SelectedProduct_details(x);
        if (cur != null) {
            z = cur.getString(cur.getColumnIndex("Price"));
            k = cur.getString(cur.getColumnIndex("Quantity"));
            e_name.setText(y);
            e_price.setText(z + "EGP");
            e_qty.setText(k);
            //cur.moveToNext();

            if (e_qty.getText().toString().equals("0")) {
                add.setEnabled(false);
                Toast.makeText(getApplicationContext(), "Sorry, The quantity carried out", Toast.LENGTH_SHORT).show();
            } else {
                add.setOnClickListener(v -> {
                    add.setEnabled(true);
                    sdb.addtoCart(x, 1);
                });
            }
        }

        cart.setOnClickListener(v -> {
            Intent i1 = new Intent(ProductsDetails.this, ShoppingCart.class);
            startActivity(i1);
        });

        home.setOnClickListener(v -> {
            Intent i12 = new Intent(ProductsDetails.this, HomeActivity.class);
            startActivity(i12);
        });
    }
}
