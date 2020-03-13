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

public class UCurRequest extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("UCurRequest");
        View rootView = getLayoutInflater().inflate(R.layout.current_request_of_user, frameLayout);

        Button CurRequestConfirm = findViewById(R.id.Cur_Request_confirm);
        EditText editTextOriginLocation = findViewById(R.id.editText);
        Spinner modelTochoose = findViewById(R.id.spinner);

        final Intent intent = getIntent();

        CurRequestConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCancelRequest = new Intent(UCurRequest.this, DriverIsOnTheWayActivity.class);
                startActivity(intentCancelRequest);
            }
        });
    }

}
