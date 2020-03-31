package com.example.autobot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firestore.v1.StructuredQuery;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderComplete extends BaseActivity {

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
        boolean value1 = true; // default value if no value was found
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("isChecked", 0);
        value1 = sharedPreferences.getBoolean("isChecked1", value1); // retrieve the value of your key
        if (value1){
            notificationManager = NotificationManagerCompat.from(this);
            sendOnChannel("Destination arrived. Please check your receipt.");
        }

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
                Date date = new Date(System.currentTimeMillis());
                request.resetArriveTime(date, db);
                Offline.clear_request(LoginActivity.sharedPreferences);
                Intent intentQRCode = new Intent(OrderComplete.this, QRCode.class);
                finish();
                startActivity(intentQRCode);
            }
        });

        db.db1.collection("Request").document(reID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    request.reset_Request_Status((String) document.getString("RequestStatus"));
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        request.setAcceptTime(formatter.parse((String) document.getString("AcceptTime")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    LoginActivity.save_request(request);
                }
            }
        });
    }

    @Override
    public void onBackPressed(){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();  // setup fragmentTransaction

        navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu(); // get the menu
        MenuItem emItem = menu.findItem(R.id.edit_profile); // item edit profile
        MenuItem mhItem = menu.findItem(R.id.my_request_history); // item my request history
        MenuItem mnItem = menu.findItem(R.id.my_notification); // item my notification
        MenuItem piItem = menu.findItem(R.id.payment_information); // item payment information
        MenuItem sItem = menu.findItem(R.id.settings); // item settings
        MenuItem lItem = menu.findItem(R.id.log_out); // item log out

        //  store the menu to var when creating options menu

        if (drawer.isDrawerOpen(GravityCompat.START)) {  // if the drawer is opened, when a item is clicked, close the drawer
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragment == null){}
        else {
            ft.remove(fragment).commit();
            fragment = null;
            setTitle("Rider Mode");
            onResume();
            frameLayout.setVisibility(View.VISIBLE);
            frameLayout.invalidate();
        }

    }
}
