package com.example.autobot;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class UCurRequest<mapFragment, fusedLocationProviderClient> extends GoogleMapActivity implements NavigationView.OnNavigationItemSelectedListener, AddPaymentFragement.OnFragmentInteractionListener{
    private DrawerLayout drawer;
    public ListView paymentList;
    public ArrayAdapter<PaymentCard> mAdapter;
    public ArrayList<PaymentCard> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_request_of_user);
        setTitle("UCurRequest");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Button CurRequestConfirm = findViewById(R.id.Cur_Request_confirm);
        EditText editTextOriginLocation = findViewById(R.id.editText);
        Spinner modelTochoose = findViewById(R.id.spinner);

        initMap();
        final Intent intent = getIntent();

        /*CurRequestConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCancelRequest = new Intent(UCurRequest.this, )
            }
        });*/
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
