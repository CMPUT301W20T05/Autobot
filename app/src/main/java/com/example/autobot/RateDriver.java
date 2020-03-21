package com.example.autobot;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * This is a class for RateDriver activity
 * User can rate driver at this step
 */

public class RateDriver extends BaseActivity implements EditProfilePage.EditProfilePageListener {

    private Database db;
    private String username;
    private User user;
    private Request request;
    private String reID;
    private String goodrate;
    private String badrate;
    private Boolean Good;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Rider Mode");
        View rootView = getLayoutInflater().inflate(R.layout.rate_driver, frameLayout);

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

        findViewById(R.id.myMap).setVisibility(View.GONE);

        ImageView avatar = findViewById(R.id.DriverAvatar);
        TextView DriverName = findViewById(R.id.Driver_name);
        Button Profile = findViewById(R.id.see_profile);
        EditText Comment = findViewById(R.id.comment);
        Button Skip = findViewById(R.id.skip);
        Button Confirm = findViewById(R.id.confirmFee);
        LikeButton thumb = findViewById(R.id.thumb);

        User driver = request.getDriver();
        User rider = request.getRider();
        //set driver infor
        //imageViewAvatar.setBackgroundResource();
        DriverName.setText(String.format("%s%s", driver.getLastName(), driver.getFirstName()));

        goodrate = driver.getGoodRate();
        badrate = driver.getBadRate();
        thumb.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Good = true;
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Good = false;
            }
        });

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //see profile
                view = LayoutInflater.from(RateDriver.this).inflate(R.layout.profile_viewer, null);

                TextView fname = view.findViewById(R.id.FirstName);
                TextView lname = view.findViewById(R.id.LastName);
                TextView pnumber = view.findViewById(R.id.PhoneNumber);
                TextView email = view.findViewById(R.id.EmailAddress);
                //should be set as driver's infor
                fname.setText(driver.getFirstName());
                lname.setText(driver.getLastName());
                pnumber.setText(driver.getPhoneNumber());
                email.setText(driver.getEmailAddress());

                final AlertDialog.Builder alert = new AlertDialog.Builder(RateDriver.this);
                alert.setView(view)
                        .setTitle("Details")
                        .setNegativeButton("Close",null);
                alert.show();
            }
        });

        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go back to home page
                int good = Integer.parseInt(goodrate);
                good += 1;
                driver.setGoodRate(String.valueOf(good));
                db.add_new_user(driver);
                Intent finishRequest = new Intent(getApplicationContext(), HomePageActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(finishRequest);
                overridePendingTransition(0, 0);
            }
        });

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go back to home page
                if (Good == true){
                    int good = Integer.parseInt(goodrate);
                    good += 1;
                    driver.setGoodRate(String.valueOf(good));
                }
                else {
                    int bad = Integer.parseInt(badrate);
                    bad += 1;
                    driver.setBadRate(String.valueOf(bad));
                }
                db.add_new_user(driver);
                Intent finishRequest = new Intent(getApplicationContext(), HomePageActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(finishRequest);
                overridePendingTransition(0, 0);
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
