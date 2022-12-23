package com.example.myshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myshoppingapp.firebase.Categories;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    ListView listView;
    Button category, search;
    ArrayAdapter<String> arr;
    ImageView cart, home, EditProfile, Orders;
    ArrayList<String> catIds = new ArrayList<>();
    ImageButton payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arr = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        setContentView(R.layout.activity_home);
        payment = findViewById(R.id.Payment);
        cart = findViewById(R.id.cartbutton);
        home = findViewById(R.id.homebutton);
        EditProfile = findViewById(R.id.EditProfile);
        Orders = findViewById(R.id.Orders);
        search = findViewById(R.id.searchbtn);
        category = findViewById(R.id.show_cat);
        listView = findViewById(R.id.cat_listview);
        listView.setAdapter(arr);
        Intent ii = getIntent();
        String userId = ii.getStringExtra("userId");
        listView.setOnItemClickListener((parent, view, position, id) -> {
            TextView e = (TextView) view;
            String y = e.getText().toString();
            Intent products = new Intent(HomeActivity.this, ProductsActivity.class);
            products.putExtra("cat_id", catIds.get(position));
            products.putExtra("cat_name", y);
            products.putExtra("userId", userId);
            startActivity(products);
        });
        category.setOnClickListener(v -> getAllCategories());
        cart.setOnClickListener(v -> {
            Intent i = new Intent(HomeActivity.this, ShoppingCart.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        search.setOnClickListener(v -> {
            Intent i = new Intent(HomeActivity.this, ProductsSearch.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        payment.setOnClickListener(v -> {
            Intent i = new Intent(HomeActivity.this, AddPayment.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        EditProfile.setOnClickListener(v -> {
            Intent i = new Intent(HomeActivity.this, ShowProfile.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        Orders.setOnClickListener(v -> {
            Intent i = new Intent(HomeActivity.this, Current_Orders.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
    }


    void getAllCategories() {
        arr.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Categories").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.size() == 0) {
                        Toast.makeText(HomeActivity.this, "add a category First ", Toast.LENGTH_SHORT).show();
                    } else {
                        for (DocumentSnapshot d : queryDocumentSnapshots) {
                            Categories temp = d.toObject(Categories.class);
                            if (temp != null) {
                                arr.add(temp.getName());
                                catIds.add(temp.getId());
                            }
                        }
                    }
                });
    }
}
