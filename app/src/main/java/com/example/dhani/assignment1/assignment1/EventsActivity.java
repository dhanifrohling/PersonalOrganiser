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
import java.util.Calendar;
import android.widget.Toast;

import java.util.ArrayList;

public class EventsActivity extends AppCompatActivity
{
    ArrayList alEvents;
    ListView lvEvents;
    String[][] events;
    DatabaseManager databaseManager;
    String event, ended, minute, hour, ampm;
    TasksAdapter adapter;
    Button btnAdd, btnDelete;
    boolean deleteModeEnabled = false;
    ArrayList<Integer> alEventsToDelete = new ArrayList<Integer>(100);


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Fill objects.
        alEvents = new ArrayList();
        lvEvents = (ListView) findViewById(R.id.lvEvents);

        databaseManager = new DatabaseManager(EventsActivity.this);

        // Populate events-array.
        events = databaseManager.queryAllEvents();

        // Close connection to database.
        databaseManager.close();

        // Get current time and date.
        Calendar calendar = Calendar.getInstance();
        int currentMinute = calendar.get(Calendar.MINUTE);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);
        ended = "";
        ampm = "";


        // Output events to listview.

        // For each event.
        for (int i = 0; i < events.length; i++)
        {

            if (Integer.parseInt(events[i][6]) < currentYear)
                ended = " (Ended)";
            else if(Integer.parseInt(events[i][6]) > currentYear)
                ended = "";
            else
            {
                if (Integer.parseInt(events[i][5]) < currentMonth)
                    ended = " (Ended)";
                else if(Integer.parseInt(events[i][5]) > currentMonth)
                    ended = "";
                else
                {
                    if (Integer.parseInt(events[i][4]) < currentDay)
                        ended = " (Ended)";
                    else if(Integer.parseInt(events[i][4]) > currentDay)
                        ended = "";
                    else
                    {
                        if (Integer.parseInt(events[i][3]) < currentHour)
                            ended = " (Ended)";
                        else if(Integer.parseInt(events[i][3]) > currentHour)
                            ended = "";
                        else
                        {
                            if (Integer.parseInt(events[i][2]) < currentMinute)
                                ended = " (Ended)";
                            else if(Integer.parseInt(events[i][2]) >= currentMinute)
                                ended = "";
                        }
                    }
                }
            }

            // Hour.
            if (Integer.parseInt(events[i][3]) > 12)
            {
                hour = Integer.toString(Integer.parseInt(events[i][3]) - 12);
                ampm = "pm";
            }
            else
            {
                hour = events[i][3];
                ampm = "am";

            }

            // Minute.
            if (Integer.parseInt(events[i][2]) < 10)
                minute = '0' + events[i][2];
            else
                minute = events[i][2];

            event = "Event: " + events[i][1] + ended + '\n' + "Time/Date: " + hour + ':' +
                    minute + ampm + ", " +  events[i][4] + '/' + events[i][5] + '/' + events[i][6] +
                    '\n' + "Location: " + events[i][7];

            alEvents.add(new TasksDataModel(event, false));

            ended = "";
        }

        adapter = new TasksAdapter(alEvents, getApplicationContext());

        lvEvents.setAdapter(adapter);


        // Listview-item long click-listener.
        lvEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                // Enable delete-mode.
                deleteModeEnabled = true;

                // Make checkbox for each contact visible.
                for (int i = 0; i < lvEvents.getAdapter().getCount(); i++)
                {
                    CheckBox mCheckBox = (CheckBox) lvEvents.getChildAt(i).findViewById(R.id.checkBox);
                    mCheckBox.setVisibility(View.VISIBLE);
                }

                // Make checkbox checked for selected contact.
                TasksDataModel dataModel = (TasksDataModel)alEvents.get(position);

                dataModel.checked = !dataModel.checked;
                adapter.notifyDataSetChanged();

                // Add selected item to a list.
                CheckBox givenCheckBox = (CheckBox) lvEvents.getChildAt(position).findViewById(R.id.checkBox);

                if (givenCheckBox.isChecked())
                    alEventsToDelete.remove(Integer.parseInt(events[position][0]));
                else
                    alEventsToDelete.add(Integer.parseInt(events[position][0]));

                // Enable 'Delete' button.
                btnDelete = (Button) findViewById(R.id.btnDelete);
                btnDelete.setEnabled(true);

                return true;
            }
        });

        // Listview item click-listener.
        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id)
            {
                // If delete-mode isn't enabled.
                if (!deleteModeEnabled)
                {

                }
                // Else, if delete-mode is enabled.
                else
                {
                    // Check checkbox of selected event.
                    TasksDataModel dataModel = (TasksDataModel) alEvents.get(position);
                    dataModel.checked = !dataModel.checked;
                    adapter.notifyDataSetChanged();

                    //Toast.makeText(EventsActivity.this, dataModel.name + ": " + dataModel.checked, Toast.LENGTH_SHORT).show();

                    // Add selected item to a list.
                    CheckBox givenCheckBox = (CheckBox) lvEvents.getChildAt(position).findViewById(R.id.checkBox);

                    if (givenCheckBox.isChecked())
                        alEventsToDelete.remove(Integer.parseInt(events[position][0]));
                    else
                        alEventsToDelete.add(Integer.parseInt(events[position][0]));


                    // Check if there're any events selected & disable checkboxes if there isn't.
                    //
                    // Declare variable.
                    boolean checkedCheckboxFound = false;

                    // For each contact.
                    for (int i = 0; i < lvEvents.getAdapter().getCount(); i++)
                    {
                        // If given contact has its checkbox checked.
                       TasksDataModel contact = (TasksDataModel) alEvents.get(i);

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
                        for (int i = 0; i < lvEvents.getAdapter().getCount(); i++)
                        {
                            CheckBox mCheckBox = (CheckBox) lvEvents.getChildAt(i).findViewById(R.id.checkBox);
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


        // Add event-button click-event.
        btnAdd = (Button) findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), AddEventActivity.class));
            }
        });

        btnDelete = (Button) findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Connect to database.
                databaseManager = new DatabaseManager(EventsActivity.this);

                // Delete selected friend(s).
                for (int i = 0; i < alEventsToDelete.size(); i++)
                    databaseManager.deleteEvent(alEventsToDelete.get(i));

                Toast.makeText(EventsActivity.this, "Event(s) deleted.",
                        Toast.LENGTH_LONG).show();

                // Close connection to database.
                databaseManager.close();

                startActivity(new Intent(getApplicationContext(), EventsActivity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_events, menu);
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
            case R.id.miTasks:
                startActivity(new Intent(getApplicationContext(), TasksActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
