package com.example.myshoppingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myshoppingapp.firebase.Cart;
import com.example.myshoppingapp.firebase.Products;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<ProductClass> {
    public ArrayList<ProductClass> records;
    public ArrayList<String> quantity;
    public double total = 0 ;


    public CustomAdapter(@NonNull Context context, int resource, ArrayList<ProductClass> records) {
        super(context, resource, records);
        this.records = records;
        quantity = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ProductClass item = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_custom_adapter, parent, false);
        TextView prodQuantity;
        TextView productName;
        TextView productPrice;
        Button plusBtn, minusBtn;
        ImageButton deleteBtn;
        prodQuantity = convertView.findViewById(R.id.qquantityeditText4);
        productName = convertView.findViewById(R.id.nameeditText2);
        productPrice = convertView.findViewById(R.id.priceeditText3);
        plusBtn = convertView.findViewById(R.id.plusbutton);
        minusBtn = convertView.findViewById(R.id.Minusbutton3);
        deleteBtn = convertView.findViewById(R.id.delete_button);
        productName.setText(item.name);
        prodQuantity.setText(item.quantity);
        productPrice.setText(item.price);


        plusBtn.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Products").document(item.id).get().addOnSuccessListener(documentSnapshot -> {
                Products temp = documentSnapshot.toObject(Products.class);
                int MaxQuantity = temp.getQuantity();
                int value = Integer.parseInt(prodQuantity.getText().toString());
                if (MaxQuantity == value) {
                    Toast.makeText(getContext(), "this is max quantity", Toast.LENGTH_SHORT).show();
                } else {
                    total += Double.parseDouble(productPrice.getText().toString());
                    String newValue = String.valueOf(value + 1);
                    prodQuantity.setText(newValue);
                    db.collection("Cart").whereEqualTo("customerId", item.customerId).get().addOnSuccessListener(queryDocumentSnapshots -> {
                        Cart newTemp = queryDocumentSnapshots.getDocuments().get(0).toObject(Cart.class);
                        ArrayList<String> newQ = new ArrayList<>();
                        for (int i = 0; i < newTemp.getProducts().size(); i++)
                            if (newTemp.getProducts().get(i).equals(item.id))
                                newQ.add(newValue);
                            else
                                newQ.add(newTemp.getProductsQuantity().get(i));
                        newTemp.setProductsQuantity(newQ);
                        db.collection("Cart").document(newTemp.getId()).set(newTemp);
                    });
                }
            });
        });

        minusBtn.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            int value = Integer.parseInt(prodQuantity.getText().toString());
            if (1 == value) {
                Toast.makeText(getContext(), "this is min quantity", Toast.LENGTH_SHORT).show();
            }
            else {
                total -= Double.parseDouble(productPrice.getText().toString());
                String newValue = String.valueOf(value - 1);
                prodQuantity.setText(newValue);
                db.collection("Cart").whereEqualTo("customerId", item.customerId).get().addOnSuccessListener(queryDocumentSnapshots -> {
                    Cart newTemp = queryDocumentSnapshots.getDocuments().get(0).toObject(Cart.class);
                    ArrayList<String> newQ = new ArrayList<>();
                    for (int i = 0; i < newTemp.getProducts().size(); i++)
                        if (newTemp.getProducts().get(i).equals(item.id))
                            newQ.add(newValue);
                        else
                            newQ.add(newTemp.getProductsQuantity().get(i));
                    newTemp.setProductsQuantity(newQ);
                    db.collection("Cart").document(newTemp.getId()).set(newTemp);
                });
            }
        });

        deleteBtn.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Cart").whereEqualTo("customerId", item.customerId).get().addOnSuccessListener(queryDocumentSnapshots -> {
                Cart newTemp = queryDocumentSnapshots.getDocuments().get(0).toObject(Cart.class);
                for (int i = 0; i < newTemp.getProducts().size(); i++)
                    if (newTemp.getProducts().get(i).equals(item.id))
                    {
                        newTemp.getProducts().remove(i);
                        total -= Double.parseDouble(newTemp.getPrices().get(i))*Double.parseDouble(newTemp.getProductsQuantity().get(i));
                        newTemp.getPrices().remove(i);
                        newTemp.getProductsQuantity().remove(i);
                        newTemp.getNames().remove(i);
                        records.remove(i);
                        notifyDataSetChanged();
                        break;
                    }
                db.collection("Cart").document(newTemp.getId()).set(newTemp);
            });
        });
        return convertView;
    }
}
