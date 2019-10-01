package com.example.dhani.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ViewTaskActivity extends AppCompatActivity
{
    int position;
    DatabaseManager databaseManager;
    String[][] allTasks;
    EditText etName, etLocation, etStatus;
    Button btnStatus, btnUpdate, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Store task-position as integer.
        position = Integer.parseInt(getIntent().getStringExtra("SELECTED_TASK"));

        // Connect to database.
        databaseManager = new DatabaseManager(ViewTaskActivity.this);

        // Populate records-2d-array.
        allTasks = databaseManager.queryAllTasks();

        // Close connection to database.
        databaseManager.close();

        // Output data to screen.
        etName = (EditText) findViewById(R.id.etName);
        etLocation = (EditText) findViewById(R.id.etLocation);
        etStatus = (EditText) findViewById(R.id.etStatus);

        etName.setText(allTasks[position][1]);
        etLocation.setText(allTasks[position][2]);

        btnStatus = (Button)findViewById(R.id.btnStatus);

        if (allTasks[position][3].equals("INCOMPLETE"))
        {
            etStatus.setText("Incomplete");
            etStatus.setTextColor(getResources().getColor(R.color.taskIncomplete));
            btnStatus.setText("Mark Complete");
        }
        else
        {
            etStatus.setText("Complete");
            etStatus.setTextColor(getResources().getColor(R.color.taskComplete));
            btnStatus.setText("Mark Incomplete");
        }

        // Status-button click-event.
        btnStatus.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Connect to database.
                databaseManager = new DatabaseManager(ViewTaskActivity.this);

                // If task-status is incomplete.
                if (etStatus.getText().toString().equals("Incomplete"))
                {
                    databaseManager.updateTaskStatus(Integer.parseInt(allTasks[position][0]), "COMPLETE");
                    etStatus.setText("Complete");
                    etStatus.setTextColor(getResources().getColor(R.color.taskComplete));
                    btnStatus.setText("Mark Incomplete");
                }
                // Else, if task-status is complete.
                else
                {
                    databaseManager.updateTaskStatus(Integer.parseInt(allTasks[position][0]), "INCOMPLETE");
                    etStatus.setText("Incomplete");
                    etStatus.setTextColor(getResources().getColor(R.color.taskIncomplete));
                    btnStatus.setText("Mark Complete");
                }

                // Close connection to database.
                databaseManager.close();
            }
        });

        // Update-button click-event.
        btnUpdate = (Button)findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                // Get task-status.
                String status;

                if (etStatus.getText().toString().equals("Complete"))
                    status = "COMPLETE";
                else
                    status = "INCOMPLETE";

                // Connect to database.
                databaseManager = new DatabaseManager(ViewTaskActivity.this);

                // Add updated task to database with validation.
                if (databaseManager.updateTask(Integer.parseInt(allTasks[position][0]),
                        etName.getText().toString(), etLocation.getText().toString(), status))
                {
                    Toast.makeText(ViewTaskActivity.this, "Task updated.",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(ViewTaskActivity.this, "Error updating task.",
                            Toast.LENGTH_LONG).show();
                }

                // Close connection to database.
                databaseManager.close();

                // Go to tasks-activity.
                startActivity(new Intent(getApplicationContext(), TasksActivity.class));
            }
        });

        // Delete-button click-event.
        btnDelete = (Button)findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Connect to database.
                databaseManager = new DatabaseManager(ViewTaskActivity.this);

                // Delete selected friend(s).
                if (databaseManager.deleteTask(Integer.parseInt(allTasks[position][0])))
                {
                    Toast.makeText(ViewTaskActivity.this, "Task deleted.",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(ViewTaskActivity.this, "Error deleting task.",
                            Toast.LENGTH_LONG).show();
                }

                // Close connection to database.
                databaseManager.close();

                // Go to tasks-activity.
                startActivity(new Intent(getApplicationContext(), TasksActivity.class));
            }
        });
    }

}
