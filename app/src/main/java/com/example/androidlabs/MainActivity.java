package com.example.androidlabs;

import android.content.Intent;
import android.os.AsyncTask;
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
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final ArrayList<StarWarsCharacter> characterList = new ArrayList<>();
    private CharacterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.characterListView);
        adapter = new CharacterAdapter(this, characterList);
        listView.setAdapter(adapter);

        fetchStarWarsData();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            StarWarsCharacter character = characterList.get(position);
            Bundle bundle = new Bundle();
            bundle.putString("name", character.getName());
            bundle.putString("height", character.getHeight());
            bundle.putString("mass", character.getMass());

            View frameLayout = findViewById(R.id.detailsFrameLayout);

            if (frameLayout == null) { // Phone
                Intent intent = new Intent(MainActivity.this, EmptyActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            } else { // Tablet
                DetailsFragment fragment = new DetailsFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.detailsFrameLayout, fragment);
                transaction.commit();
            }
        });
    }

    private void fetchStarWarsData() {
        new FetchStarWarsData(this).execute("https://swapi.dev/api/people/?format=json");
    }

    private static class FetchStarWarsData extends AsyncTask<String, Void, ArrayList<StarWarsCharacter>> {
        private final WeakReference<MainActivity> activityReference;

        FetchStarWarsData(MainActivity activity) {
            activityReference = new WeakReference<>(activity);
        }

        @Override
        protected ArrayList<StarWarsCharacter> doInBackground(String... urls) {
            ArrayList<StarWarsCharacter> fetchedCharacters = new ArrayList<>();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

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

            } catch (Exception e) {
                Log.e(TAG, "Error fetching data", e);
            }
            return fetchedCharacters;
        }

        @Override
        protected void onPostExecute(ArrayList<StarWarsCharacter> fetchedCharacters) {
            MainActivity activity = activityReference.get();
            if (activity != null && !activity.isFinishing()) {
                activity.characterList.addAll(fetchedCharacters);
                activity.adapter.notifyDataSetChanged();
            }
        }
    }
}
