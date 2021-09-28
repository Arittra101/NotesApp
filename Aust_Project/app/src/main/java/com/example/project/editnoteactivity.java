package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class editnoteactivity extends AppCompatActivity {

    EditText edit_title,edit_content;
    FloatingActionButton save_note;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    String title;
    String content;
    String noteId;
    String book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnoteactivity);



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


                documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Successfully Updated",Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(getApplicationContext(),NoteFrame.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed to Update",Toast.LENGTH_SHORT).show();
                    }
                });

//                Toast.makeText(getApplicationContext(),"Failed to Update",Toast.LENGTH_SHORT).show();

            }
        });



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}