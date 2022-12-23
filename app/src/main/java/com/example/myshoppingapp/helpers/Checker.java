package com.example.myshoppingapp.helpers;

import android.widget.EditText;

public interface Checker {
    boolean StringCheckIsEmpty(String... args);

    String EditTextIsEmpty(EditText... args);
}
