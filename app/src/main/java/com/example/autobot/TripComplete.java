package com.example.autobot;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TripComplete extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("Rider Mode");
        View rootView = getLayoutInflater().inflate(R.layout.accurate_fair, frameLayout);

        EditText initialLoc = findViewById(R.id.setDestinationLocation);
        EditText destination = findViewById(R.id.setOriginLocation);
        EditText fare = findViewById(R.id.setFare);
        EditText addTip = findViewById(R.id.addTip);
        Button TCConfirm = findViewById(R.id.confirmFee);

        String tips = addTip.getText().toString();
        addTip.setText(tips);

        TCConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //next activity
            }
        });
    }

}
