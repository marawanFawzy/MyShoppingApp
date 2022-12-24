package com.example.myshoppingapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myshoppingapp.firebase.Customers;
import com.example.myshoppingapp.helpers.Check;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    DatePickerDialog.OnDateSetListener cdate;
    EditText cname, cusername, cpassword, cbirthdate, cMail, cSSN;
    RadioButton gfemale, gmale;
    FloatingActionButton signup;
    Check errorChecker = new Check();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        cname = findViewById(R.id.nameeditText);
        cusername = findViewById(R.id.usernameeditText2);
        cpassword = findViewById(R.id.passwordeditText3);
        cbirthdate = findViewById(R.id.birthddateeditText4);
        cMail = findViewById(R.id.MailEditText);
        cSSN = findViewById(R.id.SSNText);
        gfemale = findViewById(R.id.female);
        gmale = findViewById(R.id.male);
        signup = findViewById(R.id.btnsign);
        cbirthdate.setShowSoftInputOnFocus(false);
        //disableSoftInputFromAppearing(cbirthdate);
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
            String Mail = cMail.getText().toString();
            String SSN = cSSN.getText().toString();
            if (gfemale.isChecked()) {
                gender = "Female";
            } else if (gmale.isChecked()) {
                gender = "Male";
            }
            String checkerResult = errorChecker.EditTextIsEmpty(cname, cusername, cpassword, cbirthdate, cMail, cSSN);
            if (!checkerResult.equals("") || errorChecker.StringCheckIsEmpty(gender))
                Toast.makeText(RegisterActivity.this, "Please fill " + checkerResult + " Data ", Toast.LENGTH_SHORT).show();
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
                            } else {
                                String id = db.collection("Customers").document().getId().substring(0, 5);
                                Date date = new Date(b);
                                Customers newTemp = new Customers(id, n, un, p, date, Mail, finalGender, SSN, false, true);
                                db.collection("Customers").document(id).set(newTemp);
                                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(i);
                            }
                        });

            }
        });
    }

    public static void disableSoftInputFromAppearing(EditText editText) {

    }
}
