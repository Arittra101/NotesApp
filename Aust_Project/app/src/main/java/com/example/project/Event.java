package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Event extends AppCompatActivity {

    EditText title;
    EditText location;
    EditText description;
    Button addevent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        title = findViewById(R.id.etTitle);
        location = findViewById(R.id.etlocation);
        description = findViewById(R.id.etDescription);
        addevent = findViewById(R.id.btnadd1);


        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!title.getText().toString().isEmpty() && !location.getText().toString().isEmpty()
                        && !description.getText().toString().isEmpty()) {


                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setData(CalendarContract.Events.CONTENT_URI);
                    intent.putExtra(CalendarContract.Events.TITLE, title.getText().toString());
                    intent.putExtra(CalendarContract.Events.DESCRIPTION, description.getText().toString());
                    intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location.getText().toString());

                    intent.putExtra(CalendarContract.Events.ALL_DAY, true);
                    intent.putExtra(Intent.EXTRA_EMAIL, "test@yahoo.com,test3@yahoo.com");

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(), "there is no app that can support this action",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill all the feilds",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}