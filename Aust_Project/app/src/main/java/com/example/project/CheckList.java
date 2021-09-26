package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class CheckList extends AppCompatActivity {


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firebaseFirestore =  FirebaseFirestore.getInstance();
    DocumentReference documentReference;

    ArrayList<String>doclist = new ArrayList<>();

    RecyclerView recyclerView;
    FirestoreRecyclerAdapter<list_class,listHolder> recyclerAdapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        floatingActionButton =  findViewById(R.id.addtask);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        Query query = firebaseFirestore.collection("notes").document(user.getUid()).collection("Task");
        FirestoreRecyclerOptions<list_class> allTask = new  FirestoreRecyclerOptions.Builder<list_class>().setQuery(query,list_class.class).build();


        recyclerAdapter = new FirestoreRecyclerAdapter<list_class, listHolder>(allTask) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull listHolder listHolder, int i, @NonNull list_class list_class) {

                listHolder.taskview.setText(list_class.getTask());
                String docId = recyclerAdapter.getSnapshots().getSnapshot(i).getId();

                listHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        doclist.add(docId);
                        Toast.makeText(getApplicationContext(),"See u ok",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public listHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.task_layout,parent,false);
                return new listHolder(view);
            }
        } ;


        recyclerView = findViewById(R.id.tasksRecyclerView);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);




    }


    public class listHolder extends RecyclerView.ViewHolder{

        public TextView taskview;
        public CheckBox checkBox;

        public listHolder(@NonNull View itemView)
        {
            super(itemView);
            taskview = itemView.findViewById(R.id.tasview);
            checkBox = itemView.findViewById(R.id.taskCheckBox);
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

    public void delete()
    {

        for(int i=0;i<doclist.size();i++)
        {
            documentReference = firebaseFirestore.collection("notes").document(user.getUid()).collection("Task").document(doclist.get(i));
            documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(),"OK deleted",Toast.LENGTH_SHORT).show();
                }
            });
           //
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu2,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId()==R.id.taskAdd)
        {
            finish();
            startActivity(new Intent(getApplicationContext(),AddTask.class));
        }
        return super.onOptionsItemSelected(item);
    }
}