package com.example.dhani.assignment1;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import java.util.List;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class EditFriendActivity extends AppCompatActivity
{
    List<String> lGenders;
    ArrayAdapter<String> aaGender;
    Spinner spGender;
    private DatabaseManager databaseManager;
    ImageView ivFriend;
    private EditText etFirstname, etLastname, etAge, etAddress;
    private Button btnChangePic, btnEdit;
    private boolean bResult;
    String[] friendRecord;
    Intent intent2;
    Bundle extras;
    String firstname, lastname, gender, age, address, picture;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ivFriend = (ImageView) findViewById(R.id.ivFriend);
        etFirstname = (EditText) findViewById(R.id.etFirstname);
        etLastname = (EditText) findViewById(R.id.etLastname);
        etAge = (EditText) findViewById(R.id.etAge);
        etAddress = (EditText) findViewById(R.id.etAddress);

        // Store selected-friend as integer.
        final int id = Integer.parseInt(getIntent().getStringExtra("FRIEND_ID"));
        final int position = Integer.parseInt(getIntent().getStringExtra("SELECTED_FRIEND"));

        // Populate array-list.
        lGenders =  new ArrayList<String>();
        lGenders.add("Gender...");
        lGenders.add("Male");
        lGenders.add("Female");

        // Add list of genders to array-adapter.
        aaGender = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lGenders);
        aaGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Add adapter to spinner.
        spGender = (Spinner) findViewById(R.id.spGender);
        spGender.setAdapter(aaGender);

        // If traversing from image-gallery.
        //intent2 = getIntent();
        //extras = intent2.getExtras();

        ivFriend = (ImageView) findViewById(R.id.ivFriend);

        if ((getIntent().getStringExtra("FIRSTNAME") != null) ||
                (getIntent().getStringExtra("LASTNAME") != null) ||
                (getIntent().getStringExtra("GENDER") != null) ||
                (getIntent().getStringExtra("AGE") != null) ||
                (getIntent().getStringExtra("ADDRESS") != null) ||
                (getIntent().getStringExtra("PICTURE") != null))
        {
            firstname = getIntent().getStringExtra("FIRSTNAME");
            lastname = getIntent().getStringExtra("LASTNAME");
            gender = getIntent().getStringExtra("GENDER"); // As index.
            age = getIntent().getStringExtra("AGE");
            address = getIntent().getStringExtra("ADDRESS");
            picture = getIntent().getStringExtra("PICTURE");

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
        // Else, default collection of data from database.
        else
        {
            // Connect to database.
            databaseManager = new DatabaseManager(EditFriendActivity.this);

            // Populate record-array.
            friendRecord = databaseManager.queryFriend(id);

            // Close connection to database.
            databaseManager.close();

            // Output friend-values.
            String uri = "@drawable/" + friendRecord[6];
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            ivFriend.setImageDrawable(res);

            etFirstname.setText(friendRecord[1]);
            etLastname.setText(friendRecord[2]);
            etAge.setText(friendRecord[4]);
            etAddress.setText(friendRecord[5]);

            // Select correct gender.
            if (friendRecord[3].equals("Male"))
                spGender.setSelection(1);
            else if (friendRecord[3].equals("Female"))
                spGender.setSelection(2);
            else
                spGender.setSelection(0);
        }



        btnChangePic = (Button)findViewById(R.id.btnChangePic);

        // Change picture-button click-event.
        btnChangePic.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Redirect to gallery saving inputted data.
                Intent intent = new Intent(getBaseContext(), EditFriendGalleryActivity.class);

                intent.putExtra("FRIEND_ID", Integer.toString(id));
                intent.putExtra("SELECTED_FRIEND", Integer.toString(position));

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


        // Edit-button click-event.
        btnEdit = (Button) findViewById(R.id.btnEdit);

        btnEdit.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                etFirstname = (EditText)findViewById(R.id.etFirstname);
                etLastname = (EditText)findViewById(R.id.etLastname);
                etAge = (EditText)findViewById(R.id.etAge);
                etAddress = (EditText)findViewById(R.id.etAddress);

                // Check gender was selected.
                String gender;

                if (spGender.getSelectedItem().toString().equals("Gender..."))
                    gender = "";
                else
                    gender = spGender.getSelectedItem().toString();

                // Connect to database.
                databaseManager = new DatabaseManager(EditFriendActivity.this);

                // Add input to database.
                bResult = databaseManager.updateFriend (
                        id,
                        etFirstname.getText().toString(),
                        etLastname.getText().toString(),
                        gender,
                        etAge.getText().toString(),
                        etAddress.getText().toString(),
                        picture
                );

                // Close connection to database.
                databaseManager.close();

                // Output result of updating friend as toast-notification.
                if (bResult)
                {
                    Toast.makeText(EditFriendActivity.this, "Friend updated.",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(EditFriendActivity.this, "Error updating friend.",
                            Toast.LENGTH_LONG).show();
                }

                // Redirect to friends record.
                Intent intent = new Intent(getBaseContext(), ViewFriendActivity.class);
                intent.putExtra("SELECTED_FRIEND", Integer.toString(position));
                startActivity(intent);
            }
        });
    }
}
