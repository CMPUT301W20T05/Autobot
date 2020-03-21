package com.example.autobot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This is a class for checkout
 * User can see brief information about the trip and tip driver at this step
 */

public class TripComplete extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("Rider Mode");
        View rootView = getLayoutInflater().inflate(R.layout.accurate_fair, frameLayout);

        TextView initialLoc = findViewById(R.id.setDestinationLocation);
        TextView destination = findViewById(R.id.setOriginLocation);
        TextView fare = findViewById(R.id.setFare);
        EditText addTip = findViewById(R.id.addTip);
        Button TCConfirm = findViewById(R.id.confirmFee);

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
