package com.example.myshoppingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button login;
    TextView register;
    TextView forgetPassword;
    ShoppingDatabase sdb;
    boolean saveCurrentLogin;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CheckBox remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sdb = new ShoppingDatabase(this);
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
            String userName = username.getText().toString();
            String Password = password.getText().toString();

            Cursor c = sdb.CheckUser(userName, Password);
            if (c == null) {
                Toast.makeText(MainActivity.this, "Login Error!", Toast.LENGTH_LONG).show();
                username.setText("");
                password.setText("");
            } else {
                if (remember.isChecked()) {
                    editor.putBoolean("savelogin", true);
                    editor.putString("username", userName);
                    editor.putString("password", Password);
                } else {
                    editor.putBoolean("savelogin", false);
                }
                editor.commit();
                if(c.getInt(c.getColumnIndex("flag")) == 1)
                {
                    Toast.makeText(MainActivity.this, "welcome admin", Toast.LENGTH_LONG).show();
                    Intent adminIntent = new Intent(MainActivity.this, adminPage.class);
                    startActivity(adminIntent);
                    return;
                }
                Toast.makeText(MainActivity.this, "Successfully Logged in!", Toast.LENGTH_LONG).show();
                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homeIntent);
            }
        });


//        forgetPassword.setOnClickListener(v -> {
//
//            String uname = username.getText().toString();
//            String c_upass;
//            Cursor cc = sdb.forgetpassword(uname);
//            if (!cc.isAfterLast()) {
//                c_upass = cc.getString(1);
//                //password.setText(c_upass);
//                sendEmail();
//            }
//            cc.close();
//
//        });
        forgetPassword.setOnClickListener(v -> {

            String uname = username.getText().toString();
            String c_upass;
            Cursor cc = sdb.forgetPassword(uname);
            if (!cc.isAfterLast()) {
                c_upass = cc.getString(1);
                //password.setText(c_upass);
                senEmail(c_upass);
            }
            cc.close();

        });
    }
    private void senEmail(String msg) {
        String mEmail = "marawanfawzy15@gmail.com";
        String mSubject = "you forgot your password";
        String mMessage = "your password is " + msg;


        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail, mSubject, mMessage);

        javaMailAPI.execute();
    }
}
