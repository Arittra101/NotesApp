package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class addpicture extends AppCompatActivity implements View.OnClickListener{

    private  static  final int PICK_IMAGE_REQUEST = 1;
    EditText picture_title,picture_Des;
    TextView dateView,hourView;
    Button imageselectbtn,uploadimagebtn;
    Uri imageUri;
    ImageView imageView;
    ProgressBar progressBar;

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference= firebaseStorage.getReference();  //documentreference =  firebasestore.collection().....
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DocumentReference documentReference;

    String datatext;
    String hourtext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoadd);

      //  getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(addpicture.this,R.color.purple_2001));
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        Date hour = new Date();
        datatext = formatter.format(date);
        hourtext = formatter1.format(hour) ;



        imageselectbtn = findViewById(R.id.selectbtn);
        imageselectbtn.setOnClickListener(this);
        uploadimagebtn = findViewById(R.id.uploadpicbtn);
        uploadimagebtn.setOnClickListener(this);

        imageView = findViewById(R.id.img);
        progressBar = findViewById(R.id.spinner);

        picture_title = findViewById(R.id.pictitle);
        picture_Des = findViewById(R.id.desciption);



    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.selectbtn)
        {
            openFileChooser();
        }

        else if(v.getId()==R.id.uploadpicbtn)
        {
           Image_Upload();
           //Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser()
    {
        Intent intent = new Intent();                            //initialize intent obf from class
        intent.setType("image/*");                              //we have to give the type of the intent we want to get
        intent.setAction(Intent.ACTION_GET_CONTENT);            //have to set Action about any content and it data type is image
        startActivityForResult(intent,PICK_IMAGE_REQUEST);     //to get the result of intent request we use startActivityForResult
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imageUri =  data.getData();
            Picasso.get().load(imageUri).into(imageView);
        }
    }

    public String getFileExtention(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void Image_Upload()
    {
        if(imageUri!=null)
        {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtention(imageUri));
            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    progressBar.setVisibility(View.GONE);

                    Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String uri_string = uri.toString();
                            Toast.makeText(getApplicationContext(),"Uploaded Successfully" ,Toast.LENGTH_SHORT).show();
                            documentReference = firebaseFirestore.collection("notes").document(user.getUid()).collection("Image").document();
                            // Upload upload = new Upload(picture_title.getText().toString(),datatext,hourtext,taskSnapshot.toString());
                            Map<String,Object> m = new HashMap<>();
                            m.put("image_title",picture_title.getText().toString());
                            m.put("date",datatext);
                            m.put("hour",hourtext);
                            m.put("imageUrl",uri_string);
                            m.put("description",picture_Des.getText().toString());

                            documentReference.set(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"Also added Database",Toast.LENGTH_SHORT).show();
                                    finish();
                                   // startActivity(new Intent(getApplicationContext(),RImage.class));

                                }
                            });
                        }
                    });



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    progressBar.setVisibility(View.VISIBLE);
                    double progress = (100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });
        }


    }
}