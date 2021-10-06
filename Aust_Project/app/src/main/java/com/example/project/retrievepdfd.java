package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class retrievepdfd extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore =FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirestoreRecyclerAdapter<putPdf,pdfHolder> recyclerAdapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrievepdfd);
        getWindow().setStatusBarColor(ContextCompat.getColor(retrievepdfd.this,R.color.purple_2001));
        Query query = firebaseFirestore.collection("notes").document(user.getUid()).collection("PDF");
        FirestoreRecyclerOptions<putPdf> allinfo = new FirestoreRecyclerOptions.Builder<putPdf>().setQuery(query,putPdf.class).build();

        recyclerAdapter = new FirestoreRecyclerAdapter<putPdf, pdfHolder>(allinfo) {
            @Override
            protected void onBindViewHolder(@NonNull pdfHolder pdfHolder, int i, @NonNull putPdf putPdf) {

                String docId = recyclerAdapter.getSnapshots().getSnapshot(i).getId();
                pdfHolder.title.setText(putPdf.getName());
                pdfHolder.date.setText(putPdf.getTime());

                //download
                pdfHolder.download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        intent.setType("application.pdf");
                        intent.setData(Uri.parse(putPdf.getUrl()));
                        startActivity(intent);
                    }
                });

                //
                pdfHolder.pdf_pop.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        //delete
                        PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                DocumentReference documentReference = firebaseFirestore.collection("notes").document(user.getUid()).collection("PDF").document(docId);
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(),"Deleted PDF",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return false;

                            }
                        });
                        popupMenu.show();
                    }
                });

            }

            @NonNull
            @Override
            public pdfHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.pdflayout,parent,false);

                return new pdfHolder(view);
            }
        };
        recyclerView = findViewById(R.id.pdfrecycler);
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        recyclerView.setAdapter(recyclerAdapter);

    }

    public class pdfHolder extends RecyclerView.ViewHolder{

        TextView title,date;
        ImageView download,pdf_pop;
        pdfHolder(@NonNull View ItemView)
        {
            super(ItemView);
            title = ItemView.findViewById(R.id.rpdfname);
            date = ItemView.findViewById(R.id.rpdftime);
            download = ItemView.findViewById(R.id.pdfdown);
            pdf_pop = ItemView.findViewById(R.id.pdf_pop);
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