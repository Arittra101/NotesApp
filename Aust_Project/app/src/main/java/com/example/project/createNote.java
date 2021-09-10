package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class createNote extends AppCompatActivity implements View.OnClickListener {

    EditText title,written_note;
    FloatingActionButton save_note;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
//        getSupportActionBar().hide();

        written_note= findViewById(R.id.written_note);
        title = findViewById(R.id.title);
        save_note = findViewById(R.id.note_save);

        Toolbar toolbar = findViewById(R.id.toolbarofcreateNote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
        firebaseUser = firebaseAuth.getCurrentUser();           //user inialize we can get id from it
        firebaseFirestore = firebaseFirestore.getInstance();   //firestore root
//
//
        save_note.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.note_save){

            String s_title = title.getText().toString();
            String s_written_note =  written_note.getText().toString();
            if(s_title.isEmpty()||s_written_note.isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Please",Toast.LENGTH_SHORT).show();
            }
            else
            {
                DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();
//                DocumentReference documentReference = firebaseFirestore.collection("Notes").document();
                Map<String,Object> m = new HashMap<>();
                m.put("title",s_title);
                m.put("content",s_written_note);
                documentReference.set(m).addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(createNote.this,NoteFrame.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                    }
                });


                DocumentReference documentReference1 = firebaseFirestore.collection("Notes").document();
            }



        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}