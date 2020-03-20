package com.example.autobot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

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

        db = MainActivity.db;

        Intent intent = getIntent();
        //username = intent.getStringExtra("Username");
        //reID = intent.getStringExtra("reid");

        //get user from firebase
        //user = db.rebuildUser(username);
        user = LoginActivity.user;
        username = user.getUsername();
        //get request from firebase
        //request = db.rebuildRequest(reID, user);
        request = HomePageActivity.request;
        reID = request.getRequestID();

        try {
            setProfile(username); // set profile
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView textViewDriverCondition = findViewById(R.id.driver_condition);
        Button buttonSeeProfile = findViewById(R.id.see_profile);
        ImageButton imageButtonPhone = findViewById(R.id.phoneButton);
        ImageButton imageButtonEmail = findViewById(R.id.emailButton);
        TextView textViewEstimateTime = findViewById(R.id.EstimatedTime);
        TextView textViewEstimateDist = findViewById(R.id.EstimatedDist);
        Button buttonCancelOrder = findViewById(R.id.cancel_order);

        //set location for dialog
        LatLng destination = request.getDestination();
        LatLng origin = request.getBeginningLocation();
        //distance between two locations
        double distance = Math.round(SphericalUtil.computeDistanceBetween(origin, destination));
        DecimalFormat df = new DecimalFormat("0.00");
        textViewEstimateDist.setText(df.format(distance));
        //calculate time
        double time = distance / 1008.00;
        textViewEstimateTime.setText(df.format(time));

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

        //driver都来接了应该不可以取消单了吧
//        buttonCancelOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //pop out dialog
//                final AlertDialog.Builder alert = new AlertDialog.Builder(DriverIsOnTheWayActivity.this);
//                alert.setTitle("Cancel Order");
//                alert.setMessage("Are you sure you wish to cancel current request?")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //delete current request
//                                db.CancelRequest(reID);
//                                //go back to home page
//                                Intent cancelRequest = new Intent(getApplicationContext(), HomePageActivity.class);
////                                cancelRequest.putExtra("Username",user.getUsername());
////                                cancelRequest.putExtra("reid",request.getRequestID());
//                                startActivity(cancelRequest);
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        });
//
//                alert.show();
//            }
//        });

        buttonSeeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view = LayoutInflater.from(DriverIsOnTheWayActivity.this).inflate(R.layout.profile_viewer, null);

                TextView fname = view.findViewById(R.id.FirstName);
                TextView lname = view.findViewById(R.id.LastName);
                TextView pnumber = view.findViewById(R.id.PhoneNumber);
                TextView email = view.findViewById(R.id.EmailAddress);
                //should be set as driver's infor
                fname.setText(user.getFirstName());
                lname.setText(user.getLastName());
                pnumber.setText(user.getPhoneNumber());
                email.setText(user.getEmailAddress());

                final AlertDialog.Builder alert = new AlertDialog.Builder(DriverIsOnTheWayActivity.this);
                alert.setView(view)
                        .setTitle("Details")
                        .setNegativeButton("Close",null);
                alert.show();
            }
        });

        //when driver arrived, show notification
        sendOnChannel();

        //wait driver to accept
        Intent intentComplete = new Intent(DriverIsOnTheWayActivity.this, OrderComplete.class);
        db.NotifyStatusChange(reID, "Request picked", DriverIsOnTheWayActivity.this, intentComplete);

    }

    @Override
    public void updateInformation(String FirstName, String LastName, String EmailAddress, String HomeAddress, String emergencyContact) { // change the name on the profile page to the new input name
        name = findViewById(R.id.driver_name);
        String fullName = FirstName + " " + LastName;
        name.setText(fullName);

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
}
