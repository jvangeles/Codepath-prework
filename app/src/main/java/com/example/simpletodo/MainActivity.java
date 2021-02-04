package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 10;


    List<String> todos;
    Button btnAdd;
    EditText todoEntry;
    RecyclerView rvTodos;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        todoEntry = findViewById(R.id.todoEntry);
        rvTodos = findViewById(R.id.rvTodos);

        loadTodos();
        /*todos = new ArrayList<>();
        for(int i=0;i<3;i++){
            todos.add("Todo Item #"+(i+1));
        }*/

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClick(int position){
                todos.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),"Item removed!",Toast.LENGTH_SHORT).show();
                saveTodos();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra(KEY_ITEM_TEXT, todos.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                // show activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };

        itemsAdapter = new ItemsAdapter(todos, onLongClickListener, onClickListener);
        rvTodos.setAdapter(itemsAdapter);
        rvTodos.setLayoutManager(new LinearLayoutManager(this));

        // Ensure button status is disabled by default
        MyUtility.checkEntry(todoEntry,btnAdd);

        // event listener for validating text entry
        todoEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // check for empty entry
                MyUtility.checkEntry(todoEntry,btnAdd);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String todoItem = todoEntry.getText().toString();
                todos.add(todoItem);
                itemsAdapter.notifyItemInserted(todos.size()-1);
                todoEntry.setText("");
                // show success message
                Toast.makeText(getApplicationContext(),"Todo Added!", Toast.LENGTH_SHORT).show();
                saveTodos();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_TEXT_CODE){
            switch(resultCode){
                case RESULT_OK:
                    String modifiedTodoItem = data.getStringExtra(KEY_ITEM_TEXT);
                    int position = data.getExtras().getInt(KEY_ITEM_POSITION);

                    todos.set(position, modifiedTodoItem);
                    itemsAdapter.notifyItemChanged(position);
                    saveTodos();
                    Toast.makeText(getApplicationContext(),"Todo updated successfully!", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(getApplicationContext(),"No changes made", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    private void loadTodos() {
        try {
            todos = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity","Error reading todo items", e);
            todos = new ArrayList<>();
        }
    }

    private void saveTodos() {
        try {
            FileUtils.writeLines(getDataFile(), todos);
        } catch (IOException e) {
            Log.e("MainAactivity","Error writing todo items", e);
        }
    }
}

