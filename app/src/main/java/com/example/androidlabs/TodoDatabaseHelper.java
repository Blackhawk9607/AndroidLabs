package com.example.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "todoList.db";
    private static final int DATABASE_VERSION = 1;

  
    public static final String TABLE_TODO = "todo";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TODO_TEXT = "todo_text";
    public static final String COLUMN_IS_URGENT = "is_urgent";

    
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_TODO + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TODO_TEXT + " TEXT, " +
                    COLUMN_IS_URGENT + " INTEGER);";

    
    public TodoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(db);
    }

 
    public void insertTodoItem(TodoItem todoItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TODO_TEXT, todoItem.getText());
        values.put(COLUMN_IS_URGENT, todoItem.isUrgent() ? 1 : 0);
        db.insert(TABLE_TODO, null, values);
        db.close();
    }

 
    public void deleteTodoItem(String todoText) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, COLUMN_TODO_TEXT + " = ?", new String[]{todoText});
        db.close();
    }

    
    public Cursor getAllTodos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_TODO, null, null, null, null, null, null);
    }
}
