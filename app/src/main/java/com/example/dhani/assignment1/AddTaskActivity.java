package com.example.dhani.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class AddTaskActivity extends AppCompatActivity
{
    EditText etName, etLocation;
    Button btnAdd;
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add task-button click-event.
        btnAdd = (Button)findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                etName = (EditText)findViewById(R.id.etName);
                etLocation = (EditText)findViewById(R.id.etLocation);
                String name, location;

                // Task-name.
                if (etName.getText().toString().equals(""))
                    name = "Untitled Task";
                else
                    name = etName.getText().toString();

                // Task-location.
                location = etLocation.getText().toString();

                // Connect to database.
                databaseManager = new DatabaseManager(AddTaskActivity.this);

                // Add input to database with validation.
                if (databaseManager.addTask(name, location))
                    Toast.makeText(AddTaskActivity.this, "Task added.",
                            Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(AddTaskActivity.this, "Error adding new task.",
                            Toast.LENGTH_LONG).show();

                // Close database-connection.
                databaseManager.close();

                // Redirect to contact-list.
                startActivity(new Intent(getApplicationContext(), TasksActivity.class));
            }
        });
    }
}
