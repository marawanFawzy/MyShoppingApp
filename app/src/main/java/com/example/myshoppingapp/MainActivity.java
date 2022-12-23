package com.example.myshoppingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myshoppingapp.firebase.Admin;
import com.example.myshoppingapp.firebase.Customers;
import com.example.myshoppingapp.helpers.AdminAccessProxy;
import com.example.myshoppingapp.helpers.Check;
import com.example.myshoppingapp.helpers.LoginProxy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    FloatingActionButton login;
    TextView register;
    TextView forgetPassword;
    boolean saveCurrentLogin;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CheckBox remember;
    Check errorChecker = new Check();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.username);
        password = findViewById(R.id.Password);
        login = findViewById(R.id.Loginbtn);
        register = findViewById(R.id.registertxt);
        forgetPassword = findViewById(R.id.forgetpswd);
        sharedPreferences = getSharedPreferences("loginSharedPref", MODE_PRIVATE);  //to Remember me
        remember = findViewById(R.id.checkBox);
        editor = sharedPreferences.edit();
        saveCurrentLogin = sharedPreferences.getBoolean("loginsaved", true);
        if (saveCurrentLogin) {
            username.setText(sharedPreferences.getString("username", null));
            password.setText(sharedPreferences.getString("password", null));
        } else {
            username.setText("");
            password.setText("");
        }


        register.setOnClickListener(v -> {
            Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(registerIntent);
        });

        login.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userName = username.getText().toString();
            String Password = password.getText().toString();
            db.collection("Customers")
                    .whereEqualTo("username", userName)
                    .limit(1)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.size() == 0) {
                            Toast.makeText(MainActivity.this, "please enter a valid username or password", Toast.LENGTH_SHORT).show();
                            username.setText("");
                            password.setText("");
                            return;
                        }
                        DocumentSnapshot d = queryDocumentSnapshots.getDocuments().get(0);
                        if (d.exists()) {
                            Customers temp = d.toObject(Customers.class);
                            AdminAccessProxy checkAdmin = new AdminAccessProxy();
                            LoginProxy checkLogin = new LoginProxy();
                            if (checkLogin.generateAccess(temp, Password)) {
                                if (remember.isChecked()) {
                                    editor.putBoolean("savelogin", true);
                                    editor.putString("username", userName);
                                    editor.putString("password", Password);
                                } else {
                                    editor.putBoolean("savelogin", false);
                                }
                                editor.commit();
                                if (checkAdmin.AdminRouter(temp)) {
                                    Admin admin = Admin.getAdmin();
                                    Toast.makeText(MainActivity.this, "welcome admin " + admin.getUsername(), Toast.LENGTH_SHORT).show();
                                    Intent adminIntent = new Intent(MainActivity.this, adminPage.class);
                                    startActivity(adminIntent);
                                } else {
                                    Toast.makeText(MainActivity.this, "Successfully Logged in!", Toast.LENGTH_SHORT).show();
                                    Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                                    homeIntent.putExtra("userId", temp.getId());
                                    startActivity(homeIntent);
                                }
                            } else {
                                Toast.makeText(this, "access denied please contact the admin", Toast.LENGTH_SHORT).show();
                                username.setText("");
                                password.setText("");
                            }
                        }
                    });
        });

        forgetPassword.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            if (!errorChecker.EditTextIsEmpty(username).equals("")) {
                Toast.makeText(this, "please type your username", Toast.LENGTH_SHORT).show();
                return;
            }
            db.collection("Customers")
                    .whereEqualTo("username", username.getText().toString())
                    .limit(1)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.size() == 0) {
                            Toast.makeText(MainActivity.this, "please enter a valid username", Toast.LENGTH_SHORT).show();
                            username.setText("");
                            return;
                        }
                        DocumentSnapshot d = queryDocumentSnapshots.getDocuments().get(0);
                        if (d.exists()) {
                            Customers temp = d.toObject(Customers.class);
                            if (temp == null) {
                                Toast.makeText(MainActivity.this, "please enter a valid username", Toast.LENGTH_SHORT).show();
                                username.setText("");
                                return;
                            }
                            senEmail(temp.getPassword(), temp.getEmail());
                            Toast.makeText(this, "email sent to you with password", Toast.LENGTH_SHORT).show();
                        }
                    });

        });
    }

    private void senEmail(String msg, String email) {
        String mEmail = email;
        String mSubject = "you forgot your password";
        String mMessage = "your password is " + msg;


        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail, mSubject, mMessage);

        javaMailAPI.execute();
    }
}
