package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity implements View.OnClickListener{

    CardView noteCard,checkCard,pdfCard,imageCard,remindCard,aboutCard,logOut;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //getSupportActionBar().hide();

        getWindow().setStatusBarColor(ContextCompat.getColor(Dashboard.this,R.color.purple_2001));
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

        logOut =findViewById(R.id.aboutView);
        logOut.setOnClickListener(this);


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
        if(v.getId()==R.id.imageView)
        {
            startActivity(new Intent(getApplicationContext(),RImage.class));
        }
        if(v.getId()==R.id.reminderView)
        {
            startActivity(new Intent(getApplicationContext(),Event.class));
        }
        if(v.getId()==R.id.aboutView)
        {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));

        }

    }

}