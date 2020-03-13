package com.example.autobot;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firestore.v1.StructuredQuery;

import java.io.IOException;
import java.text.ParseException;
import java.util.Locale;

public class OrderComplete extends BaseActivity implements EditProfilePage.EditProfilePageListener {

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

        TextView Destination = findViewById(R.id.setOriginLocation);
        TextView OriginalLoc = findViewById(R.id.setDestinationLocation);
        TextView Price = findViewById(R.id.setFare);
        EditText Tips = findViewById(R.id.textView3);
        Button Confirm = findViewById(R.id.confirmFee);

        LatLng destination = request.getDestination();
        LatLng origin = request.getBeginningLocation();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            String oaddress = request.ReadableAddress(origin, geocoder);
            String daddress = request.ReadableAddress(destination, geocoder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Destination.setText(String.valueOf(destination));

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRateDriver = new Intent(OrderComplete.this, RateDriver.class);
                intentRateDriver.putExtra("Username",username);
                intentRateDriver.putExtra("reid",reID);
                startActivity(intentRateDriver);
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
