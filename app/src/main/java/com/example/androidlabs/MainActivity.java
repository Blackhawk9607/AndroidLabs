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

    private ArrayList<TodoItem> todoItems; // List to hold todo items
    private EditText editTextItem; // EditText for user input
    private TodoAdapter adapter; // Adapter for the ListView
    private TodoDatabaseHelper dbHelper; // Database helper for accessing SQLite

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up window insets for proper padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the todo items list
        todoItems = new ArrayList<>();

        // Initialize the database helper
        dbHelper = new TodoDatabaseHelper(this);

        // Load the saved todos from the database
        loadTodosFromDatabase();

        // Initialize the ListView and set the adapter
        ListView listView = findViewById(R.id.listView);
        editTextItem = findViewById(R.id.EditText);
        Button addButton = findViewById(R.id.AddButton);

        // Create the adapter for the ListView
        adapter = new TodoAdapter(this, todoItems);
        listView.setAdapter(adapter);

        // Set up the button click listener to add a new to-do item
        addButton.setOnClickListener(v -> addTodoItem());

        // Set up long click listener on the ListView for item deletion
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Do you want to delete this?")
                    .setMessage("The selected row is: " + position)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        TodoItem todoItem = todoItems.get(position);
                        dbHelper.deleteTodoItem(todoItem.getText()); // Remove from the database
                        todoItems.remove(position); // Remove from the list
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });

        // Debugging: Print database information using printCursor()
        printCursor(dbHelper.getAllTodos());
    }

    // Method to load todos from the database
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
        cursor.close(); // Always close the cursor after using it
    }

    // Method to add a new to-do item
    private void addTodoItem() {
        String itemText = editTextItem.getText().toString();
        boolean isUrgent = ((SwitchCompat) findViewById(R.id.UrgentSwitch)).isChecked();

        if (!itemText.isEmpty()) {
            TodoItem newItem = new TodoItem(itemText, isUrgent);
            todoItems.add(newItem);
            adapter.notifyDataSetChanged();
            editTextItem.setText("");
            ((SwitchCompat) findViewById(R.id.UrgentSwitch)).setChecked(false);

            // Add the new item to the database
            dbHelper.insertTodoItem(newItem);
        }
    }

    // Method to print cursor details for debugging
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
