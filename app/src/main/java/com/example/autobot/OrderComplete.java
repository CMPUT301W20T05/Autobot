package com.example.autobot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.firestore.v1.StructuredQuery;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
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

        db = MainActivity.db;

        Intent intent = getIntent();

        //when driver arrived, show notification
        notificationManager = NotificationManagerCompat.from(this);
        sendOnChannel("Destination arrived. Please check your receipt.");

        user = HomePageActivity.user;
        username = user.getUsername();
        request = HomePageActivity.request;
        reID = request.getRequestID();

        setProfile(username,db); // set profile

//        TextView Destination = findViewById(R.id.setOriginLocation);
//        TextView OriginalLoc = findViewById(R.id.setDestinationLocation);
        TextView textViewFare = findViewById(R.id.setFare);
        Button Confirm = findViewById(R.id.confirmFee);

        DecimalFormat df = new DecimalFormat("0.00");
        double fare = request.getCost();
        textViewFare.setText(df.format(fare));

//        LatLng destination = request.getDestination();
//        LatLng origin = request.getBeginningLocation();
//        if (destination != null && origin != null) {
//            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//            try {
//                String oaddress = request.ReadableAddress(origin, geocoder);
//                String daddress = request.ReadableAddress(destination, geocoder);
//                OriginalLoc.setText(oaddress);
//                Destination.setText(daddress);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentQRCode = new Intent(OrderComplete.this, QRCode.class);
                startActivity(intentQRCode);
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
