package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import static java.lang.Thread.sleep;

public class splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(2000);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    Intent i = new Intent(splashscreen.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        thread.start();
    }
}