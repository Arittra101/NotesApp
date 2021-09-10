package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class  NoteFrame extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton createNotesfab;
    FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();

    RecyclerView mrecylerview;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    FirestoreRecyclerAdapter<firebasemodel,Noteviewholder> noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_frame);

//        dioluge_pop("SuccesFully Log In");

        createNotesfab = findViewById(R.id.createnote);


        firebaseUser =FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("title", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<firebasemodel> allusernotes = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query,firebasemodel.class).build();
        //user note collect


        noteAdapter = new FirestoreRecyclerAdapter<firebasemodel, Noteviewholder>(allusernotes) {
            @Override
            protected void onBindViewHolder(@NonNull Noteviewholder noteviewholder, int i, @NonNull firebasemodel firebasemodel) {

                noteviewholder.notetitle.setText(firebasemodel.getTitle());
                noteviewholder.notecontent.setText(firebasemodel.getContent());
            }

            @NonNull
            @Override
            public Noteviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
              View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
              return new Noteviewholder(view);
            }
        };
//
//
        mrecylerview = findViewById(R.id.recycler);
        mrecylerview.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mrecylerview.setLayoutManager(staggeredGridLayoutManager);

        mrecylerview.setAdapter(noteAdapter);
        createNotesfab.setOnClickListener(this);     //note add button
    }

    public class Noteviewholder extends RecyclerView.ViewHolder{

        private TextView notetitle;
        private TextView notecontent;
        LinearLayout mnote;
        public Noteviewholder(@NonNull View itemView)
        {
            super(itemView);
            notetitle = itemView.findViewById(R.id.notetitle);
            notecontent =  itemView.findViewById(R.id.notecontent);
            mnote = itemView.findViewById(R.id.note);

        }
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
//        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();

    }
    @Override
    protected void onStop() {
        super.onStop();
        if(noteAdapter!=null)
        {
            noteAdapter.startListening();
        }
    }

}