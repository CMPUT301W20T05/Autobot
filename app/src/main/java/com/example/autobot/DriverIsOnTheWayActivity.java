package com.example.autobot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class DriverIsOnTheWayActivity extends BaseActivity implements EditProfilePage.EditProfilePageListener {

    private Request request;
    private Database db;
    private String username;
    private User user;
    private String reID;
    private static final int REQUEST_PHONE_CALL = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Rider Mode");
        View rootView = getLayoutInflater().inflate(R.layout.cancel_ride, frameLayout);

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


        TextView textViewDriverCondition = findViewById(R.id.driver_condition);
        Button buttonSeeProfile = findViewById(R.id.see_profile);
        ImageButton imageButtonPhone = findViewById(R.id.phoneButton);
        ImageButton imageButtonEmail = findViewById(R.id.emailButton);
        Button buttonCancelOrder = findViewById(R.id.cancel_order);

        //use request to get infor
        User driver = request.getDriver();
        User rider = request.getRider();
        //for rider to call driver
        //String rphoneNumber = driver.getPhoneNumber();
        String rphoneNumber = "5875576400";

        //make a phone call to driver
        imageButtonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + rphoneNumber));//change the number.
                if (ActivityCompat.checkSelfPermission(DriverIsOnTheWayActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(DriverIsOnTheWayActivity.this, "No permission for calling", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(DriverIsOnTheWayActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                } else {
                    startActivity(callIntent);
                }
            }
        });

        buttonCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pop out dialog
                final AlertDialog.Builder alert = new AlertDialog.Builder(DriverIsOnTheWayActivity.this);
                alert.setTitle("Cancel Order");
                alert.setMessage("Are you sure you wish to cancel current request?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //delete current request
                                db.CancelRequest(reID);
                                //go back to home page
                                Intent cancelRequest = new Intent(getApplicationContext(), HomePageActivity.class);
//                                cancelRequest.putExtra("Username",user.getUsername());
//                                cancelRequest.putExtra("reid",request.getRequestID());
                                startActivity(cancelRequest);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                alert.show();
            }
        });

        buttonSeeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentOrderInfo = new Intent(DriverIsOnTheWayActivity.this, OrderInfo.class);
                startActivity(intentOrderInfo);
            }
        });

        //when driver arrived, show notification
        sendOnChannel();

        //hardcode for now
        request.resetRequestStatus("Request picked");
        //when request condition changes to "accept" go to next activity
        String requestState = request.getStatus();

        if (requestState.equals("Request picked")) {
            //go to next page
            Intent intentOrderComplete = new Intent(DriverIsOnTheWayActivity.this, OrderComplete.class);
//            intentOrderComplete.putExtra("Username", username);
//            intentOrderComplete.putExtra("reid", reID);
            startActivity(intentOrderComplete);
        }
        else {
            Toast.makeText(DriverIsOnTheWayActivity.this, "Please wait...", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void updateInformation(String FirstName, String LastName, String EmailAddress, String HomeAddress, String emergencyContact, Uri imageUri) { // change the name on the profile page to the new input name
        name = findViewById(R.id.driver_name);
        String fullName = FirstName + " " + LastName;
        name.setText(fullName);
        profilePhoto = findViewById(R.id.profile_photo);
        try {
            InputStream imageStream = getContentResolver().openInputStream(imageUri);
            mybitmap = BitmapFactory.decodeStream(imageStream);
            profilePhoto.setImageBitmap(mybitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(DriverIsOnTheWayActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

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
