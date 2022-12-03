package com.example.myshoppingapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class ShoppingCart extends AppCompatActivity
{
    ShoppingDatabase sdb;
    ListView myList;
    ArrayList<ProductClass> arrayOfProducts;
    CustomAdapter adapter;
    ArrayList<String> iDArray;
    ArrayList<String> catIdArray;
    ArrayList<String> quantityArray;
    //String id;
    Button addNewItem;
    Button makeOrder ;
    Button showPrice ;
    Button home ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        addNewItem = findViewById(R.id.addnewbutton);
        makeOrder = findViewById(R.id.Orderbutton2);
        showPrice = findViewById(R.id.totalpricebutton3);
        home = findViewById(R.id.homebutton);

        myList = findViewById(R.id.mylist);
        iDArray = new ArrayList<>();
        catIdArray = new ArrayList<>();
        sdb = new ShoppingDatabase(this);
        Cursor cursor = sdb.fetchCart();
        if (!cursor.isAfterLast())
        {
            while (!cursor.isAfterLast())
            {
                iDArray.add(String.valueOf(cursor.getInt(0)));
                catIdArray.add(String.valueOf(cursor.getInt(2)));
                cursor.moveToNext();
            }
        }
        else
            Toast.makeText(this, "Shopping Cart is empty", Toast.LENGTH_SHORT).show();
        InsertIntoAdapter();

       addNewItem.setOnClickListener(v -> {
           Intent i = new Intent(ShoppingCart.this,HomeActivity.class);
           startActivity(i);
       });

       makeOrder.setOnClickListener(v -> {
           if (myList.getCount()>0)
           {
               quantityArray = new ArrayList<>();
               quantityArray = adapter.getQuantity();

               Intent in = new Intent(ShoppingCart.this, MakeOrder.class);
               in.putExtra("productsID", iDArray);
               in.putExtra("productsQuantity", quantityArray);
               startActivity(in);
           }
           else
               Toast.makeText(getApplicationContext() , "Shopping Cart Is Empty",Toast.LENGTH_SHORT).show();

       });


        showPrice.setOnClickListener(v -> {
            double total = 0.0;
            Cursor cursor1 = sdb.fetchCart();
            while (!cursor1.isAfterLast())
            {
                Integer id = cursor1.getInt(0);
                Integer q = cursor1.getInt(1);
                Integer cat_id = cursor1.getInt(2);
                String price = sdb.getProductPrice(id , cat_id);
                Double prodPrice = Double.parseDouble(price);
                total += q*prodPrice;
                cursor1.moveToNext();
            }
            Toast.makeText(getApplicationContext(), "Total Price is " + total + "EGP" , Toast.LENGTH_LONG).show();
        });

        home.setOnClickListener(v -> {
            Intent i = new Intent(ShoppingCart.this, HomeActivity.class);
            startActivity(i);
        });

    }

    public void InsertIntoAdapter()
    {
        ProductClass product;
        arrayOfProducts = new ArrayList<>();

        sdb = new ShoppingDatabase(this);
        for (int i = 0; i < iDArray.size(); i++) {
            Cursor cursor = sdb.getProductInfo(Integer.parseInt(iDArray.get(i)) , Integer.parseInt(catIdArray.get(i)));

            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String price = cursor.getString(2);
            String quantity  = String.valueOf(sdb.getQuantity(Integer.parseInt(iDArray.get(i)) , Integer.parseInt(catIdArray.get(i))));

            product = new ProductClass(id,name, quantity, price , catIdArray.get(i));
            arrayOfProducts.add(product);
        }
        adapter = new CustomAdapter(this, 0, arrayOfProducts);
        myList.setAdapter(adapter);
    }

}
