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
import com.example.myshoppingapp.helpers.Check;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddPayment extends AppCompatActivity {
    EditText CardNumber, ExpireDateMonth, ExpireDateYear, CVV;
    Button addCreditCard;
    String userId;
    TextView PrevCard, status;
    boolean flag = true;
    Customers temp;
    Check errorChecker = new Check();

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

        db.collection("Customers").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            temp = documentSnapshot.toObject(Customers.class);
            if (temp.getCreditCard() != null) {
                PrevCard.setText(temp.getCreditCard().getNumber());
                status.setText("your credit Card status is " + temp.getCreditCard().getStatus() + "\n\n" + "(press to edit info)");
            } else {
                PrevCard.setText("no current credit Card ");
                status.setVisibility(View.GONE);
                flag = false;
            }
        });
        addCreditCard.setOnClickListener(v -> {
            String checkerResult = errorChecker.EditTextIsEmpty(CardNumber, ExpireDateMonth, ExpireDateYear, CVV);
            if (!checkerResult.equals(""))
                Toast.makeText(AddPayment.this, "please fill " + checkerResult + " Data", Toast.LENGTH_SHORT).show();
            else {
                CreditCard newTemp = new CreditCard(CardNumber.getText().toString(), ExpireDateMonth.getText().toString(), ExpireDateYear.getText().toString(), Integer.parseInt(CVV.getText().toString()));
                if (flag && (temp.getCreditCard().getCVV() != newTemp.getCVV() ||
                        temp.getCreditCard().getNumber() != newTemp.getNumber() ||
                        temp.getCreditCard().getExpireDateMonth() != newTemp.getExpireDateMonth() ||
                        temp.getCreditCard().getExpireDateYear() != newTemp.getExpireDateYear())) {
                    newTemp.setStatus("waiting");
                    Toast.makeText(this, "card data changed it will be reviewed from admin soon", Toast.LENGTH_SHORT).show();
                }
                temp.setCreditCard(newTemp);
                db.collection("Customers").document(userId).set(temp).addOnSuccessListener(unused -> {
                    Toast.makeText(AddPayment.this, "added this credit Card", Toast.LENGTH_SHORT).show();
                    CardNumber.setText("");
                    ExpireDateMonth.setText("");
                    ExpireDateYear.setText("");
                    CVV.setText("");
                    PrevCard.setText(newTemp.getNumber());
                    status.setVisibility(View.VISIBLE);
                    status.setText("your credit Card status is " + newTemp.getStatus() + "\n\n" + "(press to edit info)");
                    temp.setCreditCard(newTemp);
                });
            }
        });
        PrevCard.setOnClickListener(v -> {
            if (flag) {
                CardNumber.setText(temp.getCreditCard().getNumber());
                ExpireDateMonth.setText(temp.getCreditCard().getExpireDateMonth());
                ExpireDateYear.setText(temp.getCreditCard().getExpireDateYear());
                CVV.setText(temp.getCreditCard().getCVV());
            }
        });
        status.setOnClickListener(v -> {
            if (flag) {
                CardNumber.setText(temp.getCreditCard().getNumber());
                ExpireDateMonth.setText(temp.getCreditCard().getExpireDateMonth());
                ExpireDateYear.setText(temp.getCreditCard().getExpireDateYear());
                CVV.setText(temp.getCreditCard().getCVV());
            }
        });
    }
}