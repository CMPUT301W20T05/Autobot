package com.example.autobot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Locale;

/**
 * This is a class for order information activity
 * User can see more details about trip at his page
 */

public class OrderInfo extends BaseActivity {

    private Request request;
    private Database db;
    private String username;
    private User user;
    private String reID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("Rider Mode");
        View rootView = getLayoutInflater().inflate(R.layout.order_info, frameLayout);

        db = HomePageActivity.db;

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

        setProfile(username); // set profile

        Button viewDetail = findViewById(R.id.View_Detail);
        ImageButton emailButton = findViewById(R.id.emailButton);
        ImageButton phoneButton = findViewById(R.id.phoneButton);
        ImageButton backButton = findViewById(R.id.button_back);
        Button cancel = findViewById(R.id.cancel_order);
        TextView originLoc = findViewById(R.id.origin_loc);
        TextView Destination = findViewById(R.id.Destination);
        TextView ApproDist = findViewById(R.id.Appro_distance);
        TextView ApproPrice = findViewById(R.id.Appro_price);

        LatLng destination = request.getDestination();
        LatLng origin = request.getBeginningLocation();
        if (destination != null && origin != null) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                String oaddress = request.ReadableAddress(origin, geocoder);
                String daddress = request.ReadableAddress(destination, geocoder);
                originLoc.setText(oaddress);
                Destination.setText(daddress);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //distance from current to destination
        //ApproDist
        //ApproPrice = EstimateCost()

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDriverIsOnTheWay = new Intent(OrderInfo.this, DriverIsOnTheWayActivity.class);
                startActivity(intentDriverIsOnTheWay);
            }
        });

    }
}
