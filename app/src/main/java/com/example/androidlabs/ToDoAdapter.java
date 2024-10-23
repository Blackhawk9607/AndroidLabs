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

    
    public TodoAdapter(Context context, ArrayList<TodoItem> todoItems) {
        this.context = context;
        this.todoItems = todoItems;
    }

    
    @Override
    public int getCount() {
        return todoItems.size();

    
    @Override
    public Object getItem(int position) {
        return todoItems.get(position); 
    }

   
    @Override
    public long getItemId(int position) {
        return position;
    }

    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.todo_item, parent, false);
        }

        
        TodoItem currentItem = todoItems.get(position);

       
        TextView todoText = convertView.findViewById(R.id.todoItemText);
        todoText.setText(currentItem.getText()); 

       
        if (currentItem.isUrgent()) {
            convertView.setBackgroundColor(Color.RED); 
            todoText.setTextColor(Color.WHITE); 
        } else {
            convertView.setBackgroundColor(Color.WHITE); 
            todoText.setTextColor(Color.BLACK); 
        }

        return convertView; 
    }
}
