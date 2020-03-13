package com.example.autobot;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class DriveIsGoing extends BaseActivity implements EditProfilePage.EditProfilePageListener {

    protected static Request request;
    private Database db;
    private String username;
    private User user;
    private Button buttonCancelOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Rider Mode");
        View rootView = getLayoutInflater().inflate(R.layout.cancel_ride, frameLayout);

        db = HomePageActivity.db;

        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        setProfile(username); // set profile
        user = db.rebuildUser(username);

        TextView textViewDriverCondition = findViewById(R.id.driver_condition);
        Button buttonSeeProfile = findViewById(R.id.see_profile);
        ImageButton imageButtonPhone = findViewById(R.id.phoneButton);
        ImageButton imageButtonEmail = findViewById(R.id.emailButton);
        buttonCancelOrder = findViewById(R.id.cancel_order);

        //need to connect to firebase to get phone number
        final String phoneNumber = "5875576400";

        //make a phone call to driver
        imageButtonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));//change the number.
                if (ActivityCompat.checkSelfPermission(DriveIsGoing.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);

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
                Intent intentOrderComplete = new Intent(DriveIsGoing.this, OrderComplete.class);
                intentOrderComplete.putExtra("Username",username);
                startActivity(intentOrderComplete);
            }
        });
    }

}
