package com.example.androidlabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);


        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());


        loadCatImages();
    }

    private void loadCatImages() {
        executorService.execute(() -> {
            while (!isFinishing()) {
                try {
                    Bitmap bitmap = fetchCatImage();


                    for (int i = 0; i <= 100; i++) {
                        final int progress = i;

                        mainHandler.post(() -> progressBar.setProgress(progress));
                    }


                    mainHandler.post(() -> {
                        imageView.setImageBitmap(bitmap);
                        progressBar.setProgress(0);
                    });

                } catch (Exception e) {
                    Log.e("MainActivity", "Error in image loading task", e);
                }
            }
        });
    }

    private Bitmap fetchCatImage() throws Exception {
        String imageId = getString();
        String imageUrl = "https://cataas.com/cat/" + imageId;
        File imageFile = new File(getCacheDir(), imageId + ".jpg");


        if (imageFile.exists()) {
            return BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        } else {
            return downloadImage(imageUrl, imageFile);
        }
    }

    @NonNull
    private static String getString() throws IOException, JSONException {
        URL url = new URL("https://cataas.com/cat?json=true");
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

        JSONObject jsonObject = new JSONObject(jsonBuilder.toString());
        return jsonObject.getString("id");
    }

    private Bitmap downloadImage(String imageUrl, File imageFile) throws Exception {
        HttpURLConnection imageConnection = (HttpURLConnection) new URL(imageUrl).openConnection();
        imageConnection.connect();

        InputStream input = imageConnection.getInputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(input);
        FileOutputStream output = new FileOutputStream(imageFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        output.close();

        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
