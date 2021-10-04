package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class editnoteactivity extends AppCompatActivity {

    EditText edit_title,edit_content;
    FloatingActionButton save_note;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    String title;
    String content;
    String noteId;
    String book,date,time;
    String date_edit;
    String time_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnoteactivity);

        getWindow().setStatusBarColor(ContextCompat.getColor(editnoteactivity.this,R.color.purple_2001));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Date currentDate = new Date();
        SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm:ss a");
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, MMM dd yyyy");
        date_edit = dateFormat.format(currentDate);
        time_edit =  timeformat.format(currentDate);

         Intent data = getIntent();
         title = data.getStringExtra("title");
         content = data.getStringExtra("content");
         noteId = data.getStringExtra("noteId");
         book = data.getStringExtra("Bookmark");


        edit_title =  findViewById(R.id.titleedit);
        edit_content = findViewById(R.id.contentofnotedit);
        save_note = findViewById(R.id.saveditnote);

        edit_title.setText(title);
        edit_content.setText(content);

        Toolbar toolbar = findViewById(R.id.toolbarofnotedit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        save_note.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                documentReference = firebaseFirestore.collection("notes").document(user.getUid()).collection("myNotes").document(noteId);

                Map<String,Object> note = new HashMap<>();

                String title_get = edit_title.getText().toString();
                String title_content = edit_content.getText().toString();
                note.put("title",title_get);
                note.put("content",  title_content);
                note.put("bookmark",book);
                note.put("date",date_edit);
                note.put("time",time_edit);

                Map<String,Object> note2 =new HashMap<>();
                note2.put("title",title_get);
                note2.put("content",title_content);


                documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Successfully Updated",Toast.LENGTH_SHORT).show();
                        finish();
                       // startActivity(new Intent(getApplicationContext(),NoteFrame.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed to Update",Toast.LENGTH_SHORT).show();
                    }
                });

//                Toast.makeText(getApplicationContext(),"Failed to Update",Toast.LENGTH_SHORT).show();
                DocumentReference documentReference1 = firebaseFirestore.collection("notes").document(user.getUid()).collection("Bookmark").document(noteId);

                documentReference1.set(note2).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Bookmark Updated",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}