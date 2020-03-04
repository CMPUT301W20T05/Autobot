package com.example.autobot;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;

public class HomePageActivity extends BaseActivity implements EditProfilePage.EditProfilePageListener {
    public TextView name;

    private Button HPConfirmButton;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("Home page");
        View rootView = getLayoutInflater().inflate(R.layout.accurate_fair, frameLayout);

        /*HPConfirmButton = (Button) findViewById(R.id.HP_confirm);

        HPConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCurRequest = new Intent(HomePageActivity.this, UCurRequest.class);
                startActivity(intentCurRequest);
            }
        });*/
    }

    @Override
    public void updateName(String Name) {
        name = findViewById(R.id.driver_name);
        name.setText(Name);
    }
}

//        EditText editTextOrigin = findViewById(R.id.editTextOriginLocation);
//        EditText editTextDestination = findViewById(R.id.editTextDestinationLocation);

        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap);
//        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBarOriginLocation);
//
//        initMap();

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            mapView = mapFragment.getView();
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();*/