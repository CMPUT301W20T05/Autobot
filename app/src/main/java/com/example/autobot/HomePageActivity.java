package com.example.autobot;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import com.google.android.gms.maps.SupportMapFragment;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;

public class HomePageActivity extends GoogleMapActivity implements NavigationView.OnNavigationItemSelectedListener, AddPaymentFragement.OnFragmentInteractionListener{
    private DrawerLayout drawer;
    public ListView paymentList;
    public ArrayAdapter<PaymentCard> mAdapter;
    public ArrayList<PaymentCard> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        setTitle("Home Page");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        EditText editTextOrigin = findViewById(R.id.editTextOriginLocation);
//        EditText editTextDestination = findViewById(R.id.editTextDestinationLocation);

        materialSearchBar = findViewById(R.id.searchBarOriginLocation);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            mapView = mapFragment.getView();
        }
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        fetchLocation();

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()) {
            case R.id.my_request_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RequestHistoryFragment()).commit();
                break;
            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;
            case R.id.payment_information:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PaymentInformationFragment()).commit();
                break;
            case R.id.log_out:
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);startActivity(intent);
                break;
            case R.id.edit_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EditProfilePage()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void onOkPressed(PaymentCard newPayment) {
        mDataList.add(newPayment);
        mAdapter.notifyDataSetChanged();
    }

}

