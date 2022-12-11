package com.example.myshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myshoppingapp.firebase.CreditCard;
import com.example.myshoppingapp.firebase.Customers;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.atomic.AtomicReference;

public class AddPayment extends AppCompatActivity {
    EditText CardNumber, ExpireDateMonth, ExpireDateYear, CVV;
    Button addCreditCard;
    String userId;
    TextView PrevCard, status;
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);
        Intent ii = getIntent();
        userId = ii.getStringExtra("userId");
        PrevCard = findViewById(R.id.PrevCard);
        status = findViewById(R.id.Status);
        status.setVisibility(View.VISIBLE);
        CardNumber = findViewById(R.id.CardNumber);
        ExpireDateMonth = findViewById(R.id.ExpireDateMonth);
        ExpireDateYear = findViewById(R.id.ExpireDateYear);
        CVV = findViewById(R.id.CVV);
        addCreditCard = findViewById(R.id.AddPayment);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        AtomicReference<Customers> temp = new AtomicReference<>(new Customers());
        db.collection("Customers").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            temp.set(documentSnapshot.toObject(Customers.class));
            if(temp.get().getCreditCard() != null)
            {
                PrevCard.setText(temp.get().getCreditCard().getNumber());
                status.setText("your credit Card status is " + temp.get().getCreditCard().getStatus() +"\n\n"+ "(press to edit info)");
            }
            else
            {
                PrevCard.setText("no current credit Card ");
                status.setVisibility(View.GONE);
                flag = false;
            }
        });
        addCreditCard.setOnClickListener(v -> {
            if (CardNumber.getText().toString().equals("") || CardNumber.getText().toString().length() != 16)
                Toast.makeText(AddPayment.this, "please fill the credit card number it must be 16 number", Toast.LENGTH_SHORT).show();
            else if (ExpireDateMonth.getText().toString().equals(""))
                Toast.makeText(AddPayment.this, "please fill Month", Toast.LENGTH_SHORT).show();
            else if (ExpireDateYear.getText().toString().equals(""))
                Toast.makeText(AddPayment.this, "please fill Year", Toast.LENGTH_SHORT).show();
            else if (CVV.getText().toString().equals("") || CVV.getText().toString().length() != 3)
                Toast.makeText(AddPayment.this, "please fill CVV it must be 3 numbers ", Toast.LENGTH_SHORT).show();
            else {
                CreditCard newTemp = new CreditCard(CardNumber.getText().toString(), ExpireDateMonth.getText().toString(), ExpireDateYear.getText().toString(), CVV.getText().toString());
                if(flag &&(temp.get().getCreditCard().getCVV() != newTemp.getCVV() ||
                        temp.get().getCreditCard().getNumber() != newTemp.getNumber() ||
                        temp.get().getCreditCard().getExpireDateMonth() != newTemp.getExpireDateMonth() ||
                        temp.get().getCreditCard().getExpireDateYear() != newTemp.getExpireDateYear()))
                {
                    newTemp.setStatus("waiting");
                    Toast.makeText(this, "card data changed it will be reviewed from admin soon", Toast.LENGTH_SHORT).show();
                }
                temp.get().setCreditCard(newTemp);
                db.collection("Customers").document(userId).set(temp.get()).addOnSuccessListener(unused -> {
                    Toast.makeText(AddPayment.this, "added this credit Card", Toast.LENGTH_SHORT).show();
                    CardNumber.setText("");
                    ExpireDateMonth.setText("");
                    ExpireDateYear.setText("");
                    CVV.setText("");
                    PrevCard.setText(newTemp.getNumber());
                    status.setVisibility(View.VISIBLE);
                    status.setText("your credit Card status is " + newTemp.getStatus() +"\n\n"+ "(press to edit info)");
                    temp.get().setCreditCard(newTemp);
                });
            }
        });
        PrevCard.setOnClickListener(v -> {
            if(flag) {
                CardNumber.setText(temp.get().getCreditCard().getNumber());
                ExpireDateMonth.setText(temp.get().getCreditCard().getExpireDateMonth());
                ExpireDateYear.setText(temp.get().getCreditCard().getExpireDateYear());
                CVV.setText(temp.get().getCreditCard().getCVV());
            }
        });
        status.setOnClickListener(v -> {
            if(flag) {
                CardNumber.setText(temp.get().getCreditCard().getNumber());
                ExpireDateMonth.setText(temp.get().getCreditCard().getExpireDateMonth());
                ExpireDateYear.setText(temp.get().getCreditCard().getExpireDateYear());
                CVV.setText(temp.get().getCreditCard().getCVV());
            }
        });
    }
}