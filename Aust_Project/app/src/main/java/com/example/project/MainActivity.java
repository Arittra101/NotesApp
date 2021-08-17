package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView forget;
    private Button  sign_up,logIn;
    private EditText email,pass;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();  //get Intialize firebase Authentication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();          //Action bar hidden

        email =  findViewById(R.id.loginEmail);  //getting email Edit Text  access
        pass =  findViewById(R.id.loginPass); // getting pass editText access
        logIn = findViewById(R.id.logIN);    //logIN button access
        forget = findViewById(R.id.forgetPass);  //forget TextView Access
        sign_up =  findViewById(R.id.new_user); //New user Button Access

        logIn.setOnClickListener(this);               //logIn button Action Listner
        forget.setOnClickListener(this);            //forget button Action Listner
        sign_up.setOnClickListener(this);           // sign up button Action Listener

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            startActivity(new Intent(getApplicationContext(),NoteFrame.class));

        }

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
            String user_mail = email.getText().toString().trim();  //getText from the email edit Text
            String user_pass = pass.getText().toString().trim();      //getText from the pass edit Text
            firebaseAuth.signInWithEmailAndPassword(user_mail,user_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                           checkVarification(); //check the email verification
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Account Doesn't exist", Toast.LENGTH_SHORT).show();
                        }
                }
            });

        }
    }

   public void checkVarification()  //Verification check method
    {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();  //we have intialize firebaseUser by getCurrentUser();
       if(firebaseUser.isEmailVerified()==true)    //is valid then below statement will exicute
       {
           Toast.makeText(getApplicationContext(), "Successfully Log In", Toast.LENGTH_SHORT).show();
           startActivity(new Intent(getApplicationContext(),NoteFrame.class));
       }
       else
           Toast.makeText(getApplicationContext(), "Please Varified Your Account", Toast.LENGTH_SHORT).show();
    }
}