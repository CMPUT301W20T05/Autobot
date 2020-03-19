package com.example.autobot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

        db = LoginActivity.db;

        Intent intent = getIntent();
        //username = intent.getStringExtra("Username");
        //reID = intent.getStringExtra("reid");

        //get user from firebase
        //user = db.rebuildUser(username);
        user = HomePageActivity.user;
        username = user.getUsername();
        //get request from firebase
        //request = db.rebuildRequest(reID, user);
        request = HomePageActivity.request;
        reID = request.getRequestID();

        setProfile(username,db); // set profile

        findViewById(R.id.myMap).setVisibility(View.GONE);

        ImageView Selfie = findViewById(R.id.DriverAvatar);
        TextView Driver = findViewById(R.id.Driver_name);
        Button Profile = findViewById(R.id.see_profile);
        EditText Comment = findViewById(R.id.comment);
        Button Skip = findViewById(R.id.skip);
        Button Confirm = findViewById(R.id.confirmFee);
        ImageView goodButton = findViewById(R.id.good);
        ImageView badButton = findViewById(R.id.bad);

        goodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        badButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
                //go back to home page
                Intent finishRequest = new Intent(getApplicationContext(), HomePageActivity.class);
//                                finishRequest.putExtra("Username",user.getUsername());
//                                finishRequest.putExtra("reid",request.getRequestID());
                startActivity(finishRequest);
            }
        });

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go back to home page
                Intent finishRequest = new Intent(getApplicationContext(), HomePageActivity.class);
//                                finishRequest.putExtra("Username",user.getUsername());
//                                finishRequest.putExtra("reid",request.getRequestID());
                startActivity(finishRequest);
            }
        });
    }

    @Override
    public void updateInformation(String FirstName, String LastName, String EmailAddress, String HomeAddress, String emergencyContact, Bitmap bitmap) { // change the name on the profile page to the new input name
        name = findViewById(R.id.driver_name);
        String fullName = FirstName + " " + LastName;
        name.setText(fullName);
        profilePhoto = findViewById(R.id.profile_photo);
        mybitmap = bitmap;
        profilePhoto.setImageBitmap(mybitmap);

        User newUser = user;
        newUser.setFirstName(FirstName); // save the changes that made by user
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
    @Override
    public Bitmap getBitmap(){
        return mybitmap;
    }
}
