package com.example.autobot;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.content.Intent;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.android.SphericalUtil;

import java.util.Arrays;

import static android.os.AsyncTask.execute;

public class HomePageActivity extends BaseActivity {

    LatLng destination;
    LatLng origin;
    Button HPConfirmButton, HPDirectionButton;

    private static final String TAG = "HomePageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("Home page");
        View rootView = getLayoutInflater().inflate(R.layout.home_page, frameLayout);

        HPConfirmButton = findViewById(R.id.buttonConfirmRequest);
        HPConfirmButton.setVisibility(View.GONE);

        // Initialize the AutocompleteSupportFragment.
        //origin
        AutocompleteSupportFragment autocompleteFragmentOrigin = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_origin);
        //destination
        AutocompleteSupportFragment autocompleteFragmentDestination = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_destination);

        // Specify the types of place data to return.
        if (autocompleteFragmentOrigin != null) {
            autocompleteFragmentOrigin.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
            autocompleteFragmentOrigin.setHint("Current Location");
            setAutocompleteSupportFragment(autocompleteFragmentOrigin);
        }

        if (autocompleteFragmentDestination != null) {
            autocompleteFragmentDestination.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
            autocompleteFragmentDestination.setHint("Destination");
            setAutocompleteSupportFragment(autocompleteFragmentDestination);
        }


        HPDirectionButton = (Button) findViewById(R.id.buttonShowDirection);
        HPDirectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                origin = getOrigin(autocompleteFragmentOrigin);
                destination = getDestination(autocompleteFragmentDestination);

                //distance between two locations
                double distance = Math.round(SphericalUtil.computeDistanceBetween(origin, destination));
                //draw route between two locations
                String url = drawRoute(origin, destination);

                HPConfirmButton.setVisibility(View.VISIBLE);
            }
        });

        HPConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //next activity
                Intent intentUCurRequest = new Intent(HomePageActivity.this, UCurRequest.class);
                startActivity(intentUCurRequest);
            }
        });

    }

    public LatLng getOrigin(AutocompleteSupportFragment autocompleteFragmentOrigin){
        Location temp = getCurrentLocation();
        if (temp != null) {
            origin = new LatLng(temp.getLatitude(), temp.getLongitude()); //convert to latlng

            autocompleteFragmentOrigin.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                    origin = place.getLatLng();
                }

                @Override
                public void onError(@NonNull Status status) {
                    // TODO: Handle the error.
                    Log.i(TAG, "An error occurred: " + status);
                }
            });
        }
        return origin;
    }

    public LatLng getDestination(AutocompleteSupportFragment autocompleteFragmentDestination) {
        destination = getSearchedLatLng();
        return destination;
    }

    @Override
    public void updateName(String Name) {
        name = findViewById(R.id.driver_name);
        name.setText(Name);
    }
}
