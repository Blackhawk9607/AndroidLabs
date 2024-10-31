package com.example.androidlabs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final ArrayList<StarWarsCharacter> characterList = new ArrayList<>();
    private CharacterAdapter adapter;
    private ExecutorService executorService;  // For managing background tasks

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Activity created");

        ListView listView = findViewById(R.id.characterListView);
        adapter = new CharacterAdapter(this, characterList);
        listView.setAdapter(adapter);

        // Initialize the executor service
        executorService = Executors.newSingleThreadExecutor();

        // Fetch data
        fetchStarWarsData();

        // Handle item clicks
        listView.setOnItemClickListener((parent, view, position, id) -> {
            StarWarsCharacter character = characterList.get(position);
            Log.d(TAG, "onItemClick: Selected character - " + character.getName());
            Bundle bundle = new Bundle();
            bundle.putString("name", character.getName());
            bundle.putString("height", character.getHeight());
            bundle.putString("mass", character.getMass());

            View frameLayout = findViewById(R.id.detailsFrameLayout);

            if (frameLayout == null) { // Phone
                Intent intent = new Intent(MainActivity.this, EmptyActivity.class);
                intent.putExtras(bundle);
                Log.d(TAG, "onItemClick: Starting EmptyActivity with character data");
                startActivity(intent);
            } else { // Tablet
                DetailsFragment fragment = new DetailsFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.detailsFrameLayout, fragment);
                transaction.commit();
                Log.d(TAG, "onItemClick: Displaying character details in fragment");
            }
        });
    }

    private void fetchStarWarsData() {
        Log.d(TAG, "fetchStarWarsData: Starting data fetch task.");
        executorService.submit(() -> {
            ArrayList<StarWarsCharacter> fetchedCharacters = new ArrayList<>();
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("https://swapi.dev/api/people/?format=json");
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000); // Set a timeout
                connection.setReadTimeout(5000);
                connection.setRequestMethod("GET");

                // Check if connection is successful
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder json = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        json.append(line);
                    }

                    JSONObject jsonObject = new JSONObject(json.toString());
                    JSONArray results = jsonObject.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject characterObject = results.getJSONObject(i);
                        String name = characterObject.getString("name");
                        String height = characterObject.getString("height");
                        String mass = characterObject.getString("mass");

                        fetchedCharacters.add(new StarWarsCharacter(name, height, mass));
                    }

                    Log.d(TAG, "fetchStarWarsData: Data fetch successful. " + fetchedCharacters.size() + " characters fetched.");
                } else {
                    Log.e(TAG, "fetchStarWarsData: Server returned: " + connection.getResponseCode());
                }
            } catch (Exception e) {
                Log.e(TAG, "fetchStarWarsData: Error fetching data", e);
            } finally {
                // Close resources
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        Log.e(TAG, "fetchStarWarsData: Error closing reader", e);
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }

            // Update UI on the main thread
            runOnUiThread(() -> {
                characterList.addAll(fetchedCharacters);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "fetchStarWarsData: Adapter updated with fetched characters.");
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();  // Clean up the executor service
        Log.d(TAG, "onDestroy: Executor service shut down.");
    }
}
