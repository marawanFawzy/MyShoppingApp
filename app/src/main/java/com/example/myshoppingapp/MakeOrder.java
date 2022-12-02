package com.example.myshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MakeOrder extends AppCompatActivity {
    ArrayList<String> iDArray;
    ArrayList<String> quantityArray;
    EditText city;
    EditText street;
    EditText building;
    Button confirm;
    Button cart;
    Button home;
    ShoppingDatabase sdb = new ShoppingDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        cart = (Button) findViewById(R.id.cartbutton);
        home = (Button) findViewById(R.id.homebutton);
        confirm = (Button) findViewById(R.id.confirm);
        city = (EditText) findViewById(R.id.cityedittext);
        street = (EditText) findViewById(R.id.streeteditText2);
        building = (EditText) findViewById(R.id.buildingeditText3);

        iDArray = new ArrayList<String>();
        iDArray = getIntent().getStringArrayListExtra("productsID");
        quantityArray = new ArrayList<String>();
        quantityArray = getIntent().getStringArrayListExtra("productsQuantity");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c = city.getText().toString();
                String s = street.getText().toString();
                String b = building.getText().toString();
                if (c.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter Your City", Toast.LENGTH_SHORT).show();
                } else if (s.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter Your Street", Toast.LENGTH_SHORT).show();
                } else if (b.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter Your Building", Toast.LENGTH_SHORT).show();
                } else {
                    /*Intent i = new Intent(MakeOrder.this , OrderDetailsActivity.class);
                    i.putExtra("productsID" , iDArray);
                    i.putExtra("productsQuantity" , quantityArray);
                    i.putExtra("address" , c+","+s+","+b);
                    startActivity(i);*/
                    //sdb.CreateNewOrder();
                    //sdb.OrderDetails();

                }
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
