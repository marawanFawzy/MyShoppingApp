package com.example.myshoppingapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myshoppingapp.firebase.Customers;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    DatePickerDialog.OnDateSetListener cdate;
    EditText cname;
    EditText cusername;
    EditText cpassword;
    EditText cbirthdate;
    EditText cjob;
    RadioButton gfemale;
    RadioButton gmale;
    Button signup;
    ShoppingDatabase sdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sdb = new ShoppingDatabase(this);
        cname = findViewById(R.id.nameeditText);
        cusername = findViewById(R.id.usernameeditText2);
        cpassword = findViewById(R.id.passwordeditText3);
        cbirthdate = findViewById(R.id.birthddateeditText4);
        cjob = findViewById(R.id.jobeditText5);
        gfemale = findViewById(R.id.female);
        gmale = findViewById(R.id.male);
        signup = findViewById(R.id.btnsign);
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
            DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, cdate, year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        });
        signup.setOnClickListener(v -> {
            String gender = "";
            String n = cname.getText().toString();
            String un = cusername.getText().toString();
            String p = cpassword.getText().toString();
            String b = cbirthdate.getText().toString();
            String j = cjob.getText().toString();
            String Email = "marawanfawzy15@gmail.com";
            if (gfemale.isChecked()) {
                gender = "Female";
            } else if (gmale.isChecked()) {
                gender = "Male";
            }
            if (cname.getText().toString().equals(""))
                Toast.makeText(RegisterActivity.this, "Please enter your Name", Toast.LENGTH_SHORT).show();
            else if (cusername.getText().toString().equals(""))
                Toast.makeText(getApplicationContext(), "Please enter your Username", Toast.LENGTH_SHORT).show();
            else if (cpassword.getText().toString().equals(""))
                Toast.makeText(getApplicationContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();
            else if (gender.equals(""))
                Toast.makeText(getApplicationContext(), "Please enter your Gender", Toast.LENGTH_SHORT).show();
            else if (cbirthdate.getText().toString().equals("Select your Birthdate"))
                Toast.makeText(getApplicationContext(), "Please enter your Birth Date", Toast.LENGTH_SHORT).show();
            else if (cjob.getText().toString().equals(""))
                Toast.makeText(getApplicationContext(), "Please enter your Job", Toast.LENGTH_SHORT).show();
            else {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String finalGender = gender;
                db.collection("Customers")
                        .whereEqualTo("username", un)
                        .limit(1)
                        .get().addOnSuccessListener(queryDocumentSnapshots -> {
                            if (queryDocumentSnapshots.size() != 0) {
                                Toast.makeText(RegisterActivity.this, "please enter a valid username ", Toast.LENGTH_SHORT).show();
                                cusername.setText("");
                            }
                            else {
                                String id = db.collection("Customers").document().getId().substring(0, 5);
                                Date date = new Date(b);
                                Customers newTemp = new Customers(id, n, un, p, date, j, Email , finalGender, false);
                                db.collection("Customers").document(id).set(newTemp);
                                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(i);
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
}
