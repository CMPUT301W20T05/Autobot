package com.example.autobot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firestore.v1.StructuredQuery;

import java.io.IOException;
import java.text.ParseException;
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

        db = HomePageActivity.db;

        Intent intent = getIntent();
        //username = intent.getStringExtra("Username");
        //reID = intent.getStringExtra("reid");

        //get user from firebase
        //user = db.rebuildUser(username);
        request = DriveIsGoing.request;
        user = request.getDriver();
        username = user.getUsername();
        request.setRequestID("818922938922");
        //get request from firebase
        //request = db.rebuildRequest(reID, user);
        //request = HomePageActivity.request;
        reID = request.getRequestID();

        setProfile(username); // set profile
        TextView Destination = findViewById(R.id.setOriginLocation);
        TextView OriginalLoc = findViewById(R.id.setDestinationLocation);
        TextView Price = findViewById(R.id.setFare);
        EditText Tips = findViewById(R.id.textView3);
        Button Confirm = findViewById(R.id.confirmFee);

        Price.setText("fee: "+String.valueOf(13.34));
        Destination.setText("southgate");
        //get begining location name
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try{
        String oaddress = request.ReadableAddress(request.getBeginningLocation(),geocoder);
        OriginalLoc.setText(oaddress);
        }catch(IOException e){
            e.printStackTrace();
        }


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