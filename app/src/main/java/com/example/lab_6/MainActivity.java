package com.example.lab_6;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.imageView);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        CatImage catImage = new CatImage(imageView, progressBar);
        catImage.execute();
    }
}
