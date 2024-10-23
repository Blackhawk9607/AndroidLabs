package com.example.androidlabs;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<TodoItem> todoItems; 
    private EditText editTextItem; 
    private TodoAdapter adapter; 
    private TodoDatabaseHelper dbHelper; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        
        todoItems = new ArrayList<>();

        
        dbHelper = new TodoDatabaseHelper(this);

        
        loadTodosFromDatabase();

        
        ListView listView = findViewById(R.id.listView);
        editTextItem = findViewById(R.id.EditText);
        Button addButton = findViewById(R.id.AddButton);

       
        adapter = new TodoAdapter(this, todoItems);
        listView.setAdapter(adapter);

       
        addButton.setOnClickListener(v -> addTodoItem());

        
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Do you want to delete this?")
                    .setMessage("The selected row is: " + position)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        TodoItem todoItem = todoItems.get(position);
                        dbHelper.deleteTodoItem(todoItem.getText()); 
                        todoItems.remove(position); 
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });

       
        printCursor(dbHelper.getAllTodos());
    }

   
    private void loadTodosFromDatabase() {
        Cursor cursor = dbHelper.getAllTodos();
        if (cursor.moveToFirst()) {
            do {
                int textIndex = cursor.getColumnIndex(TodoDatabaseHelper.COLUMN_TODO_TEXT);
                int urgentIndex = cursor.getColumnIndex(TodoDatabaseHelper.COLUMN_IS_URGENT);

                if (textIndex != -1 && urgentIndex != -1) {
                    String todoText = cursor.getString(textIndex);
                    boolean isUrgent = cursor.getInt(urgentIndex) == 1;
                    todoItems.add(new TodoItem(todoText, isUrgent));
                }
            } while (cursor.moveToNext());
        }
        cursor.close(); 
    }

   
    private void addTodoItem() {
        String itemText = editTextItem.getText().toString();
        boolean isUrgent = ((SwitchCompat) findViewById(R.id.UrgentSwitch)).isChecked();

        if (!itemText.isEmpty()) {
            TodoItem newItem = new TodoItem(itemText, isUrgent);
            todoItems.add(newItem);
            adapter.notifyDataSetChanged();
            editTextItem.setText("");
            ((SwitchCompat) findViewById(R.id.UrgentSwitch)).setChecked(false);

            
            dbHelper.insertTodoItem(newItem);
        }
    }

    
    private void printCursor(Cursor cursor) {
        if (cursor != null) {
            Log.d("DB", "Database Version: " + dbHelper.getReadableDatabase().getVersion());
            Log.d("DB", "Number of Columns: " + cursor.getColumnCount());
            Log.d("DB", "Column Names: " + java.util.Arrays.toString(cursor.getColumnNames()));
            Log.d("DB", "Number of Rows: " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    Log.d("DB", "Row: " + cursor.getString(cursor.getColumnIndexOrThrow(TodoDatabaseHelper.COLUMN_TODO_TEXT)));
                } while (cursor.moveToNext());
            }
        }
    }
}
