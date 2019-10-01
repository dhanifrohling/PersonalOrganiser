package com.example.dhani.assignment1;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewFriendActivity extends AppCompatActivity
{
    DatabaseManager databaseManager;
    String[][] allRecords;
    ImageView ivFriend;
    TextView tvFullName, tvGender, tvAge, tvAddress;
    Button btnMap, btnEdit, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Store selected-friend as integer.
        final int position = Integer.parseInt(getIntent().getStringExtra("SELECTED_FRIEND"));

        // Connect to database.
        databaseManager = new DatabaseManager(ViewFriendActivity.this);

        // Populate records-2d-array.
        allRecords = databaseManager.queryAllFriends();

        // Close connection to database.
        databaseManager.close();

        // Output data to screen.
        ivFriend = (ImageView) findViewById(R.id.ivFriend);
        tvFullName = (TextView) findViewById(R.id.tvFullName);
        tvGender = (TextView) findViewById(R.id.tvGender);
        tvAge = (TextView) findViewById(R.id.tvAge);
        tvAddress = (TextView) findViewById(R.id.tvAddress);

        String uri = "@drawable/" + allRecords[position][6];
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        ivFriend.setImageDrawable(res);

        tvFullName.setText(allRecords[position][1] + ' ' + allRecords[position][2]);
        tvGender.setText(allRecords[position][3]);
        tvAge.setText(allRecords[position][4]);
        tvAddress.setText(allRecords[position][5]);

        // Map-button click-event.
        btnMap = (Button) findViewById(R.id.btnMap);

        btnMap.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                intent.putExtra("LOCATION", tvAddress.getText().toString());
                startActivity(intent);
            }
        });


        // Edit-button click-event.
        btnEdit = (Button) findViewById(R.id.btnEdit);

        btnEdit.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), EditFriendActivity.class);
                intent.putExtra("FRIEND_ID", allRecords[position][0]);
                intent.putExtra("SELECTED_FRIEND", Integer.toString(position));
                startActivity(intent);
            }
        });

        // Delete-button click-event.
        btnDelete = (Button) findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Connect to database.
                databaseManager = new DatabaseManager(ViewFriendActivity.this);

                // Delete selected friend(s).
                if (databaseManager.deleteFriend(Integer.parseInt(allRecords[position][0])))
                {
                    Toast.makeText(ViewFriendActivity.this, "Friend deleted.",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(ViewFriendActivity.this, "Error deleting friend.",
                            Toast.LENGTH_LONG).show();
                }

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
        getMenuInflater().inflate(R.menu.menu_view_friend, menu);
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
            case R.id.miEvents:
                startActivity(new Intent(getApplicationContext(), EventsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
