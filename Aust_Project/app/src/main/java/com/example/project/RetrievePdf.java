package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RetrievePdf extends AppCompatActivity {
    ListView listView;
    DatabaseReference databaseReference;
    List<putPdf> uploadedPDF;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_pdf);

        getWindow().setStatusBarColor(ContextCompat.getColor(RetrievePdf.this,R.color.purple_2001));
        listView = findViewById(R.id.listView);

        uploadedPDF = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();   //user id
        databaseReference = FirebaseDatabase.getInstance().getReference("uploadPDF").child(user.getUid());  //reference

        retrievePDFFiles();   //pdf retrive

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {        //for download
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                putPdf putPDF=uploadedPDF.get(i);
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setType("application.pdf");
                intent.setData(Uri.parse(putPDF.getUrl()));
                startActivity(intent);

            }
        });


    }


    private void retrievePDFFiles()
    {
        databaseReference= FirebaseDatabase.getInstance().getReference("uploadPDF");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    putPdf putPDF=ds.getValue(com.example.project.putPdf.class);
                    uploadedPDF.add(putPDF);                                             //firebase realtime-> list //uploarpdf->>arraylist
                }

                String[] uploadsName=new String[uploadedPDF.size()];
                for (int i=0;i<uploadsName.length;i++)
                {
                    uploadsName[i]=uploadedPDF.get(i).getName();                            //pdf name -> string array

                }
                ArrayAdapter<String>arrayAdapter= new ArrayAdapter<String>(getApplicationContext(),             //arraysetadapter
                        android.R.layout.simple_list_item_1,uploadsName){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view=super.getView(position, convertView,parent);
                        TextView textView=(TextView) view.findViewById(android.R.id.text1);
                        textView.setTextColor(Color.BLACK);
                        textView.setTextSize(20);
                        return view;
                    }
                };

                listView.setAdapter(arrayAdapter);   //set list view arrayadapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}