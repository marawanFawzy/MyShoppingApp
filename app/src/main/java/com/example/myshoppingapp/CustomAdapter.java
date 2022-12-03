package com.example.myshoppingapp;

import android.content.Context;
import android.database.Cursor;
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

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<ProductClass> {
    public ArrayList<ProductClass> records;
    public ArrayList<String> quantity;
    ShoppingDatabase sdb;
    public int count = 0;


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
        Button plusBtn;
        Button minusBtn;
        ImageButton deleteBtn;
        sdb = new ShoppingDatabase(getContext());
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
            int qty = 0;
            Cursor c = sdb.getProductInfo(Integer.parseInt(item.id) ,Integer.parseInt(item.cat_id));
            if (!c.isAfterLast()) {
                qty = c.getInt(3);
            }
            int value = Integer.parseInt(prodQuantity.getText().toString());
            if (qty == value) {
                Toast.makeText(getContext(), "this is max quantity", Toast.LENGTH_SHORT).show();
            } else {
                String newValue = String.valueOf(value + 1);
                prodQuantity.setText(newValue);
            }
            Integer q = Integer.parseInt(prodQuantity.getText().toString());
            sdb.editQuantity(Integer.parseInt(item.id), q);
        });

        minusBtn.setOnClickListener(v -> {
            int value = Integer.parseInt(prodQuantity.getText().toString());
            if (value != 1) {
                String newValue = String.valueOf(value - 1);
                prodQuantity.setText(newValue);
            }
            Integer q = Integer.parseInt(prodQuantity.getText().toString());
            sdb.editQuantity(Integer.parseInt(item.id), q);
        });

        deleteBtn.setOnClickListener(v -> {
            sdb.deleteItem(Integer.parseInt(item.id));
            records.remove(position);
            Toast.makeText(getContext(), "Item Deleted", Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        });

        return convertView;
    }

    public ArrayList<String> getQuantity() {
        return quantity;
    }
}
