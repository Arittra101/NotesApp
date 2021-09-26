package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class pdfUplpad extends AppCompatActivity {

    EditText editText;
    Button upload,retrive;
    StorageReference storageReference;
    DocumentReference documentReference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_uplpad);

        editText = findViewById(R.id.editText);
        upload = findViewById(R.id.uploadView);
        retrive = findViewById(R.id.retrieve);
        retrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),RetrievePdf.class));
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
                        putPdf putPDF=new putPdf(editText.getText().toString(),uri.toString());
                        databaseReference.child(databaseReference.push().getKey()).setValue(putPDF);
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