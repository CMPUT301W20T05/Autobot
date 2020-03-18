package com.example.autobot;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.ParseException;

/**
 * Rider wait for driver to accept their order, they can also choose to cancel order at the same time
 */
public class RiderWaitDriverAcceptRequest extends BaseActivity implements EditProfilePage.EditProfilePageListener{
    private Database db;
    private String username;
    private User user;
    private Request request;
    private String reID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = MainActivity.db;
        super.onCreate(savedInstanceState);
        setTitle("Rider Mode");
        View rootView = getLayoutInflater().inflate(R.layout.rider_wait_accept_request, frameLayout);

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

        try {
            setProfile(username); // set profile
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //when driver arrived, show notification
        sendOnChannel();

        //check if database changed


        Button cancelRequest = findViewById(R.id.cancel_order);
        cancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //pop out dialog
                final AlertDialog.Builder alert = new AlertDialog.Builder(RiderWaitDriverAcceptRequest.this);
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
                                return;
                            }
                        });

                alert.show();

            }
        });

        Button continueButton = findViewById(R.id.ContinueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hardcode for now
                request.resetRequestStatus("Request Accepted");
                //when request condition changes to "accept" go to next activity
                String requestState = request.getStatus();

                if (requestState.equals("Request Accepted")) {
                    //go to next page
                    Intent intentWait = new Intent(RiderWaitDriverAcceptRequest.this, DriverIsOnTheWayActivity.class);
//                    intentWait.putExtra("Username",user.getUsername());
//                    intentWait.putExtra("reid",request.getRequestID());
                    startActivity(intentWait);
                }
                else {
                    Toast.makeText(RiderWaitDriverAcceptRequest.this, "Please wait...", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
