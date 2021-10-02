package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
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

                String docId = recyclerAdapter.getSnapshots().getSnapshot(i).getId();
                imageHolder.imageTitle.setText(upload.getImage_title());
                imageHolder.DateView.setText(upload.getDate());
               // imageHolder.timeView.setText(upload.getHour());
                Picasso.get().load(upload.getImageUrl()).into(imageHolder.rimageView);



                imageHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),showImage.class);
                        intent.putExtra("date",upload.getDate());
                        intent.putExtra("hour",upload.getHour());
                        intent.putExtra("discription",upload.getDescription());
                        intent.putExtra("imagetitle",upload.getImage_title());
                        intent.putExtra("imageUrl",upload.getImageUrl());
                        startActivity(intent);
//                        Pair[] pairs = new Pair[1];
//                        pairs[0]= new Pair<View,String>(imageHolder.rimageView,"mimg");
//                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) imageHolder.itemView.getContext(),pairs);
//
//                        //startActivity(intent);
//                       startActivity(intent,optionsCompat.toBundle());
//                        Toast.makeText(getApplicationContext(),upload.getImageUrl() ,Toast.LENGTH_SHORT).show();
                    }
                });

                imageHolder.pop_up.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(getApplicationContext(),"Also ",Toast.LENGTH_SHORT).show();
//                        pop_up_method(imageHolder.itemView)
                        PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {


                                DocumentReference documentReference = firebaseFirestore.collection("notes").document(user.getUid()).collection("Image").document(docId);
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(),"Also added Database",Toast.LENGTH_SHORT).show();
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
            public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.layout_image,parent,false);

                return new ImageHolder(view);
            }
        };


        recyclerView = findViewById(R.id.imagerecycler);
        recyclerView.setHasFixedSize(true);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        recyclerView.setAdapter(recyclerAdapter);


    }


    public void pop_up_method()
    {

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
                ImageView rimageView;
                ImageView pop_up;
                //ImageView edownloadView;
                public  ImageHolder(@NonNull View itemview)
                {
                    super(itemview);
                    imageTitle = itemview.findViewById(R.id.Image1_title);
                    DateView = itemview.findViewById(R.id.Image1_date);
                    rimageView = itemview.findViewById(R.id.Image1_view);
                    pop_up = itemview.findViewById(R.id.ImageView1_dot);
                   // downloadView = itemview.findViewById(R.id.rdownload);
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