package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.white101));
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
//            startActivity(new Intent(getApplicationContext(),NoteFrame.class));
            finish();
            startActivity(new Intent(getApplicationContext(),Dashboard.class));

        }


    }

    public void dioluge_pop(String about)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View v = new View(MainActivity.this) ;
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.notice_pop,viewGroup,false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button ok = dialogView.findViewById(R.id.ok);
        TextView msg = dialogView.findViewById(R.id.about);
        msg.setText(about);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.forgetPass)    //forget Password action method conditions
        {
            Intent intent = new Intent(getApplicationContext(),Forget.class);
            startActivity(intent);
        }
        else if(v.getId()==R.id.new_user)       //signUp action method condition upto
        {
            Intent intent = new Intent(getApplicationContext(),SignUp.class);

            startActivity(intent);
//            finish();
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
//           dioluge_pop("Succesfully Log In");
          // Toast.makeText(getApplicationContext(), "Successfully Log In", Toast.LENGTH_SHORT).show();
           finish();
           startActivity(new Intent(getApplicationContext(),Dashboard.class));
       }
       else
           Toast.makeText(getApplicationContext(), "Please Varified Your Account", Toast.LENGTH_SHORT).show();
    }
}