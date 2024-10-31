package com.example.androidlabs;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class EmptyActivity extends AppCompatActivity {

    private static final String TAG = "EmptyActivity"; // Tag for logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        Log.d(TAG, "onCreate called");

        if (savedInstanceState == null) {
            Log.d(TAG, "No savedInstanceState, adding DetailsFragment");

            // Creating the fragment and setting arguments
            DetailsFragment detailsFragment = new DetailsFragment();
            detailsFragment.setArguments(getIntent().getExtras());

            // Log the received arguments
            Bundle args = getIntent().getExtras();
            if (args != null) {
                Log.d(TAG, "Arguments received - Name: " + args.getString("name", "N/A") +
                        ", Height: " + args.getString("height", "N/A") +
                        ", Mass: " + args.getString("mass", "N/A"));
            } else {
                Log.w(TAG, "No arguments received");
            }

            // Adding the fragment to the container
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, detailsFragment)
                    .commit();
        } else {
            Log.d(TAG, "savedInstanceState is not null, fragment already exists");
        }
    }
}
