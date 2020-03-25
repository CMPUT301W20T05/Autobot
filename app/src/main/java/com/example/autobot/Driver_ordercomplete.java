package com.example.autobot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.Locale;


public class Driver_ordercomplete extends BaseActivity implements EditProfilePage.EditProfilePageListener {

    private Database db;
    private String username;
    private User user;
    private Request request;
    private String reID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("Rider Mode");
        View rootView = getLayoutInflater().inflate(R.layout.accurate_fair, frameLayout);

        db = LoginActivity.db;

        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        //reID = intent.getStringExtra("reid");

        //get user from firebase
        //user = db.rebuildUser(username);
        request = DriveIsGoing.request;
        //get request from firebase
        //request = db.rebuildRequest(reID, user);
        //request = HomePageActivity.request;
        reID = request.getRequestID();

        setProfile(username,db); // set profile

        TextView Price = findViewById(R.id.setFare);
        //driver scan qrcode
        Button buttonScan = findViewById(R.id.scan);
        buttonScan.setVisibility(View.VISIBLE);
        Button ConfirmButton = findViewById(R.id.confirmFee);
        ConfirmButton.setVisibility(View.GONE);

        Price.setText(String.valueOf(request.getCost()));

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scan qrcode
                Intent intentScanner = new Intent(getApplicationContext(), Scanner.class);
                finish();
                startActivity(intentScanner);
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
        if (mybitmap != null) profilePhoto.setImageBitmap(mybitmap);

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
