package com.example.autobot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.ParseException;

/**
 * This is a class for RateDriver activity
 * User can rate driver at this step
 */

public class RateDriver extends BaseActivity implements EditProfilePage.EditProfilePageListener {

    private Database db;
    private String username;
    private User user;
    private Request request;
    private String reID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Rider Mode");
        View rootView = getLayoutInflater().inflate(R.layout.rate_driver, frameLayout);

        db = HomePageActivity.db;

        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        reID = intent.getStringExtra("reid");

        setProfile(username); // set profile
        //get user from firebase
        user = db.rebuildUser(username);
        //get request from firebase
        try {
            request = db.rebuildRequest(reID, user);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        findViewById(R.id.myMap).setVisibility(View.GONE);

        ImageView Selfie = findViewById(R.id.DriverAvatar);
        TextView Driver = findViewById(R.id.Driver_name);
        Button Profile = findViewById(R.id.see_profile);
        EditText Comment = findViewById(R.id.comment);
        Button Skip = findViewById(R.id.skip);
        Button Confirm = findViewById(R.id.confirmFee);

        Selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //see profile
            }
        });

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //see profile
            }
        });

        String comment = Comment.getText().toString();
        Comment.setText(comment);

        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //next activity
                Intent intentHomePage = new Intent(RateDriver.this, HomePageActivity.class);
                startActivity(intentHomePage);
            }
        });

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //next activity
                Intent intentHomePage = new Intent(RateDriver.this, HomePageActivity.class);
                startActivity(intentHomePage);
            }
        });
    }

    @Override
    public void updateInformation(String FirstName, String LastName, String EmailAddress, String HomeAddress, String emergencyContact) { // change the name on the profile page to the new input name
        name = findViewById(R.id.driver_name);
        String fullName = FirstName + " " + LastName;
        name.setText(fullName);

        User newUser = db.rebuildUser(username);
        newUser.setFirstName(FirstName);
        newUser.setLastName(LastName);
        newUser.setEmailAddress(EmailAddress);
        newUser.setHomeAddress(HomeAddress);
        newUser.setEmergencyContact(emergencyContact);

        db.add_new_user(newUser);

    }
    @Override
    public String getUsername() {
        return username;
    }
}
