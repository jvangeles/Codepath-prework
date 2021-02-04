package com.example.simpletodo;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

public class MyUtility {
    public static void checkEntry(EditText field, Button btnBinded){
        String entry = field.getText().toString().trim();
        btnBinded.setEnabled(!TextUtils.isEmpty(entry));
    }
}
