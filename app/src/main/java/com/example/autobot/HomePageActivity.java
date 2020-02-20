package com.example.autobot;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class HomePageActivity extends AppCompatActivity implements OnMapReadyCallback{

    private MapView mapView;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }


        mapView = findViewById(R.id.mapViewHomePage);
        EditText editTextOriginLocation = findViewById(R.id.editTextOriginLocation);
        EditText editTextDestinationLocation = findViewById(R.id.editTextDestinationLocation);
        Button HPConfimrButton = findViewById(R.id.HPconfirm);
        editTextOriginLocation.bringToFront();
        editTextDestinationLocation.bringToFront();
        HPConfimrButton.bringToFront();

        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        HPConfimrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCurRequest = new Intent(HomePageActivity.this, UCurRequest.class);
                startActivity(intentCurRequest);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMinZoomPreference(12);
        LatLng ny = new LatLng(40.7143528, -74.0059731);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }
}
