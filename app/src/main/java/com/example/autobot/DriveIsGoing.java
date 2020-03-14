package com.example.autobot;


import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class DriveIsGoing extends BaseActivity implements EditProfilePage.EditProfilePageListener {

    protected static Request request;
    private Database db;
    private String username;
    private User user;
    private Button buttonCancelOrder;
    private static final int REQUEST_PHONE_CALL = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Rider Mode");
        View rootView = getLayoutInflater().inflate(R.layout.cancel_ride, frameLayout);

        db = DriverhomeActivity.db;
        user = DriverhomeActivity.user; // get User
        username = user.getUsername(); // get username
        setProfile(username); // set profile
        //Log.d("debug",username);

        User rider = request.getRider();
        TextView reminder = findViewById(R.id.driver_condition);
        reminder.setText("drive safe");
        TextView rider_name = findViewById(R.id.Driver_name);
        rider_name.setText("passenger: "+rider.getUsername());
        Button buttonSeeProfile = findViewById(R.id.see_profile);
        ImageButton imageButtonPhone = findViewById(R.id.phoneButton);
        ImageButton imageButtonEmail = findViewById(R.id.emailButton);
        buttonCancelOrder = findViewById(R.id.cancel_order);

        //for driver to call rider
        //String dphoneNumber = rider.getPhoneNumber();
        String rphoneNumber = "5875576400";

        //make a phone call to driver
        imageButtonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + rphoneNumber));//change the number.
                if (ActivityCompat.checkSelfPermission(DriveIsGoing.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(DriveIsGoing.this, "No permission for calling", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(DriveIsGoing.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                } else {
                    startActivity(callIntent);
                }
            }
        });

        buttonCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pop out dialog
                final AlertDialog.Builder alert = new AlertDialog.Builder(DriveIsGoing.this);
                alert.setTitle("Cancel Order");
                alert.setMessage("Are you sure you wish to cancel current request?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //delete current request
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

        //set the onclick function for button
        buttonCancelOrder.setText("Pick up passenager");
        pick_up_rider();
    }

    @Override
    public void updateInformation(String FirstName, String LastName, String EmailAddress, String HomeAddress, String emergencyContact) { // change the name on the profile page to the new input name
        name = findViewById(R.id.driver_name);
        String fullName = FirstName + " " + LastName;
        name.setText(fullName);

        User newUser = db.rebuildUser(username);
        newUser.setFirstName(FirstName);
        newUser.setLastName(LastName);

        db.add_new_user(newUser);

    }
    @Override
    public String getUsername() {
        return username;
    }


    //reset the button onclick function--------------------------------------
    /*public void accept_order(){
        buttonCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request.UpdateStatus(1);
                //need to add
                //update db
                //change the text view of button after accept order
                buttonCancelOrder.setText("Pick up passenager");
                Log.d("check",request.getStatus());
                //overide onclick
                pick_up_rider();
            }
        });
    }*/

    public void pick_up_rider(){
        buttonCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request.UpdateStatus(2);
                //need to add
                //update db
                //change the text view of button after accept order
                buttonCancelOrder.setText("Finish");
                Log.d("check",request.getStatus());
                //override onlick
                finish_order();
            }
        });
    }


    public void finish_order(){
        buttonCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                request.UpdateStatus(2);
                //need to add
                //update db
                Intent intentOrderComplete = new Intent(DriveIsGoing.this, Driver_ordercomplete.class);
                //intentOrderComplete.putExtra("Username",username);
                startActivity(intentOrderComplete);
            }
        });
    }

}
