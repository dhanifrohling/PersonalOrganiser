package com.example.dhani.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ImageView;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class AddFriendActivity extends AppCompatActivity
{
    List<String> lGenders;
    ArrayAdapter<String> aaGender;
    Spinner spGender;
    DatabaseManager databaseManager;
    EditText etFirstname, etLastname, etAge, etAddress;
    Button btnAdd, btnChangePic;
    boolean bResult;
    Intent intent2;
    Bundle extras;
    String firstname, lastname, gender, age, address, picture = "default_pic";
    ImageView ivFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Populate string-list.
        lGenders =  new ArrayList<String>();
        lGenders.add("Gender...");
        lGenders.add("Male");
        lGenders.add("Female");

        // Initalise array-adapter & add list of genders to it.
        aaGender = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lGenders);

        // Set drop-down view resource to array-adapter.
        aaGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Add array-adapter to gender-spinner.
        spGender = (Spinner) findViewById(R.id.spGender);
        spGender.setAdapter(aaGender);

        // If traversing from image-gallery.
        intent2 = getIntent();
        extras = intent2.getExtras();

        ivFriend = (ImageView) findViewById(R.id.ivFriend);

        if (extras != null)
        {
            firstname = getIntent().getStringExtra("FIRSTNAME");
            lastname = getIntent().getStringExtra("LASTNAME");
            gender = getIntent().getStringExtra("GENDER"); // As index.
            age = getIntent().getStringExtra("AGE");
            address = getIntent().getStringExtra("ADDRESS");
            picture = getIntent().getStringExtra("PICTURE");

            etFirstname = (EditText)findViewById(R.id.etFirstname);
            etLastname = (EditText)findViewById(R.id.etLastname);
            etAge = (EditText)findViewById(R.id.etAge);
            etAddress = (EditText)findViewById(R.id.etAddress);

            String uri = "@drawable/" + picture;
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());

            Drawable res = getResources().getDrawable(imageResource);

            ivFriend.setImageDrawable(res);
            etFirstname.setText(firstname);
            etLastname.setText(lastname);
            spGender.setSelection(Integer.parseInt(gender));
            etAge.setText(age);
            etAddress.setText(address);
        }

        btnChangePic = (Button)findViewById(R.id.btnChangePic);

        // Change picture-button click-event.
        btnChangePic.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {


                etFirstname = (EditText)findViewById(R.id.etFirstname);
                etLastname = (EditText)findViewById(R.id.etLastname);
                etAge = (EditText)findViewById(R.id.etAge);
                etAddress = (EditText)findViewById(R.id.etAddress);

                // Redirect to gallery saving inputted data.
                Intent intent = new Intent(getBaseContext(), GalleryActivity.class);

                intent.putExtra("FIRSTNAME", etFirstname.getText().toString());
                intent.putExtra("LASTNAME", etLastname.getText().toString());

                String gender;
                if (spGender.getSelectedItem().toString().equals("Gender..."))
                    gender = "0";
                else if (spGender.getSelectedItem().toString().equals("Male"))
                    gender = "1";
                else
                    gender = "2";
                intent.putExtra("GENDER", gender);

                intent.putExtra("AGE", etAge.getText().toString());
                intent.putExtra("ADDRESS", etAddress.getText().toString());

                startActivity(intent);
            }
        });

        btnAdd = (Button) findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                etFirstname = (EditText)findViewById(R.id.etFirstname);
                etLastname = (EditText)findViewById(R.id.etLastname);
                etAge = (EditText)findViewById(R.id.etAge);
                etAddress = (EditText)findViewById(R.id.etAddress);

                // Check gender was selected.
                String gender;

                if (!etAge.getText().toString().matches("[0-9]+"))
                {
                    Toast.makeText(AddFriendActivity.this, "Invaid age-input.",
                            Toast.LENGTH_LONG).show();

                    // Exit button-click event early.
                    return;
                }

                if (spGender.getSelectedItem().toString().equals("Gender..."))
                    gender = "";
                else
                    gender = spGender.getSelectedItem().toString();

                // Connect to database.
                databaseManager = new DatabaseManager(AddFriendActivity.this);

                // Add input to database.
                bResult = databaseManager.addFriend (
                        etFirstname.getText().toString(),
                        etLastname.getText().toString(),
                        etAge.getText().toString(),
                        age,
                        etAddress.getText().toString(),
                        picture
                );

                // Close connection to database.
                databaseManager.close();

                // Output result of adding contact as toast-notification.
                if (bResult)
                {
                    Toast.makeText(AddFriendActivity.this, "Friend added.",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(AddFriendActivity.this, "Error adding new friend.",
                            Toast.LENGTH_LONG).show();
                }

                // Redirect to contact-list.
                startActivity(new Intent(getApplicationContext(), FriendsActivity.class));
            }
        });
    }
}
