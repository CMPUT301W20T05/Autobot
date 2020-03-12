package com.example.autobot;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class TripComplete extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("Rider Mode");
        View rootView = getLayoutInflater().inflate(R.layout.accurate_fair, frameLayout);

        EditText initialLoc = findViewById(R.id.textView4);
        EditText destination = findViewById(R.id.textView3);
        EditText fare = findViewById(R.id.textView6);
        EditText addTip = findViewById(R.id.textView7);
        Button TCConfirm = findViewById(R.id.button2);

        String tips = addTip.getText().toString();
        addTip.setText(tips);

        TCConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRateDriver = new Intent(TripComplete.this, RateDriver.class);
                startActivity(intentRateDriver);
            }
        });
    }

}
