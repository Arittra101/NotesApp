package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class RImage extends AppCompatActivity implements  View.OnClickListener{

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter<Upload,ImageHolder> recyclerAdapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FloatingActionButton addImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_image);
        addImage = findViewById(R.id.addimage);
        addImage.setOnClickListener(this);
        Query query = firebaseFirestore.collection("notes").document(user.getUid()).collection("Image");
        FirestoreRecyclerOptions<Upload> allinfo =  new FirestoreRecyclerOptions.Builder<Upload>().setQuery(query,Upload.class).build();

        recyclerAdapter = new FirestoreRecyclerAdapter<Upload, ImageHolder>(allinfo) {
            @Override
            protected void onBindViewHolder(@NonNull ImageHolder imageHolder, int i, @NonNull Upload upload) {

                imageHolder.imageTitle.setText(upload.getImage_title());
                imageHolder.DateView.setText(upload.getDate());
                imageHolder.timeView.setText(upload.getHour());
                Picasso.get().load(upload.getImageUrl()).into(imageHolder.rimageView);

                imageHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),upload.getImageUrl() ,Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @NonNull
            @Override
            public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.image_layout,parent,false);

                return new ImageHolder(view);
            }
        };


        recyclerView = findViewById(R.id.imagerecycler);
        recyclerView.setHasFixedSize(true);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        recyclerView.setAdapter(recyclerAdapter);


    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.addimage)
        {
            finish();
            startActivity(new Intent(getApplicationContext(),addpicture.class));
        }
    }


    public class  ImageHolder extends RecyclerView.ViewHolder
    {
                TextView imageTitle,DateView,timeView;
                ImageView rimageView,downloadView;
                public  ImageHolder(@NonNull View itemview)
                {
                    super(itemview);
                    imageTitle = itemview.findViewById(R.id.rimage_title);
                    DateView = itemview.findViewById(R.id.rdate);
                    timeView = itemview.findViewById(R.id.rtime);
                    rimageView = itemview.findViewById(R.id.img);
                    downloadView = itemview.findViewById(R.id.rdownload);
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