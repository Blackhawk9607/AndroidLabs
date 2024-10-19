package com.example.androidlabs;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private TextView textView;    // Declare a TextView
    private EditText editText;    // Declare an EditText

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_linear); // Set your layout file here

        // Initialize the views using their IDs defined in XML
        textView = findViewById(R.id.textView);    // Ensure this ID matches your XML
        editText = findViewById(R.id.editText);    // Ensure this ID matches your XML
        // Declare a Button
        Button button = findViewById(R.id.button);        // Ensure this ID matches your XML
        // Declare a CheckBox
        CheckBox checkBox = findViewById(R.id.checkBox);    // Ensure this ID matches your XML

        // Set an OnClickListener on the button
        button.setOnClickListener(v -> {
            // Get the text from the EditText
            String newText = editText.getText().toString();

            // Set the text to the TextView
            textView.setText(newText);

            // Show a Toast message
            Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_message), Toast.LENGTH_SHORT).show();
        });

        // Add a check change listener to the CheckBox
        checkBox.setOnCheckedChangeListener((cb, isChecked) -> {
            // Display a Snack bar showing the state of the checkbox
            String message = "The checkbox is now " + (isChecked ? "on" : "off");

            Snackbar snackbar = Snackbar.make(cb, message, Snackbar.LENGTH_LONG);

            // Add an "Undo" action to revert the checkbox state
            snackbar.setAction("Undo", v -> {
                // Revert the checkbox to its previous state
                cb.setChecked(!isChecked);
            });

            snackbar.show();
        });

        // Handling Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
