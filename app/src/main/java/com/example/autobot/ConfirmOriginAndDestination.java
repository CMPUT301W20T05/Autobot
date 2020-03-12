package com.example.autobot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextClassifierEvent;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.autobot.BaseActivity;
import com.example.autobot.HomePageActivity;
import com.example.autobot.R;
import com.example.autobot.Request;
import com.example.autobot.UCurRequest;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class ConfirmOriginAndDestination extends BaseActivity {

    LatLng destination;
    LatLng origin;

    private static final String TAG = "ConfirmOriginAndDestinationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = getLayoutInflater().inflate(R.layout.home_page, frameLayout);

        final Intent intent = getIntent();

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
            setAutocompleteSupportFragment(autocompleteFragmentOrigin);
            Location temp = getCurrentLocation();
            origin = new LatLng(temp.getLatitude(), temp.getLongitude());

            autocompleteFragmentOrigin.setText("Current Location");

            autocompleteFragmentOrigin.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @SuppressLint("LongLogTag")
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                    origin = place.getLatLng();
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onError(@NonNull Status status) {
                    // TODO: Handle the error.
                    Log.i(TAG, "An error occurred: " + status);
                }
            });
        }

        if (autocompleteFragmentDestination != null) {
            autocompleteFragmentDestination.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
            setAutocompleteSupportFragment(autocompleteFragmentDestination);

            destination = getSearchedLatLng();
        }


        Button HPConfirmButton = (Button) findViewById(R.id.buttonConfirmLocation);
        HPConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Request request = new Request();
                finish();
            }
        });

    }


}
