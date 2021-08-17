package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private EditText email,pass;
    private Button signUp;
    private TextView logIn;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(); //get firebase intialize
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();  //  //Action bar hidden


        email = findViewById(R.id.userMail);  //email edit text access
        pass = findViewById(R.id.userPass);  //pass edit text access

        signUp = findViewById(R.id.sign_up);   //signup button edit text

        signUp.setOnClickListener(this);        //click method call

    }

    @Override
    public void onClick(View v) {

        getSign_Up();  //signUp method call
    }

    public void getSign_Up()   //signUp method
    {
        String input_mail = email.getText().toString().trim();
        String input_pass = pass.getText().toString().trim();


        if(input_mail.isEmpty())
        {
            email.setError("Please Enter Your Email");
            email.requestFocus();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(input_mail).matches())
        {
            email.setError("Please Enter a Valid Email Address");
            email.requestFocus();
        }
        else if(input_pass.length()<8)
        {
            pass.setError("Password Must be greater than 7 char");
            pass.requestFocus();
        }

        else{

            firebaseAuth.createUserWithEmailAndPassword(input_mail,input_pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(),"Successfully Registered",Toast.LENGTH_SHORT).show();
                        sendVerification();  //verification sent
                    }
                    else {

                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getApplicationContext(),"Already Registered",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

        }

    }

    void sendVerification() //verification sent method
    {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(getApplicationContext(),"Varification Email is Sent",Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Failed To Sent Varification",Toast.LENGTH_SHORT).show();
        }

    }

}