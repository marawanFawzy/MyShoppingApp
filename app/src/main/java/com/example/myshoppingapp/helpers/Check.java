package com.example.myshoppingapp.helpers;

import android.widget.EditText;

public class Check implements Checker {

    @Override
   public boolean StringCheckIsEmpty(String...args)
    {
        for (String e : args)
        if ( e == null || e.equals("")) {
            return true;
        }
        return false;
    }
    @Override
  public String EditTextIsEmpty(EditText... args ) {
        for (EditText e : args)
            if (e.getText().toString().equals("")) {
                return (String) e.getHint();
            }
        return "";
    }

}
