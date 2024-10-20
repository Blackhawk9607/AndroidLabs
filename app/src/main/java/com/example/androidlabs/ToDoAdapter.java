package com.example.androidlabs;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class TodoAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<TodoItem> todoItems;

    // Constructor to initialize adapter with context and list of todo items
    public TodoAdapter(Context context, ArrayList<TodoItem> todoItems) {
        this.context = context;
        this.todoItems = todoItems;
    }

    // This method returns the number of items in the ListView (todoItems size)
    @Override
    public int getCount() {
        return todoItems.size(); // Number of rows
    }

    // This method returns the item at a specific position in the ListView
    @Override
    public Object getItem(int position) {
        return todoItems.get(position); // Item at position
    }

    // This method returns the item ID for a specific position
    @Override
    public long getItemId(int position) {
        return position; // Return position as ID since we are not using a database
    }

    // This method returns the view for each row in the ListView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the layout for a todo item if it's not already created
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.todo_item, parent, false);
        }

        // Get the current todo item from the list
        TodoItem currentItem = todoItems.get(position);

        // Get reference to the TextView in the row
        TextView todoText = convertView.findViewById(R.id.todoItemText);
        todoText.setText(currentItem.getText()); // Set the text for the item

        // Set background and text color based on urgency
        if (currentItem.isUrgent()) {
            convertView.setBackgroundColor(Color.RED); // Urgent -> Red background
            todoText.setTextColor(Color.WHITE); // White text for urgent item
        } else {
            convertView.setBackgroundColor(Color.WHITE); // Non-urgent -> White background
            todoText.setTextColor(Color.BLACK); // Black text for non-urgent item
        }

        return convertView; // Return the completed view for the row
    }
}
