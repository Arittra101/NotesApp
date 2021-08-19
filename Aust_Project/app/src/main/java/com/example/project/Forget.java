package com.example.project;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Forget extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    Button recover_btn;
    EditText recover;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        getSupportActionBar().hide();  //action bar hidden

        recover = findViewById(R.id.confirm_email);
        recover_btn = findViewById(R.id.recover);
        recover_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recover_mail = recover.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(recover_mail).matches())
                {
                    recover.setError("Input Valid Email Address");
                    recover.requestFocus();
                }
               else if(recover_mail.isEmpty())
                {
                    recover.setError("Please Enter Email Address");
                    recover.requestFocus();
                }
                else{
                    firebaseAuth.sendPasswordResetEmail(recover_mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"Mail sent! You can Recover Now",Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                    });
                }
            }
        });



    }
}