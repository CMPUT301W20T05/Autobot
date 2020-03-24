package com.example.autobot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.maps.android.SphericalUtil;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.android.volley.VolleyLog.TAG;
import static com.example.autobot.App.CHANNEL_1_ID;

public class DriverIsOnTheWayActivity extends BaseActivity implements EditProfilePage.EditProfilePageListener {

    private Request request;
    private Database db;
    private String username;
    private User user;
    private String reID;
    private static final int REQUEST_PHONE_CALL = 101;
    private User rider;
    private User driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Rider Mode");
        View rootView = getLayoutInflater().inflate(R.layout.cancel_ride, frameLayout);

        db = MainActivity.db;

        Intent intent = getIntent();

        //when driver arrived, show notification
        notificationManager = NotificationManagerCompat.from(this);
        sendOnChannel("Driver has accepted your request. Please wait for picking up.");

        //get user from firebase
        //user = db.rebuildUser(username);
        user = LoginActivity.user;
        username = user.getUsername();
        //get request from firebase
        //request = db.rebuildRequest(reID, user);
        request = HomePageActivity.request;
        reID = request.getRequestID();

        //use request to get infor
        driver = db.rebuildUser("jc");
        request.setDriver(driver);
        driver = request.getDriver();
        rider = request.getRider();

        setProfile(username,db); // set profile

        //rider accepted
        final BottomSheetDialog riderAcceptedDialog = new BottomSheetDialog(DriverIsOnTheWayActivity.this);
        riderAcceptedDialog.setContentView(R.layout.rider_accept);
        riderAcceptedDialog.setCancelable(false);
        ImageView driverAvatar = riderAcceptedDialog.findViewById(R.id.imageViewAvatar);
        TextView driverName = riderAcceptedDialog.findViewById(R.id.Driver_name);
        TextView driverRate = riderAcceptedDialog.findViewById(R.id.driverRate);

        driverName.setText(String.format("%s%s", driver.getLastName(), driver.getFirstName()));

        DecimalFormat df = new DecimalFormat("0.0");
        float goodRate = Float.parseFloat(driver.getGoodRate());
        float badRate = Float.parseFloat(driver.getBadRate());
        float rate = goodRate / (goodRate + badRate) * 10;
        driverRate.setText(df.format(rate));

        Button accept = riderAcceptedDialog.findViewById(R.id.acceptDriver);
        Button reject = riderAcceptedDialog.findViewById(R.id.rejectDriver);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request.resetRequestStatus("Rider Accepted",db);
                db.ChangeRequestStatus(request);
                //mark driver and rider location in map
                LatLng driverCurrent = driver.getCurrentLocation();
                LatLng riderCurrent = rider.getCurrentLocation();
                drawRouteWithAvatar(driverCurrent, riderCurrent, driver, rider);
                riderAcceptedDialog.dismiss();
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request.resetRequestStatus("Cancel",db);
                db.ChangeRequestStatus(request);
                riderAcceptedDialog.dismiss();
                //return to homepage
                Intent finishRequest = new Intent(getApplicationContext(), HomePageActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(finishRequest);
                overridePendingTransition(0, 0);
            }
        });
        riderAcceptedDialog.show();

        TextView textViewDriverCondition = findViewById(R.id.driver_condition);
        //Button buttonSeeProfile = findViewById(R.id.see_profile);
        ImageView imageViewAvatar = findViewById(R.id.imageViewAvatar);
        TextView textViewDriverRate = findViewById(R.id.driverRate);
        TextView textViewDriverName = findViewById(R.id.Driver_name);
        ImageButton imageButtonPhone = findViewById(R.id.phoneButton);
        ImageButton imageButtonEmail = findViewById(R.id.emailButton);
        TextView textViewEstimateTime = findViewById(R.id.EstimatedTime);
        TextView textViewEstimateDist = findViewById(R.id.EstimatedDist);
        Button buttonCancelOrder = findViewById(R.id.cancel_order);
        Button buttonComplete = findViewById(R.id.complete);

        //get location
        LatLng destination = request.getDestination();
        LatLng origin = request.getBeginningLocation();
        //distance between two locations
        double distance = Math.round(SphericalUtil.computeDistanceBetween(origin, destination));
        DecimalFormat df1 = new DecimalFormat("0.00");
        textViewEstimateDist.setText(df1.format(distance));
        //calculate time
        double time = distance / 1008.00 + 5.00;
        textViewEstimateTime.setText(df1.format(time));
        //set driver infor
        //imageViewAvatar.setBackgroundResource();
        textViewDriverName.setText(String.format("%s%s", driver.getLastName(), driver.getFirstName()));
        //good rate infor
        textViewDriverRate.setText(df.format(rate));

        //for rider to call driver
        String rphoneNumber = driver.getPhoneNumber();
        //String rphoneNumber = "5875576400";

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

        textViewDriverName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view = LayoutInflater.from(DriverIsOnTheWayActivity.this).inflate(R.layout.profile_viewer, null);

                TextView fname = view.findViewById(R.id.FirstName);
                TextView lname = view.findViewById(R.id.LastName);
                TextView pnumber = view.findViewById(R.id.PhoneNumber);
                TextView email = view.findViewById(R.id.EmailAddress);
                //should be set as driver's infor
                fname.setText(driver.getFirstName());
                lname.setText(driver.getLastName());
                pnumber.setText(driver.getPhoneNumber());
                email.setText(driver.getEmailAddress());

                final AlertDialog.Builder alert = new AlertDialog.Builder(DriverIsOnTheWayActivity.this);
                alert.setView(view)
                        .setTitle("Details")
                        .setNegativeButton("Close",null);
                alert.show();
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
                                //cancel current request
                                //db.CancelRequest(reID);
                                request.resetRequestStatus("Cancel",db);
                                db.ChangeRequestStatus(request);
                                //return to homepage
                                Intent finishRequest = new Intent(getApplicationContext(), HomePageActivity.class);
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(finishRequest);
                                overridePendingTransition(0, 0);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        });

                alert.show();
            }
        });

        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //arrive destination
                request.resetRequestStatus("Trip Completed",db);
                db.ChangeRequestStatus(request);
                Intent intentComplete = new Intent(DriverIsOnTheWayActivity.this, OrderComplete.class);
                finish();
                startActivity(intentComplete);
            }
        });

        //picked up rider
        db.NotifyStatusChangeEditText(reID, "Rider picked", textViewDriverCondition, "Driving to destination...");



        //rider confirm completion
        db.NotifyStatusChangeButton(reID, "Rider picked", buttonCancelOrder, false);
        db.NotifyStatusChangeButton(reID, "Rider picked", buttonComplete, true);


//        Intent intentComplete = new Intent(DriverIsOnTheWayActivity.this, OrderComplete.class);
//        db.NotifyStatusChange(reID, "Trip Completed", DriverIsOnTheWayActivity.this, intentComplete);

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
