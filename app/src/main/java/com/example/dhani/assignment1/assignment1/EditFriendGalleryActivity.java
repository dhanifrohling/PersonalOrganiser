package com.example.dhani.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class EditFriendGalleryActivity extends AppCompatActivity
{

    ImageButton ib1, ib2, ib3, ib4, ib5, ib6, ib7, ib8, ib9;
    String id, position, firstname, lastname, gender, age, address;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ib1 = (ImageButton)findViewById(R.id.ib1);
        ib2 = (ImageButton)findViewById(R.id.ib2);
        ib3 = (ImageButton)findViewById(R.id.ib3);
        ib4 = (ImageButton)findViewById(R.id.ib4);
        ib5 = (ImageButton)findViewById(R.id.ib5);
        ib6 = (ImageButton)findViewById(R.id.ib6);
        ib7 = (ImageButton)findViewById(R.id.ib7);
        ib8 = (ImageButton)findViewById(R.id.ib8);
        ib9 = (ImageButton)findViewById(R.id.ib9);

        // Get inputted data.
        id = getIntent().getStringExtra("FRIEND_ID");
        position = getIntent().getStringExtra("SELECTED_FRIEND");

        firstname = getIntent().getStringExtra("FIRSTNAME");
        lastname = getIntent().getStringExtra("LASTNAME");
        gender = getIntent().getStringExtra("GENDER"); // As index.
        age = getIntent().getStringExtra("AGE");
        address = getIntent().getStringExtra("ADDRESS");

        // Redirect to gallery saving inputted data.
        intent = new Intent(getBaseContext(), EditFriendActivity.class);

        intent.putExtra("FRIEND_ID", id);
        intent.putExtra("SELECTED_FRIEND", position);

        intent.putExtra("FIRSTNAME",firstname);
        intent.putExtra("LASTNAME", lastname);
        intent.putExtra("GENDER", gender);
        intent.putExtra("AGE", age);
        intent.putExtra("ADDRESS", address);

        ib1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                intent.putExtra("PICTURE", "pic1");

                startActivity(intent);
            }
        });

        ib2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                intent.putExtra("PICTURE", "pic2");

                startActivity(intent);
            }
        });

        ib3.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                intent.putExtra("PICTURE", "pic3");

                startActivity(intent);
            }
        });

        ib4.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                intent.putExtra("PICTURE", "pic4");

                startActivity(intent);
            }
        });

        ib5.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                intent.putExtra("PICTURE", "pic5");

                startActivity(intent);
            }
        });

        ib6.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                intent.putExtra("PICTURE", "pic6");

                startActivity(intent);
            }
        });

        ib7.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                intent.putExtra("PICTURE", "pic7");

                startActivity(intent);
            }
        });

        ib8.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                intent.putExtra("PICTURE", "pic8");

                startActivity(intent);
            }
        });

        ib9.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                intent.putExtra("PICTURE", "pic9");

                startActivity(intent);
            }
        });
    }
}
