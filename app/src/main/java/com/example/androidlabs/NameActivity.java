package com.example.androidlabs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        // Apply system bar insets padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.nameLayouts), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get the Intent from MainActivity and retrieve the passed name
        Intent intent = getIntent();
        String userName = intent.getStringExtra("name");

        // Find the TextView and set the welcome message
        TextView welcomeTextView = findViewById(R.id.NameTextView);
        welcomeTextView.setText(getString(R.string.welcome_message, userName));

        // Set up the "Don't call me that" button
        Button dontCallMeButton = findViewById(R.id.NameButton1);
        dontCallMeButton.setOnClickListener(v -> {
            // Set result to RESULT_CANCELED and finish the activity
            setResult(RESULT_CANCELED);
            finish();
        });

        // Set up the "Thank you" button
        Button thankYouButton = findViewById(R.id.NameButton2);
        thankYouButton.setOnClickListener(v -> {
            // Set result to RESULT_OK and finish the activity
            setResult(RESULT_OK);
            finish();
        });
    }
}
