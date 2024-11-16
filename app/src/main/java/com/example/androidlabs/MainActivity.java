package com.example.androidlabs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: MainActivity started");

        // Set up the Toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG, "onCreate: Toolbar initialized");

        // Set up the Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Log.d(TAG, "onCreate: Navigation Drawer set up");

        // Add toggle button for opening/closing the drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Log.d(TAG, "onCreate: Drawer toggle added");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the toolbar menu
        getMenuInflater().inflate(R.menu.main_menu, menu);
        Log.d(TAG, "onCreateOptionsMenu: Menu inflated");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle toolbar menu item clicks
        int id = item.getItemId();
        if (id == R.id.item1) {
            Toast.makeText(this, "You clicked on item 1", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onOptionsItemSelected: Item 1 clicked");
            return true;
        } else if (id == R.id.item2) {
            Toast.makeText(this, "You clicked on item 2", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onOptionsItemSelected: Item 2 clicked");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation drawer item clicks
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Log.d(TAG, "onNavigationItemSelected: Home clicked");
            Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_dad_joke) {
            Log.d(TAG, "onNavigationItemSelected: Dad Joke clicked");
            startActivity(new Intent(this, DadJokeActivity.class));
        } else if (id == R.id.nav_exit) {
            Log.d(TAG, "onNavigationItemSelected: Exit clicked");
            finishAffinity();
        }
        drawerLayout.closeDrawers(); // Close the drawer after selection
        Log.d(TAG, "onNavigationItemSelected: Drawer closed");
        return true;
    }
}
