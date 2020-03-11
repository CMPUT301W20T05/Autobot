package com.example.autobot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OrderComplete extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("Rider Mode");
        View rootView = getLayoutInflater().inflate(R.layout.accurate_fair, frameLayout);

        EditText Destination = findViewById(R.id.setOriginLocation);
        EditText OriginalLoc = findViewById(R.id.setDestinationLocation);
        EditText Price = findViewById(R.id.setFare);
        EditText Tips = findViewById(R.id.addTip);
        Button Confirm = findViewById(R.id.confirmFee);

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRateDriver = new Intent(OrderComplete.this, RateDriver.class);
                startActivity(intentRateDriver);
            }
        });
    }
}
