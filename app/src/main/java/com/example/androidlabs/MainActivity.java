package com.example.androidlabs;

import android.app.AlertDialog;
import android.os.Bundle;
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

        // Initialize the ListView and set the adapter
        ListView listView = findViewById(R.id.listView);
        editTextItem = findViewById(R.id.EditText); // Reference to the EditText
        Button addButton = findViewById(R.id.AddButton); // Reference to the Add button

        // Create the adapter for the ListView
        adapter = new TodoAdapter(this, todoItems);
        listView.setAdapter(adapter);

        // Set up the button click listener to add a new to-do item
        addButton.setOnClickListener(v -> {
            addTodoItem(); // Call the method to add a new to-do item
        });

        // Set up long click listener on the ListView for item deletion
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            // Build and show an AlertDialog
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Do you want to delete this?")
                    .setMessage("The selected row is: " + position)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Remove the item from the list
                        todoItems.remove(position);
                        // Notify the adapter about the data change
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", null) // Just dismiss the dialog if "No" is clicked
                    .show();

            // Return true to indicate the long-click was handled
            return true;
        });
    }

    // Method to add a new to-do item
    private void addTodoItem() {
        String itemText = editTextItem.getText().toString(); // Get text from EditText
        boolean isUrgent = ((SwitchCompat) findViewById(R.id.UrgentSwitch)).isChecked(); // Get urgency status from Switch

        // Check if the input is not empty
        if (!itemText.isEmpty()) {
            TodoItem newItem = new TodoItem(itemText, isUrgent); // Create a new TodoItem
            todoItems.add(newItem); // Add to the list
            adapter.notifyDataSetChanged(); // Notify adapter of the data change
            editTextItem.setText(""); // Clear the EditText
            ((SwitchCompat) findViewById(R.id.UrgentSwitch)).setChecked(false); // Reset the Switch to unchecked
        }
    }
}
