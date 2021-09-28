package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity implements View.OnClickListener{

    CardView noteCard,checkCard,pdfCard,imageCard,remindCard,aboutCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        noteCard = findViewById(R.id.noteView);
        noteCard.setOnClickListener(this);
        checkCard = findViewById(R.id.checkView);
        checkCard.setOnClickListener(this);
        pdfCard = findViewById(R.id.pdfView);
        pdfCard.setOnClickListener(this);
        imageCard = findViewById(R.id.imageView);
        imageCard.setOnClickListener(this);
        remindCard = findViewById(R.id.reminderView);
        remindCard.setOnClickListener(this);
        aboutCard = findViewById(R.id.aboutView);
        aboutCard.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.checkView)
        {
            startActivity(new Intent(getApplicationContext(),CheckList.class));
        }
        if(v.getId()==R.id.noteView)
        {
            startActivity(new Intent(getApplicationContext(),NoteFrame.class));
        }
        if(v.getId()==R.id.pdfView)
        {
            startActivity(new Intent(getApplicationContext(),pdfUplpad.class));
        }
    }
}