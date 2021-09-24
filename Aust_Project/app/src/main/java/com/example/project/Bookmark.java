package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Bookmark extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();
    DocumentReference documentReference ;
    RecyclerView recyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FirestoreRecyclerAdapter<MyBookmark,NoteAccess> recyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        //Toast.makeText(getApplicationContext(),"OK Done",Toast.LENGTH_SHORT).show();


//        Query query = firebaseFirestore.collection("notes").document(user.getUid()).collection("myNotes").orderBy("title", Query.Direction.ASCENDING);
        Query query = firebaseFirestore.collection("notes").document(user.getUid()).collection("Bookmark");
        FirestoreRecyclerOptions<MyBookmark> alluserNotes = new FirestoreRecyclerOptions.Builder<MyBookmark>().setQuery(query,MyBookmark.class).build();

        recyclerAdapter =  new FirestoreRecyclerAdapter<MyBookmark, NoteAccess>(alluserNotes) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull NoteAccess noteAccess, int i, @NonNull MyBookmark myBookmark) {

                noteAccess.textViewtitle.setText(myBookmark.getTitle());
                noteAccess.textViewcontent.setText(myBookmark.getContent());
                String docId = recyclerAdapter.getSnapshots().getSnapshot(i).getId();
                noteAccess.popupbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),docId+"ok",Toast.LENGTH_SHORT).show();
                    }
                });
//                Toast.makeText(getApplicationContext(),"OK pl",Toast.LENGTH_SHORT).show();


            }

            @NonNull
            @Override
            public NoteAccess onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.book_marklayout,parent,false);

                return new NoteAccess(view);
            }
        };


        recyclerView = findViewById(R.id.recycler_book);
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);




    }

    public class  NoteAccess extends RecyclerView.ViewHolder{

        public TextView textViewcontent;
        public TextView textViewtitle;
        private ImageView popupbutton;
        private ImageView bookmarkimageview;
        LinearLayout mnote;
        public NoteAccess(@NonNull View itemView)
        {
            super(itemView);
            textViewcontent = itemView.findViewById(R.id.notecontent1);
            textViewtitle = itemView.findViewById(R.id.notetitle1);
            popupbutton = itemView.findViewById(R.id.menupopbutton1);
            bookmarkimageview = itemView.findViewById(R.id.book_mark_icon1);
            mnote = itemView.findViewById(R.id.note1);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerAdapter.startListening();

    }
    @Override
    protected void onStop() {
        super.onStop();
        if(recyclerAdapter!=null)
        {
            recyclerAdapter.stopListening();
        }
    }
}