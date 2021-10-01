package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class showImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        ImageView imageView = findViewById(R.id.img1);
        Intent data = getIntent();
        String url = data.getStringExtra("imageUrl");
        Picasso.get().load(url).into(imageView);
    }
}