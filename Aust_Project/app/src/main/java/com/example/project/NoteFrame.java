package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NoteFrame extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton createNotesfab;
    FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_frame);
//        getSupportActionBar().hide();
        dioluge_pop("SuccesFully Log In");

        createNotesfab = findViewById(R.id.createnote);
        createNotesfab.setOnClickListener(this);

//        Button logOut = findViewById(R.id.logOut);
//        logOut.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        if(v.getId()==R.id.createnote)
        {
            startActivity(new Intent(NoteFrame.this,createNote.class));
            Toast.makeText(getApplicationContext(),"Nice",Toast.LENGTH_SHORT).show();
        }
//        if(v.getId()==R.id.logout)
//        {
//            logOut();
//        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.LO)
        {
          logOut();
        }
        return super.onOptionsItemSelected(item);
    }

    public void logOut()
    {

        firebaseAuth.signOut();
        finish();
    }
}