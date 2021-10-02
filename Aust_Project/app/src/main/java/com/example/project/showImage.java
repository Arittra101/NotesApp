package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class showImage extends AppCompatActivity {

    TextView title_view,date_view,des_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        ImageView imageView = findViewById(R.id.img1);
        Intent data = getIntent();
        String date = data.getStringExtra("date");
        String hour = data.getStringExtra("hour");
        String total = date + " " +hour;
        String des = data.getStringExtra("discription");
        String title = data.getStringExtra("imagetitle");
        String url = data.getStringExtra("imageUrl");


        Picasso.get().load(url).into(imageView);
        title_view = findViewById(R.id.title_detail);
        date_view = findViewById(R.id.time_detail);
        des_view = findViewById(R.id.des_detail);

        title_view.setText(title);
        date_view.setText(total);
        des_view.setText(des);
    }
}