package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText nameEditText;
    private SharedPreferences sharedPreferences;

    // Define the ActivityResultLauncher for starting NameActivity and receiving results
    private final ActivityResultLauncher<Intent> nameActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // User is happy, finish the app
                    finish(); // Close the app
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Apply system bar insets padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find views
        nameEditText = findViewById(R.id.MainEditText);
        Button nextButton = findViewById(R.id.MainButton);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);

        // Load the saved name from SharedPreferences
        String savedName = sharedPreferences.getString("name", "");
        if (!savedName.isEmpty()) {
            nameEditText.setText(savedName);
        }

        // Set OnClickListener for the Next button
        nextButton.setOnClickListener(v -> {
            // Get the name from EditText
            String name = nameEditText.getText().toString();

            // Create an Intent to launch NameActivity
            Intent intent = new Intent(MainActivity.this, NameActivity.class);
            intent.putExtra("name", name);

            // Launch the NameActivity using the ActivityResultLauncher
            nameActivityResultLauncher.launch(intent);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Save the current value of the EditText to SharedPreferences
        String nameToSave = nameEditText.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", nameToSave);
        editor.apply();
    }
}
