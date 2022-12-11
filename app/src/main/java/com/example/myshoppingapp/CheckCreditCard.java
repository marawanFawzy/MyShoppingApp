package com.example.myshoppingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myshoppingapp.firebase.CreditCard;
import com.example.myshoppingapp.firebase.Customers;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CheckCreditCard extends AppCompatActivity {
    private final ArrayList<String> paths = new ArrayList<>();
    private final ArrayList<String> CustomerId = new ArrayList<>();
    private final ArrayList<CreditCard> creditCards = new ArrayList<>();
    EditText number , month , year , cvv;
    int SelectedPosition = 0;
    Button buttonReject , buttonApprove;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_credit_card);
        paths.add("Select Credit Card");
        CustomerId.add("");
        creditCards.add(new CreditCard());
        spinner = findViewById(R.id.spinnerSelectCreditCard);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(CheckCreditCard.this, android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        FillWithWaitingCards();
        number = findViewById(R.id.CardNumber);
        month = findViewById(R.id.ExpireDateMonth);
        year = findViewById(R.id.ExpireDateYear);
        cvv = findViewById(R.id.CVV);
        buttonReject = findViewById(R.id.buttonReject);
        buttonReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateStatus("rejected");
            }
        });
        buttonApprove = findViewById(R.id.buttonApprove);
        buttonApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateStatus("approved");
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedPosition = position;
                if(position!= 0)
                {
                    buttonApprove.setEnabled(true);
                    buttonReject.setEnabled(true);
                    number.setText(creditCards.get(position).getNumber());
                    month.setText(creditCards.get(position).getExpireDateMonth());
                    year.setText(creditCards.get(position).getExpireDateYear());
                    cvv.setText(creditCards.get(position).getCVV());
                }
                else
                {
                    buttonApprove.setEnabled(false);
                    buttonReject.setEnabled(false);
                    number.setText("");
                    month.setText("");
                    year.setText("");
                    cvv.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    void FillWithWaitingCards()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Customers").whereEqualTo("creditCard.status" , "waiting").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.getDocuments().size() ==0)
                {
                    Toast.makeText(CheckCreditCard.this, "no waiting credit cards", Toast.LENGTH_SHORT).show();
                }
                else{
                    for(int i = 0 ; i < queryDocumentSnapshots.getDocuments().size();i++)
                    {
                        Customers temp = queryDocumentSnapshots.getDocuments().get(i).toObject(Customers.class);
                        CustomerId.add(temp.getId());
                        CreditCard newTemp = temp.getCreditCard();
                        creditCards.add(newTemp);
                        paths.add(newTemp.getNumber());
                    }

                }
            }
        });
    }
    void UpdateStatus(String newStatus)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Customers").document(CustomerId.get(SelectedPosition)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Customers temp = documentSnapshot.toObject(Customers.class);
                CreditCard newTemp = temp.getCreditCard();
                newTemp.setStatus(newStatus);
                temp.setCreditCard(newTemp);
                db.collection("Customers").document(CustomerId.get(SelectedPosition)).set(temp).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CheckCreditCard.this, "this credit card is " + newStatus, Toast.LENGTH_SHORT).show();
                        paths.remove(SelectedPosition);
                        CustomerId.remove(SelectedPosition);
                        creditCards.remove(SelectedPosition);
                        spinner.setSelection(0);
                    }
                });
            }
        });
    }
}