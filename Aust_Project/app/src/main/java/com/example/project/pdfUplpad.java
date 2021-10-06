package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class pdfUplpad extends AppCompatActivity {

    EditText editText;
    Button upload,retrive;
    StorageReference storageReference;
    DocumentReference documentReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_uplpad);
        getWindow().setStatusBarColor(ContextCompat.getColor(pdfUplpad.this,R.color.purple_2001));
        editText = findViewById(R.id.editText);
        upload = findViewById(R.id.uploadView);
        retrive = findViewById(R.id.retrieve);
        retrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),retrievepdfd.class));
            }
        });
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("uploadPDF");
        upload.setEnabled(false);

        //select pdf
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();
            }
        });
    }

    //collect pdf file from storage
    private void selectPDF()
    {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"PDF FILE SELECT"),12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== 12&& resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            upload.setEnabled(true);
            editText.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/")+1));

            //fetch pdf from storage
            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadPDFFileFirebase(data.getData());
                }
            });
        }
    }

    private void uploadPDFFileFirebase(Uri data) {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("File is Loading");
        progressDialog.show();
        StorageReference reference=storageReference.child("upload"+System.currentTimeMillis()+".pdf");

        //
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri =uriTask.getResult();
                        Date currentDate = new Date();
                        SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm:ss a");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("E, MMM dd yyyy");

                        String date_edit = dateFormat.format(currentDate);
                        String  time_edit =  timeformat.format(currentDate);
                        String total = date_edit+" "+ time_edit;
                       // putPdf putPDF=new putPdf(editText.getText().toString(),uri.toString());
                        Map<String,Object> m = new HashMap<>();
                        documentReference = firebaseFirestore.collection("notes").document(user.getUid()).collection("PDF").document();
                        m.put("name",editText.getText().toString());
                        m.put("url",uri.toString());
                        m.put("time",total);
                        documentReference.set(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"File Upload", Toast.LENGTH_SHORT).show();
                            }
                        });
                     //   databaseReference.child(databaseReference.push().getKey()).setValue(putPDF);
                        Toast.makeText(getApplicationContext(),"File Upload", Toast.LENGTH_SHORT).show();
                        editText.setText("");
                        progressDialog.dismiss();


                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                double progress =(100.0* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("File Uploaded....."+(int) progress+"%");

            }
        });
    }
}