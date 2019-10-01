package com.example.dhani.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.graphics.Color;
import android.widget.Toast;

import java.util.ArrayList;

public class TasksActivity extends AppCompatActivity
{
    ArrayList alTasks;
    ListView lvTasks;
    String[][] tasks;
    DatabaseManager databaseManager;
    String task, status;
    TasksAdapter adapter;
    Button btnAdd, btnDelete;
    boolean deleteModeEnabled = false;
    ArrayList<Integer> alTasksToDelete = new ArrayList<Integer>(100);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Fill objects.
        alTasks = new ArrayList();
        lvTasks = (ListView) findViewById(R.id.lvTasks);

        databaseManager = new DatabaseManager(TasksActivity.this);

        // Populate tasks-array.
        tasks = databaseManager.queryAllTasks();

        // Close connection to database.
        databaseManager.close();

        // Output tasks to listview.

        // For each task.
        for (int i = 0; i < tasks.length; i++)
        {
            // Build task-string.
            if (tasks[i][3].equals("INCOMPLETE"))
            {
                status = "Incomplete";
            }
            else
            {
                status = "Complete";
            }

            task = "Task: " + tasks[i][1] + '\n' + "Location: " + tasks[i][2] + '\n' + "Status: " + status;


            alTasks.add(new TasksDataModel(task, false));
        }
        //alTasks.add(new TasksDataModel("Hello", false));

        adapter = new TasksAdapter(alTasks, getApplicationContext());

        lvTasks.setAdapter(adapter);


        // Listview-item long click-listener.
        lvTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                // Enable delete-mode.
                deleteModeEnabled = true;

                // Make checkbox for each contact visible.
                for (int i = 0; i < lvTasks.getAdapter().getCount(); i++)
                {
                    CheckBox mCheckBox = (CheckBox) lvTasks.getChildAt(i).findViewById(R.id.checkBox);
                    mCheckBox.setVisibility(View.VISIBLE);
                }

                // Make checkbox checked for selected contact.
                TasksDataModel dataModel = (TasksDataModel)alTasks.get(position);

                dataModel.checked = !dataModel.checked;
                adapter.notifyDataSetChanged();

                // Add selected item to a list.
                CheckBox givenCheckBox = (CheckBox) lvTasks.getChildAt(position).findViewById(R.id.checkBox);

                if (givenCheckBox.isChecked())
                    alTasksToDelete.remove(Integer.parseInt(tasks[position][0]));
                else
                    alTasksToDelete.add(Integer.parseInt(tasks[position][0]));

                // Enable 'Delete' button.
                btnDelete = (Button) findViewById(R.id.btnDelete);
                btnDelete.setEnabled(true);

                return true;
            }
        });

        // Listview item click-listener.
        lvTasks.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id)
            {
                // If delete-mode isn't enabled.
                if (!deleteModeEnabled)
                {
                    // View task.
                    Intent intent = new Intent(getBaseContext(), ViewTaskActivity.class);
                    intent.putExtra("SELECTED_TASK", Integer.toString(position));
                    startActivity(intent);
                }
                // Else, if delete-mode is enabled.
                else
                {
                    // Check checkbox of selected task.
                    TasksDataModel dataModel = (TasksDataModel) alTasks.get(position);
                    dataModel.checked = !dataModel.checked;
                    adapter.notifyDataSetChanged();

                    // Add selected item to a list.
                    CheckBox givenCheckBox = (CheckBox) lvTasks.getChildAt(position).findViewById(R.id.checkBox);

                    if (givenCheckBox.isChecked())
                        alTasksToDelete.remove(Integer.parseInt(tasks[position][0]));
                    else
                        alTasksToDelete.add(Integer.parseInt(tasks[position][0]));


                    // Check if there're any contacts selected & disable checkboxes if there isn't.

                    // Declare variable.
                    boolean checkedCheckboxFound = false;

                    // For each contact.
                    for (int i = 0; i < lvTasks.getAdapter().getCount(); i++)
                    {
                        // If given contact has its checkbox checked.
                        TasksDataModel contact = (TasksDataModel) alTasks.get(i);

                        if (contact.checked)
                        {
                            // Store that a checkbox was found checked.
                            checkedCheckboxFound = true;

                            break;
                        }
                    }

                    // If a checked checkboxes was not found.
                    if (!checkedCheckboxFound)
                    {
                        // Make all checkboxes invincible.
                        for (int i = 0; i < lvTasks.getAdapter().getCount(); i++)
                        {
                            CheckBox mCheckBox = (CheckBox) lvTasks.getChildAt(i).findViewById(R.id.checkBox);
                            mCheckBox.setVisibility(View.INVISIBLE);
                        }

                        // Disable 'Delete' button.
                        btnDelete = (Button) findViewById(R.id.btnDelete);
                        btnDelete.setEnabled(false);

                        // Disable delete-mode.
                        deleteModeEnabled = false;
                    }
                }
            }
        });


        // Add task-button click-event.
        btnAdd = (Button) findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), AddTaskActivity.class));
            }
        });

        btnDelete = (Button) findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Connect to database.
                databaseManager = new DatabaseManager(TasksActivity.this);

                // Delete selected friend(s).
                for (int i = 0; i < alTasksToDelete.size(); i++)
                    databaseManager.deleteEvent(alTasksToDelete.get(i));

                Toast.makeText(TasksActivity.this, "Tasks(s) deleted.",
                        Toast.LENGTH_LONG).show();

                // Close connection to database.
                databaseManager.close();

                startActivity(new Intent(getApplicationContext(), TasksActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tasks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Menubar functionality.
        switch (item.getItemId())
        {
            case R.id.miHome:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            case R.id.miFriends:
                startActivity(new Intent(getApplicationContext(), FriendsActivity.class));
                return true;
            case R.id.miEvents:
                startActivity(new Intent(getApplicationContext(), EventsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
