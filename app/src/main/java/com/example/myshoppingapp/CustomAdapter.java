package com.example.myshoppingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.myshoppingapp.firebase.Cart;
import com.example.myshoppingapp.firebase.Orders;
import com.example.myshoppingapp.firebase.Products;
import com.google.firebase.firestore.FirebaseFirestore;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Products> {
    public ArrayList<Products> records;
    public double total = 0, time = 0;
    boolean report;
    boolean delivered;
    boolean first = true;
    ImageView plus, minus;
    CircleImageView ProductImage;
    String photo, userId, OrderId;
    int tempQuantity;

    public CustomAdapter(@NonNull Context context, int resource, ArrayList<Products> records, boolean report, boolean delivered, String userId, String OrderId) {
        super(context, resource, records);
        this.records = records;
        this.report = report;
        this.delivered = delivered;
        this.userId = userId;
        this.OrderId = OrderId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Products item = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_custom_adapter, parent, false);
        TextView prodQuantity, productName, productPrice;
        ImageButton deleteBtn, addFeedBack;
        ProductImage = convertView.findViewById(R.id.ProductImageAdapter);
        prodQuantity = convertView.findViewById(R.id.qquantityeditText4);
        productName = convertView.findViewById(R.id.nameeditText2);
        productPrice = convertView.findViewById(R.id.priceeditText3);
        plus = convertView.findViewById(R.id.imageViewPlus);
        minus = convertView.findViewById(R.id.imageViewMinus);
        deleteBtn = convertView.findViewById(R.id.delete_button);
        addFeedBack = convertView.findViewById(R.id.addFeedBack);
        productName.setText(item.getName());
        prodQuantity.setText(String.valueOf(item.getQuantity()));
        productPrice.setText(String.valueOf(item.getPrice()));
        photo = item.getPhoto();
        ProductImage.setImageBitmap(StringToBitMap(photo));
        if (delivered) {
            deleteBtn.setVisibility(View.GONE);
            plus.setVisibility(View.GONE);
            minus.setVisibility(View.GONE);
            addFeedBack.setVisibility(View.VISIBLE);
            addFeedBack.setOnClickListener(v -> {
                Intent i = new Intent(parent.getContext(), AddFeedBackOnItem.class);
                i.putExtra("Prod_id", item.getId());
                parent.getContext().startActivity(i);
            });
        } else {
            addFeedBack.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.VISIBLE);
            plus.setVisibility(View.VISIBLE);
            minus.setVisibility(View.VISIBLE);
        }
        if (report) {
            deleteBtn.setVisibility(View.GONE);
            addFeedBack.setVisibility(View.VISIBLE);
            addFeedBack.setOnClickListener(v -> {
                Intent i = new Intent(parent.getContext(), ShowFeedBack.class);
                i.putExtra("Prod_id", item.getId());
                parent.getContext().startActivity(i);
            });
            plus.setVisibility(View.GONE);
            minus.setVisibility(View.GONE);
        } else {
            prodQuantity.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            productName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            productPrice.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        if (!OrderId.equals("Cart") && first) {
            first = false;
            tempQuantity = item.getQuantity();
        }

        plus.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Products").document(item.getId()).get().addOnSuccessListener(documentSnapshot -> {
                Products temp = documentSnapshot.toObject(Products.class);
                int MaxQuantity;
                if (OrderId.equals("Cart")) MaxQuantity = temp.getQuantity();
                else MaxQuantity = temp.getQuantity() + tempQuantity;
                int value = Integer.parseInt(prodQuantity.getText().toString());
                if (MaxQuantity == value) {
                    Toast.makeText(getContext(), "this is max quantity", Toast.LENGTH_SHORT).show();
                } else {
                    total += (Double.parseDouble(productPrice.getText().toString()))-((Double.parseDouble(productPrice.getText().toString()))*temp.getDiscount());
                    int newValue = value + 1;
                    prodQuantity.setText(String.valueOf(newValue));
                    records.get(position).setQuantity(newValue);
                    if (OrderId.equals("Cart")) {
                        db.collection("Cart").whereEqualTo("customerId", userId).get().addOnSuccessListener(queryDocumentSnapshots -> {
                            Cart newTemp = queryDocumentSnapshots.getDocuments().get(0).toObject(Cart.class);
                            for (int i = 0; i < newTemp.getProducts().size(); i++)
                                if (newTemp.getProducts().get(i).getId().equals(item.getId())) {
                                    newTemp.getProducts().get(i).setQuantity(newValue);
                                    break;
                                }
                            db.collection("Cart").document(newTemp.getId()).set(newTemp);
                        });
                    } else {
                        db.collection("Orders").document(OrderId).get().addOnSuccessListener(queryDocumentSnapshots -> {
                            Orders Order = queryDocumentSnapshots.toObject(Orders.class);
                            Cart newTemp = Order.getCart();
                            for (int i = 0; i < newTemp.getProducts().size(); i++)
                                if (newTemp.getProducts().get(i).getId().equals(item.getId())) {
                                    newTemp.getProducts().get(i).setQuantity(newValue);
                                    Order.setTotal(total);
                                    db.collection("Orders").document(Order.getId()).set(Order);
                                    notifyDataSetChanged();
                                    break;
                                }
                        });
                    }
                }
            });
        });

        minus.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            int value = Integer.parseInt(prodQuantity.getText().toString());
            if (1 == value) {
                Toast.makeText(getContext(), "this is min quantity", Toast.LENGTH_SHORT).show();
            } else {
                total -= (Double.parseDouble(productPrice.getText().toString()))-((Double.parseDouble(productPrice.getText().toString()))*item.getDiscount());
                int newValue = value - 1;
                prodQuantity.setText(String.valueOf(newValue));
                records.get(position).setQuantity(newValue);
                if (OrderId.equals("Cart")) {
                    db.collection("Cart").whereEqualTo("customerId", userId).get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                Cart newTemp = queryDocumentSnapshots.getDocuments().get(0).toObject(Cart.class);
                                for (int i = 0; i < newTemp.getProducts().size(); i++)
                                    if (newTemp.getProducts().get(i).getId().equals(item.getId())) {
                                        newTemp.getProducts().get(i).setQuantity(newValue);
                                        break;
                                    }
                                db.collection("Cart").document(newTemp.getId()).set(newTemp);
                            });
                } else {
                    db.collection("Orders").document(OrderId).get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                Orders order = queryDocumentSnapshots.toObject(Orders.class);
                                Cart newTemp = order.getCart();
                                for (int i = 0; i < newTemp.getProducts().size(); i++)
                                    if (newTemp.getProducts().get(i).getId().equals(item.getId())) {
                                        newTemp.getProducts().get(i).setQuantity(newValue);
                                        order.setTotal(total);
                                        break;
                                    }
                                notifyDataSetChanged();
                                db.collection("Orders").document(order.getId()).set(order);
                            });
                }
            }
        });

        deleteBtn.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            if (OrderId.equals("Cart")) {
                db.collection("Cart").whereEqualTo("customerId", userId).get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            Cart newTemp = queryDocumentSnapshots.getDocuments().get(0).toObject(Cart.class);
                            {
                                double newTime = -1;
                                newTemp.getProducts().remove(position);
                                records.remove(position);
                                if (newTemp.getProducts().size() != 0) {
                                    total -= (newTemp.getProducts().get(position).getPrice() * newTemp.getProducts().get(position).getQuantity())*item.getDiscount();
                                    for (int i = 0; i < newTemp.getProducts().size(); i++) {
                                        if (newTemp.getProducts().get(i).getDays_For_Delivery() > newTime)
                                            newTime = newTemp.getProducts().get(i).getDays_For_Delivery();
                                    }
                                    time = newTime;
                                } else {
                                    time = 0;
                                }
                                notifyDataSetChanged();
                            }
                            db.collection("Cart").document(newTemp.getId()).set(newTemp);
                        });
            } else {
                db.collection("Orders").document(OrderId).get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            Orders order = queryDocumentSnapshots.toObject(Orders.class);
                            Cart newTemp = order.getCart();
                            {
                                double newTime = -1;
                                newTemp.getProducts().remove(position);
                                records.remove(position);
                                if (newTemp.getProducts().size() != 0) {
                                    total -= newTemp.getProducts().get(position).getPrice() * newTemp.getProducts().get(position).getQuantity();
                                    for (int i = 0; i < newTemp.getProducts().size(); i++) {
                                        if (newTemp.getProducts().get(i).getDays_For_Delivery() > newTime)
                                            newTime = newTemp.getProducts().get(i).getDays_For_Delivery();
                                    }
                                    time = newTime;
                                } else {
                                    time = 0;
                                }
                                order.setEstimatedTime(time);
                                notifyDataSetChanged();
                            }
                            db.collection("Orders").document(order.getId()).set(order);
                        });
            }
        });

        return convertView;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}

