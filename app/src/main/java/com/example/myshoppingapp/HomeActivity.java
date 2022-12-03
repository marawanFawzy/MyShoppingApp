package com.example.myshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    ShoppingDatabase sdb = new ShoppingDatabase(this);
    ListView listView;
    Button category, searchButton, cart, home;
    EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ArrayAdapter<String> arr = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        setContentView(R.layout.activity_home);
        cart = findViewById(R.id.cartbutton);
        home = findViewById(R.id.homebutton);
        searchText = findViewById(R.id.search);
        searchButton = findViewById(R.id.searchbtn);
        category = findViewById(R.id.show_cat);
        listView = findViewById(R.id.cat_listview);
        listView.setAdapter(arr);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            TextView e = (TextView) view;
            String y = e.getText().toString();
            Intent products = new Intent(HomeActivity.this, Products.class);
            products.putExtra("cat_id", position + 1);
            products.putExtra("cat_name", y);
            startActivity(products);
        });
        searchButton.setOnClickListener(v1 -> {
            String text = searchText.getText().toString();
            Cursor cu = sdb.Search_By_Text(text);  //so2al el while loop!

            Intent i = new Intent(HomeActivity.this, ProductsDetails.class);
            i.putExtra("Prod_name", text);
            startActivity(i);
        });
        category.setOnClickListener(v -> {
            arr.clear();
            Cursor cc = sdb.Select_Categories();
            while (!cc.isAfterLast()) {
                arr.add(cc.getString(1));
                cc.moveToNext();
            }
            arr.notifyDataSetChanged();

        });
        cart.setOnClickListener(v -> {
            Intent i = new Intent(HomeActivity.this, ShoppingCart.class);
            startActivity(i);
        });
    }
}
