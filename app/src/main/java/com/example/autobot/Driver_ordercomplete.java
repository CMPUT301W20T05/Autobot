package com.example.autobot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firestore.v1.StructuredQuery;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

        db = LoginActivity.db;

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

        setProfile(username,db); // set profile

        TextView Destination = findViewById(R.id.setOriginLocation);
        TextView OriginalLoc = findViewById(R.id.setDestinationLocation);
        TextView Price = findViewById(R.id.setFare);
        Button Confirm = findViewById(R.id.confirmFee);

        Price.setText("fee: "+String.valueOf(0));
        Destination.setText("southgate");
        //get begining location name
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try{
        String oaddress = request.ReadableAddress(request.getBeginningLocation(),geocoder);
        OriginalLoc.setText(oaddress);
        }catch(IOException e){
            e.printStackTrace();
        }

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenthome = new Intent(Driver_ordercomplete.this, DriverhomeActivity.class);
                startActivity(intenthome);
            }
        });


    }

    @Override
    public void updateInformation(String FirstName, String LastName, String EmailAddress, String HomeAddress, String emergencyContact, Uri imageUri) { // change the name on the profile page to the new input name
        name = findViewById(R.id.driver_name);
        String fullName = FirstName + " " + LastName;
        name.setText(fullName);
        profilePhoto = findViewById(R.id.profile_photo);
        try {
            if (imageUri != Uri.parse("http://www.google.com")) {
                myuri = imageUri;
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                mybitmap = BitmapFactory.decodeStream(imageStream);
                profilePhoto.setImageBitmap(mybitmap);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //Toast.makeText(HomePageActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

        User newUser = user;
        newUser.setFirstName(FirstName); // save the changes that made by user
        newUser.setLastName(LastName);
        newUser.setEmailAddress(EmailAddress);
        newUser.setHomeAddress(HomeAddress);
        newUser.setEmergencyContact(emergencyContact);
        if (!imageUri.toString().equals("http://www.google.com")) {
            newUser.setUri(imageUri.toString());
        }
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
    @Override
    public Uri getUri(){
        return myuri;
    }
}
