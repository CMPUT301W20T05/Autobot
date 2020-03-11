package com.example.autobot;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.content.Intent;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class HomePageActivity extends BaseActivity implements EditProfilePage.EditProfilePageListener {
    public TextView name;

    LatLng destination;
    LatLng origin;

    private Button HPConfirmButton;
    private TextView textViewWhereToGo;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("Home page");
        View rootView = getLayoutInflater().inflate(R.layout.activity_request_destination, frameLayout);

        // Initialize the AutocompleteSupportFragment.
//        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
//                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//        // Specify the types of place data to return.
//        if (autocompleteFragment != null) {
//            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
//            setAutocompleteSupportFragment(autocompleteFragment);
//
//            destination = getSearchedLatLng();
//        }

        textViewWhereToGo = findViewById(R.id.textViewWhereToGo);
        textViewWhereToGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentOriginAndDestination = new Intent(HomePageActivity.this, ConfirmOriginAndDestination.class);
                startActivity(intentOriginAndDestination);
            }
        });

//        HPConfirmButton = (Button) findViewById(R.id.HP_confirm);
//
//        HPConfirmButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentCurRequest = new Intent(HomePageActivity.this, UCurRequest.class);
//                startActivity(intentCurRequest);
//            }
//        });
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