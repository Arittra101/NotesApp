package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class showImage extends AppCompatActivity {

    TextView title_view,date_view,des_view;
    String title;
    String des;
    String url;
    String docId ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        getWindow().setStatusBarColor(ContextCompat.getColor(showImage.this,R.color.purple_2001));
        ImageView imageView = findViewById(R.id.img1);
        Intent data = getIntent();
        String date = data.getStringExtra("date");
        String hour = data.getStringExtra("hour");
        String total = date + " " +hour;
         des = data.getStringExtra("discription");
         title = data.getStringExtra("imagetitle");
         docId = data.getStringExtra("docId");
         url = data.getStringExtra("imageUrl");


        Picasso.get().load(url).into(imageView);
        title_view = findViewById(R.id.title_detail);
        date_view = findViewById(R.id.time_detail);
        des_view = findViewById(R.id.des_detail);

        title_view.setText(title);
        date_view.setText(total);
        des_view.setText(des);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu3,menu);   //layout,parent,false
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.p_edit)
        {
            dioluge_pop();
        }
        return super.onOptionsItemSelected(item);
    }

    public void dioluge_pop()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(showImage.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View v = new View(showImage.this) ;
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.pop_edit,viewGroup,false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView edit_title = dialogView.findViewById(R.id.p_title_edit);
        TextView edit_des = dialogView.findViewById(R.id.p_des_edit);
        Button update = dialogView.findViewById(R.id.t_update);

        edit_title.setText(title_view.getText().toString());
        edit_des.setText(des_view.getText().toString());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_String =edit_title.getText().toString();
                String des_String =edit_des.getText().toString();
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

                Date currentDate = new Date();
                SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm:ss a");
                SimpleDateFormat dateFormat = new SimpleDateFormat("E, MMM dd yyyy");

               String date_edit = dateFormat.format(currentDate);
                String  time_edit =  timeformat.format(currentDate);

                DocumentReference documentReference = firebaseFirestore.collection("notes").document(user.getUid()).collection("Image").document(docId);

                Map<String,Object> m = new HashMap<>();
                m.put("date",date_edit);
                m.put("description",des_String);
                m.put("hour",time_edit);

                m.put("imageUrl",url);
                m.put("image_title",title_String);

                documentReference.set(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       // finish();
                        //startActivity(new Intent(getApplicationContext(),RImage.class));
                        title_view.setText(title_String);
                        des_view.setText(des_String);
                        String total_time = date_edit+time_edit;
                        date_view .setText(total_time);
                        Toast.makeText(getApplicationContext(),"Edit p",Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog.dismiss();
            }
        });
    }
}