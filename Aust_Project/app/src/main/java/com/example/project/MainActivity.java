package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView forget;
    private Button  sign_up,logIn;
    private EditText email,pass;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();  //get Intialize firebase Authentication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();     //Action bar hidden

        email =  findViewById(R.id.loginEmail);  //getting email Edit Text  access
        pass =  findViewById(R.id.loginPass); // getting pass editText access
        logIn = findViewById(R.id.logIN);    //logIN button access
        forget = findViewById(R.id.forgetPass);  //forget TextView Access
        sign_up =  findViewById(R.id.new_user); //New user Button Access

        logIn.setOnClickListener(this);               //logIn button Action Listner
        forget.setOnClickListener(this);            //forget button Action Listner
        sign_up.setOnClickListener(this);           // sign up button Action Listener


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.forgetPass)    //forget Password action method condition
        {
            Intent intent = new Intent(getApplicationContext(),Forget.class);
            startActivity(intent);
        }
        else if(v.getId()==R.id.new_user)       //signUp action method condition
        {
            Intent intent = new Intent(getApplicationContext(),SignUp.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.logIN)            //get Log in action method condition
        {

        }
    }
}