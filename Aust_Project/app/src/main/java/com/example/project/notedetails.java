package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class notedetails extends AppCompatActivity {

    FloatingActionButton edit;
    TextView detail_title,detail_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetails);

        detail_title = findViewById(R.id.titledetail);
        detail_content = findViewById(R.id.contentofnotedetail);
        edit = findViewById(R.id.gotoeditnote);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent data = getIntent();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),editnoteactivity.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content",data.getStringExtra("content"));
                intent.putExtra("noteId",data.getStringExtra("noteId"));
                intent.putExtra("Bookmark",data.getStringExtra("Bookmark"));
                v.getContext().startActivity(intent);

            }
        });

        detail_title.setText(data.getStringExtra("title"));
        detail_content.setText(data.getStringExtra("content"));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}