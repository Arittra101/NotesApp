package com.example.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NoteFrame extends AppCompatActivity {

    FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_frame);
        getSupportActionBar().hide();
        dioluge_pop("SuccesFully Log In");
        Button logOut = findViewById(R.id.logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                firebaseAuth.signOut();
                finish();
                Toast.makeText(getApplicationContext(),"Log Out",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void dioluge_pop(String about)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(NoteFrame.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View v = new View(NoteFrame.this) ;
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
}