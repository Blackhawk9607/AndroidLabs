package com.example.androidlabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private ProgressBar progressBar;
    private ExecutorService executorService;
    private Handler mainHandler;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Initializing views and services");
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        // Start loading cat images
        Log.d(TAG, "onCreate: Starting image loading task");
        loadCatImages();
    }

    private void loadCatImages() {
        executorService.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Log.d(TAG, "loadCatImages: Fetching cat image ID");
                    String imageId = fetchCatImageId();
                    File imageFile = new File(getCacheDir(), imageId + ".jpg");
                    Bitmap bitmap;

                    // Check if image already exists in cache
                    if (imageFile.exists()) {
                        Log.d(TAG, "loadCatImages: Image found in cache");
                        bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    } else {
                        String imageUrl = "https://cataas.com/cat/" + imageId;
                        Log.d(TAG, "loadCatImages: Image not in cache, downloading from URL: " + imageUrl);
                        bitmap = downloadImage(imageUrl, imageFile);
                    }

                    // Slow and smooth progress update
                    for (int i = 0; i <= 100; i += 5) { // Increase step for smoother animation
                        final int progress = i;
                        mainHandler.post(() -> {
                            Log.d(TAG, "loadCatImages: Updating progress to " + progress);
                            progressBar.setProgress(progress);
                        });
                        Thread.sleep(150); // Slower progress update, 150ms delay between steps
                    }

                    // Set image on the ImageView
                    mainHandler.post(() -> {
                        Log.d(TAG, "loadCatImages: Setting image on ImageView");
                        imageView.setImageBitmap(bitmap);
                    });

                    // Wait for a short period before loading the next image
                    Thread.sleep(2000); // Pause for 2 seconds before loading the next image

                } catch (Exception e) {
                    Log.e(TAG, "Error in image loading task", e);
                    break; // Exit the loop if there's an error
                }
            }
        });
    }


    private String fetchCatImageId() throws IOException, JSONException {
        URL url = new URL("https://cataas.com/cat?json=true");
        Log.d(TAG, "fetchCatImageId: Connecting to " + url);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        InputStream inputStream = connection.getInputStream();
        StringBuilder jsonBuilder = new StringBuilder();
        int data = inputStream.read();
        while (data != -1) {
            jsonBuilder.append((char) data);
            data = inputStream.read();
        }
        inputStream.close();

        String jsonResponse = jsonBuilder.toString();
        Log.d(TAG, "fetchCatImageId: JSON response - " + jsonResponse);

        JSONObject jsonObject = new JSONObject(jsonResponse);

        // Use "_id" instead of "id" as per the JSON structure in the logs
        String imageId = jsonObject.optString("_id", null);
        if (!imageId.isEmpty()) {
            Log.d(TAG, "fetchCatImageId: Fetched image ID - " + imageId);
            return imageId;
        } else {
            Log.e(TAG, "fetchCatImageId: No valid ID found in the JSON response");
            throw new JSONException("No valid ID found in JSON response");
        }
    }

    private Bitmap downloadImage(String imageUrl, File imageFile) throws Exception {
        Log.d(TAG, "downloadImage: Starting download from " + imageUrl);

        HttpURLConnection imageConnection = (HttpURLConnection) new URL(imageUrl).openConnection();
        imageConnection.connect();

        InputStream input = imageConnection.getInputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(input);

        Log.d(TAG, "downloadImage: Download completed, saving to cache");

        // Save bitmap to file
        FileOutputStream output = new FileOutputStream(imageFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        output.close();

        Log.d(TAG, "downloadImage: Image saved to cache");
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Shutting down ExecutorService");
        executorService.shutdownNow(); // Shutdown ExecutorService on activity destroy
    }
}
