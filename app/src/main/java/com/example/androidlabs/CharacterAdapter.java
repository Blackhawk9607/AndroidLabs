package com.example.androidlabs;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CharacterAdapter extends BaseAdapter {

    private static final String TAG = "CharacterAdapter"; // Tag for logging
    private final Context context;
    private final ArrayList<StarWarsCharacter> characterList;

    public CharacterAdapter(Context context, ArrayList<StarWarsCharacter> characterList) {
        this.context = context;
        this.characterList = characterList;

        Log.d(TAG, "CharacterAdapter initialized with " + characterList.size() + " items.");
    }

    @Override
    public int getCount() {
        Log.d(TAG, "getCount called, returning " + characterList.size());
        return characterList.size();
    }

    @Override
    public Object getItem(int position) {
        Log.d(TAG, "getItem called for position: " + position);
        return characterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.d(TAG, "getItemId called for position: " + position);
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView called for position: " + position);

        if (convertView == null) {
            Log.d(TAG, "Inflating new view for position: " + position);
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        } else {
            Log.d(TAG, "Reusing existing view for position: " + position);
        }

        try {
            TextView textView = convertView.findViewById(android.R.id.text1);
            String characterName = characterList.get(position).getName();
            textView.setText(characterName);
            Log.d(TAG, "Set text for position " + position + ": " + characterName);
        } catch (Exception e) {
            Log.e(TAG, "Error setting text for position " + position, e);
        }

        return convertView;
    }
}
