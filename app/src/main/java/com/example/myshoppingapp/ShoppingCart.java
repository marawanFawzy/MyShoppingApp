package com.example.myshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myshoppingapp.firebase.Cart;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class ShoppingCart extends AppCompatActivity {
    ShoppingDatabase sdb;
    ListView myList;
    ArrayList<ProductClass> arrayOfProducts;
    CustomAdapter adapter;
    double total = 0.0;
    Button addNewItem, makeOrder, showPrice, home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> ids, NamesArray, quantityArray , PricesArray;
        setContentView(R.layout.activity_shopping_cart);
        addNewItem = findViewById(R.id.addnewbutton);
        makeOrder = findViewById(R.id.Orderbutton2);
        showPrice = findViewById(R.id.totalpricebutton3);
        home = findViewById(R.id.homebutton);
        Intent ii = getIntent();
        String userId = ii.getStringExtra("userId");
        myList = findViewById(R.id.mylist);
        ids = new ArrayList<>();
        NamesArray = new ArrayList<>();
        PricesArray = new ArrayList<>();
        quantityArray = new ArrayList<>();
        sdb = new ShoppingDatabase(this);
        getCart(userId, NamesArray, PricesArray, quantityArray, ids);


        makeOrder.setOnClickListener(v -> {
            if (myList.getCount() > 0) {
                Intent i = new Intent(ShoppingCart.this, MakeOrder.class);
                i.putExtra("productsID", NamesArray);
                i.putExtra("productsQuantity", quantityArray);
                i.putExtra("products_cat_ids", PricesArray);
                i.putExtra("userId", userId);
                startActivity(i);
            } else
                Toast.makeText(getApplicationContext(), "Shopping Cart Is Empty", Toast.LENGTH_SHORT).show();

        });

        showPrice.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Total Price is " + adapter.total + "EGP", Toast.LENGTH_LONG).show();
        });

        home.setOnClickListener(v -> {
            Intent i = new Intent(ShoppingCart.this, HomeActivity.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        addNewItem.setOnClickListener(v -> {
            Intent i = new Intent(ShoppingCart.this, HomeActivity.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });

    }

    public void InsertIntoAdapter(String userId,ArrayList<String> ids, ArrayList<String> namesArray, ArrayList<String> pricesArray, ArrayList<String> quantityArray) {
        ProductClass product;
        arrayOfProducts = new ArrayList<>();
        for (int i = 0; i < namesArray.size(); i++) {
            String id = ids.get(i);
            String name = namesArray.get(i);
            String price = pricesArray.get(i);
            String quantity = quantityArray.get(i);
            total += Double.parseDouble(price) * Double.parseDouble(quantity);
            product = new ProductClass(userId,id, name, price, quantity);
            arrayOfProducts.add(product);
        }
        adapter = new CustomAdapter(this, 0, arrayOfProducts);
        adapter.total = total;
        myList.setAdapter(adapter);
    }

    void getCart(String userId, ArrayList<String> NamesArray, ArrayList<String> PricesArray, ArrayList<String> quantityArray, ArrayList<String> ids) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Cart")
                .whereEqualTo("customerId", userId)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.getDocuments().size() == 0) {
                        Toast.makeText(this, "add any product to your cart first", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        DocumentSnapshot d = queryDocumentSnapshots.getDocuments().get(0);
                        Cart temp = d.toObject(Cart.class);
                        NamesArray.addAll(temp.getNames());
                        PricesArray.addAll(temp.getPrices());
                        quantityArray.addAll(temp.getProductsQuantity());
                        ids.addAll(temp.getProducts());
                    }
                    InsertIntoAdapter(userId, ids, NamesArray, PricesArray, quantityArray);
                });
    }

}
