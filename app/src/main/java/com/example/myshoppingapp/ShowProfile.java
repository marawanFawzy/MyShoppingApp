package com.example.myshoppingapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myshoppingapp.firebase.Customers;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

public class ShowProfile extends AppCompatActivity {
    DatePickerDialog.OnDateSetListener cdate;
    EditText cname, cusername, cpassword, cbirthdate, cMail, cSSN;
    RadioButton gfemale, gmale;
    FloatingActionButton Update;
    ImageButton back;
    String userId, userNameTemp = "";
    Customers CurrentCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        Intent ii = getIntent();
        userId = ii.getStringExtra("userId");
        cname = findViewById(R.id.nameeditText);
        cusername = findViewById(R.id.usernameeditText2);
        cpassword = findViewById(R.id.passwordeditText3);
        cbirthdate = findViewById(R.id.birthddateeditText4);
        cMail = findViewById(R.id.MailEditText);
        cSSN = findViewById(R.id.SSNText);
        gfemale = findViewById(R.id.female);
        gmale = findViewById(R.id.male);
        Update = findViewById(R.id.Update);
        back = findViewById(R.id.logOutB);
        LoadUser();
        disableSoftInputFromAppearing(cbirthdate);
        cdate = (datePicker, year, month, day) -> {
            month = month + 1;  //assuming month starts with zero
            String date = month + "/" + day + "/" + year;
            cbirthdate.setText(date);
        };
        cbirthdate.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(ShowProfile.this, android.R.style.Theme_Holo_Dialog_MinWidth, cdate, year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        });
        back.setOnClickListener(v -> {
            Intent i = new Intent(ShowProfile.this, HomeActivity.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
        Update.setOnClickListener(v -> {
            String gender = "";
            String un = cusername.getText().toString();
            if (gfemale.isChecked()) gender = "Female";
            else if (gmale.isChecked()) gender = "Male";
            CurrentCustomer.setName(cname.getText().toString());
            CurrentCustomer.setPassword(cpassword.getText().toString());
            CurrentCustomer.setBirthdate(new Date(cbirthdate.getText().toString()));
            CurrentCustomer.setEmail(cMail.getText().toString());
            CurrentCustomer.setSSN(cSSN.getText().toString());
            CurrentCustomer.setGender(gender);
            if (cname.getText().toString().equals(""))
                Toast.makeText(ShowProfile.this, "Please enter your Name", Toast.LENGTH_SHORT).show();
            else if (cusername.getText().toString().equals(""))
                Toast.makeText(ShowProfile.this, "Please enter your Username", Toast.LENGTH_SHORT).show();
            else if (cpassword.getText().toString().equals(""))
                Toast.makeText(ShowProfile.this, "Please enter your Password", Toast.LENGTH_SHORT).show();
            else if (gender.equals(""))
                Toast.makeText(ShowProfile.this, "Please enter your Gender", Toast.LENGTH_SHORT).show();
            else if (cbirthdate.getText().toString().equals(""))
                Toast.makeText(ShowProfile.this, "Please enter your Birth Date", Toast.LENGTH_SHORT).show();
            else if (cMail.getText().toString().equals(""))
                Toast.makeText(ShowProfile.this, "Please enter your Email", Toast.LENGTH_SHORT).show();
            else if (cSSN.getText().toString().equals(""))
                Toast.makeText(ShowProfile.this, "Please enter your SSN", Toast.LENGTH_SHORT).show();
            else {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Customers")
                        .whereEqualTo("username", un)
                        .limit(1)
                        .get().addOnSuccessListener(queryDocumentSnapshots -> {
                            if (userNameTemp.equals(cusername.getText().toString())) {
                                db.collection("Customers").document(userId).set(CurrentCustomer);
                                Intent i = new Intent(ShowProfile.this, HomeActivity.class);
                                i.putExtra("userId", userId);
                                startActivity(i);
                            } else {
                                if (queryDocumentSnapshots.size() != 0) {
                                    Toast.makeText(ShowProfile.this, "please enter a valid username ", Toast.LENGTH_SHORT).show();
                                    cusername.setText("");
                                } else {
                                    CurrentCustomer.setUsername(un);
                                    db.collection("Customers").document(userId).set(CurrentCustomer);
                                    Intent i = new Intent(ShowProfile.this, HomeActivity.class);
                                    i.putExtra("userId", userId);
                                    startActivity(i);
                                }
                            }
                        });

            }
        });
    }

    public static void disableSoftInputFromAppearing(EditText editText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setShowSoftInputOnFocus(false);
        } else {
            try {
                final Method method = EditText.class.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {
                // handle error
            }
        }
    }

    void LoadUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Customers").document(userId)
                .get().addOnSuccessListener(documentSnapshot -> {
                    CurrentCustomer = documentSnapshot.toObject(Customers.class);
                    cname.setText(CurrentCustomer.getName());
                    userNameTemp = CurrentCustomer.getUsername();
                    cusername.setText(CurrentCustomer.getUsername());
                    cpassword.setText(CurrentCustomer.getPassword());
                    cbirthdate.setText(CurrentCustomer.getBirthdate().toString());
                    cMail.setText(CurrentCustomer.getEmail());
                    cSSN.setText(CurrentCustomer.getSSN());
                    gmale.setChecked(CurrentCustomer.getGender().equals("Male"));
                    gfemale.setChecked(CurrentCustomer.getGender().equals("Female"));
                });
    }

}