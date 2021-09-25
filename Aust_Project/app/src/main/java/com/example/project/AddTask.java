package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddTask extends AppCompatActivity implements View.OnClickListener {


    FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    EditText editTask;
    FloatingActionButton savetaskfabbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        editTask = findViewById(R.id.task_note);
        savetaskfabbutton = findViewById(R.id.task_save);
        savetaskfabbutton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.task_save)
        {
            documentReference =firebaseFirestore.collection("notes").document(User.getUid()).collection("Task").document();
            Map<String,Object> m = new HashMap<>();
            String task = editTask.getText().toString();
            m.put("Task",task);

            documentReference.set(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(),"Ok added task",Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}