package com.example.autobot;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.android.volley.VolleyLog.TAG;

/**
 * This is a class for RateDriver activity
 * User can rate driver at this step
 */

public class RateDriver extends BaseActivity {

    private Database db;
    private String username;
    private User user;
    private Request request;
    private String reID;
    private Boolean Good = false;

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

        User driver = DriverIsOnTheWayActivity.driver;
        //set driver infor
        setAvatar(driver, avatar);
        DriverName.setText(String.format("%s%s", driver.getLastName(), driver.getFirstName()));

        String goodrate = driver.getGoodRate();
        String badrate = driver.getBadRate();
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
                driver.resetGoodrate(String.valueOf(good),db);
                Intent finishRequest = new Intent(RateDriver.this, HomePageActivity.class);
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
                    driver.resetGoodrate(String.valueOf(good),db);
                }
                else {
                    int bad = Integer.parseInt(badrate);
                    bad += 1;
                    driver.resetBadrate(String.valueOf(bad), db);
                }
                Intent finishRequest = new Intent(getApplicationContext(), HomePageActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(finishRequest);
                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public void onBackPressed() {}
}
