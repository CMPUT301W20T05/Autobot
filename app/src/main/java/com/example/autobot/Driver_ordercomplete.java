package com.example.autobot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Locale;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class Driver_ordercomplete extends BaseActivity implements EditProfilePage.EditProfilePageListener {

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

        db = LoginActivity.db;

        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        //reID = intent.getStringExtra("reid");

        //get user from firebase
        //user = db.rebuildUser(username);
        request = DriveIsGoing.request;
        //get request from firebase
        //request = db.rebuildRequest(reID, user);
        //request = HomePageActivity.request;
        reID = request.getRequestID();

        setProfile(username,db); // set profile

        TextView Price = findViewById(R.id.setFare);
        DecimalFormat df1 = new DecimalFormat("0.00");
        Price.setText(df1.format(request.getCost()));
        //driver scan qrcode
        Button buttonScan = findViewById(R.id.scan);
        buttonScan.setVisibility(View.VISIBLE);
        Button ConfirmButton = findViewById(R.id.confirmFee);
        ConfirmButton.setVisibility(View.GONE);


        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scan qrcode
                Intent intentScanner = new Intent(getApplicationContext(), Scanner.class);
                finish();
                startActivity(intentScanner);
            }
        });


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
        }
        else if (onNavigationItemSelected(emItem)) { // if the edit profile page is opened, back to main page
            if (fragment != null){
                ft.remove(fragment).commit();
                onResume();
                fragment = null;
                setTitle("Home Page");
            }

        } else if (onNavigationItemSelected(mhItem)){ // if the my request history page is opened, back to main page
            if (fragment != null){
                ft.remove(fragment).commit();
                onResume();
                fragment = null;
                setTitle("Home Page");
            }

        } else if (onNavigationItemSelected(piItem)){ // if the payment information page is opened, back to main page
            if (fragment != null){
                Fragment wallet_fragment = fragmentManager.findFragmentByTag("WALLET_FRAGMENT");
                if (wallet_fragment instanceof Wallet_fragment && wallet_fragment.isVisible()) {
                    fragmentManager.popBackStackImmediate();
                } else {
                    ft.remove(fragment).commit();
                    onResume();
                    fragment = null;
                    setTitle("Home Page");
                }
            }

        } else if (onNavigationItemSelected(sItem)){ // if the settings page is opened, back to main page
            if (fragment != null){
                ft.remove(fragment).commit();
                onResume();
                fragment = null;
                setTitle("Home Page");
            }
        } else if (onNavigationItemSelected(mnItem)){ // if the notifications page is opened, back to main page
            if (fragment != null){
                ft.remove(fragment).commit();
                onResume();
                fragment = null;
                setTitle("Home Page");
            }
        }

    }
}
