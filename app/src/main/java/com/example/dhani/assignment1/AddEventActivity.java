package com.example.dhani.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.DatePicker;
import android.widget.Toast;

public class AddEventActivity extends AppCompatActivity
{
    EditText etName, etLocation;
    TimePicker tpTime;
    DatePicker dpDate;
    Button btnAdd;
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add event-button click-event.
        btnAdd = (Button)findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                etName = (EditText)findViewById(R.id.etName);
                tpTime = (TimePicker)findViewById(R.id.tpTime);
                dpDate = (DatePicker)findViewById(R.id.dpDate);
                etLocation = (EditText)findViewById(R.id.etLocation);
                String name, location;
                int minute, hour, day, month, year;

                // Event-name.
                if (etName.getText().toString().equals(""))
                    name = "Untitled Event";
                else
                    name = etName.getText().toString();

                // Event-time.
                minute = tpTime.getCurrentMinute();
                hour = tpTime.getCurrentHour();

                // Event-date.
                day = dpDate.getDayOfMonth();
                month = dpDate.getMonth() + 1;
                year = dpDate.getYear();

                // Event-location.
                location = etLocation.getText().toString();

                // Connect to database.
                databaseManager = new DatabaseManager(AddEventActivity.this);

                // Add input to database with validation.
                if (databaseManager.addEvent(name, Integer.toString(minute),
                        Integer.toString(hour), Integer.toString(day), Integer.toString(month),
                        Integer.toString(year), location))
                    Toast.makeText(AddEventActivity.this, "Event added.",
                            Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(AddEventActivity.this, "Error adding new event.",
                            Toast.LENGTH_LONG).show();

                // Close database-connection.
                databaseManager.close();

                // Redirect to contact-list.
                startActivity(new Intent(getApplicationContext(), EventsActivity.class));
            }
        });
    }
}
