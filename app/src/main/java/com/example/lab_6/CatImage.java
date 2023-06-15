package com.example.lab_6;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class CatImage extends AsyncTask<Void, Integer, Bitmap> {

    @SuppressLint("StaticFieldLeak")
    private final ImageView imageView;
    @SuppressLint("StaticFieldLeak")
    private final ProgressBar progressBar;
    private Bitmap currentCatPicture;

    public CatImage(ImageView imageView, ProgressBar progressBar) {
        this.imageView = imageView;
        this.progressBar = progressBar;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Random random = new Random();

        while (true) {
            try {
                URL url = new URL("https://cataas.com/cat?json=true");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    String response = convertStreamToString(inputStream);

                    assert response != null;
                    JSONObject jsonObject = new JSONObject(response);
                    String id = jsonObject.getString("id");
                    String urlStr = jsonObject.getString("url");

                    // Check if an image file with the "id" exists locally
                    String imageFilePath = getImageFilePath(id);
                    if (imageFileExists(imageFilePath)) {
                        return loadBitmapFromFile(imageFilePath);
                    } else {
                        Bitmap bitmap = downloadImage(urlStr);
                        assert bitmap != null;
                        saveBitmapToFile(bitmap, imageFilePath);
                        currentCatPicture = bitmap; // Store the current picture for updating ImageView
                        publishProgress(); // Trigger onProgressUpdate to update ImageView
                        return bitmap;
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            // Sleep for a while to give the user time to appreciate the current picture
            for (int i = 0; i < 100; i++) {
                try {
                    publishProgress(i);
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        // Update the progress bar with the current status of the timer
        progressBar.setProgress(values[0]);

        // If a new cat picture has been selected, update the ImageView with the new picture
        imageView.setImageBitmap(currentCatPicture);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        // Update the ImageView with the final downloaded bitmap
        imageView.setImageBitmap(bitmap);
    }

    private Bitmap downloadImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean imageFileExists(String imageFilePath) {
        File imageFile = new File(imageFilePath);
        return imageFile.exists();
    }

    private Bitmap loadBitmapFromFile(String imageFilePath) {
        return BitmapFactory.decodeFile(imageFilePath);
    }

    private void saveBitmapToFile(Bitmap bitmap, String imageFilePath) {
        File imageFile = new File(imageFilePath);
        try (FileOutputStream outputStream = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getImageFilePath(String imageId) {
        // Define the file path for the image file with the given id in the images directory
        String imagesDirectoryPath = "C:\\Users\\mazen\\Desktop\\Lab_6_Images";
        return imagesDirectoryPath + File.separator + imageId + ".png";
    }

    private String convertStreamToString(InputStream inputStream) {
        // Implement code to convert the input stream to a string
        // Return the converted string
        return null;
    }
}
