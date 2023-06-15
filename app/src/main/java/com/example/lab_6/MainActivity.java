package com.example.lab_6;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.imageView);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        CatImage catImage = new CatImage(imageView, progressBar);
        catImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static class CatImage extends AsyncTask<Void, Integer, Bitmap> {

        private final WeakReference<ImageView> imageViewRef;
        private final WeakReference<ProgressBar> progressBarRef;

        public CatImage(ImageView imageView, ProgressBar progressBar) {
            imageViewRef = new WeakReference<>(imageView);
            progressBarRef = new WeakReference<>(progressBar);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            // Implement your background image fetching logic here
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // Update the progress bar with the current status
            ProgressBar progressBar = progressBarRef.get();
            if (progressBar != null) {
                progressBar.setProgress(values[0]);
            }

            // If a new cat picture has been selected, update the ImageView with the new picture
            if (values.length > 1) {
                ImageView imageView = imageViewRef.get();
                if (imageView != null) {
                    Bitmap bitmap = BitmapFactory.decodeResource(imageView.getResources(), values[1]);
                    imageView.setImageBitmap(bitmap);
                }
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            // Update the ImageView with the final downloaded bitmap
            ImageView imageView = imageViewRef.get();
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
