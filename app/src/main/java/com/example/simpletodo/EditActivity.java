package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText editTodo;
    Button btnSave;
    String previousEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editTodo = findViewById(R.id.editTodoEntry);
        btnSave = findViewById(R.id.btnSave);

        getSupportActionBar().setTitle("Edit Todo");
        previousEntry = getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT);
        editTodo.setText(previousEntry);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entry = editTodo.getText().toString().trim();
                Intent i = new Intent();
                if (TextUtils.isEmpty(entry) || entry.equals(previousEntry)){
                    setResult(RESULT_CANCELED,i);
                    finish();
                }
                i.putExtra(MainActivity.KEY_ITEM_TEXT, editTodo.getText().toString());
                i.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                setResult(RESULT_OK, i);
                finish();
            }
        });
        editTodo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // check for empty entry
                MyUtility.checkEntry(editTodo,btnSave);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}