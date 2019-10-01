package com.example.dhani.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

public class FriendsActivity extends AppCompatActivity
{
    ArrayList alFriendNames;
    ListView lvFriends;
    String[] friendNames;
    String[][] friends;
    DatabaseManager databaseManager;
    CustomAdapter adapter;
    Button btnAdd, btnDelete;
    boolean deleteModeEnabled = false;
    ArrayList<Integer> alFriendsToDelete = new ArrayList<Integer>(100);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Fill objects.
        alFriendNames = new ArrayList();
        lvFriends = (ListView) findViewById(R.id.lvFriends);
        databaseManager = new DatabaseManager(FriendsActivity.this);

        friends = databaseManager.queryAllFriends();

                // Populate name-array.
        friendNames = databaseManager.retrieveNames();

        // Close connection to database.
        databaseManager.close();

        // Output names to listview.
        for (int i = 0; i < friendNames.length; i++)
            alFriendNames.add(new MyDataModel(friendNames[i], false));

        adapter = new CustomAdapter(alFriendNames, getApplicationContext());

        lvFriends.setAdapter(adapter);

        lvFriends.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                // Enable delete-mode.
                deleteModeEnabled = true;

                // Make checkbox for each contact visible.
                for (int i = 0; i < lvFriends.getAdapter().getCount(); i++)
                {
                    CheckBox mCheckBox = (CheckBox) lvFriends.getChildAt(i).findViewById(R.id.checkBox);
                    mCheckBox.setVisibility(View.VISIBLE);
                }

                // Make checkbox checked for selected contact.
                MyDataModel dataModel = (MyDataModel)alFriendNames.get(position);

                dataModel.checked = !dataModel.checked;
                adapter.notifyDataSetChanged();

                // Add selected item to a list.
                CheckBox givenCheckBox = (CheckBox) lvFriends.getChildAt(position).findViewById(R.id.checkBox);

                if (givenCheckBox.isChecked())
                    alFriendsToDelete.remove(Integer.parseInt(friends[position][0]));
                else
                    alFriendsToDelete.add(Integer.parseInt(friends[position][0]));

                // Enable 'Delete' button.
                btnDelete = (Button) findViewById(R.id.btnDelete);
                btnDelete.setEnabled(true);

                return true;
            }
        });

        lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id)
            {
                // If delete-mode isn't enabled.
                if (!deleteModeEnabled)
                {
                    // View friends record.
                    Intent intent = new Intent(getBaseContext(), ViewFriendActivity.class);
                    intent.putExtra("SELECTED_FRIEND", Integer.toString(position));
                    startActivity(intent);
                }
                // Else, if delete-mode is enabled.
                else
                {
                    // Check checkbox of selected friend.
                    MyDataModel dataModel = (MyDataModel) alFriendNames.get(position);
                    dataModel.checked = !dataModel.checked;
                    adapter.notifyDataSetChanged();

                    // Add selected item to a list.
                    CheckBox givenCheckBox = (CheckBox) lvFriends.getChildAt(position).findViewById(R.id.checkBox);

                    if (givenCheckBox.isChecked())
                        alFriendsToDelete.remove(Integer.parseInt(friends[position][0]));
                    else
                        alFriendsToDelete.add(Integer.parseInt(friends[position][0]));


                    // Check if there're any contacts selected & disable checkboxes if there isn't.

                    // Declare variable.
                    boolean checkedCheckboxFound = false;

                    // For each contact.
                    for (int i = 0; i < lvFriends.getAdapter().getCount(); i++)
                    {
                        // If given contact has its checkbox checked.
                        MyDataModel contact = (MyDataModel) alFriendNames.get(i);

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
                        for (int i = 0; i < lvFriends.getAdapter().getCount(); i++)
                        {
                            CheckBox mCheckBox = (CheckBox) lvFriends.getChildAt(i).findViewById(R.id.checkBox);
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

        btnAdd = (Button) findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), AddFriendActivity.class));
            }
        });

        btnDelete = (Button) findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Connect to database.
                databaseManager = new DatabaseManager(FriendsActivity.this);

                // Delete selected friend(s).
                for (int i = 0; i < alFriendsToDelete.size(); i++)
                    databaseManager.deleteEvent(alFriendsToDelete.get(i));

                Toast.makeText(FriendsActivity.this, "Friend(s) deleted.",
                        Toast.LENGTH_LONG).show();

                // Close connection to database.
                databaseManager.close();

                startActivity(new Intent(getApplicationContext(), FriendsActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friends, menu);
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
            case R.id.miTasks:
                startActivity(new Intent(getApplicationContext(), TasksActivity.class));
                return true;
            case R.id.miEvents:
                startActivity(new Intent(getApplicationContext(), EventsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
