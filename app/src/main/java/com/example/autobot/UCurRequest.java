package com.example.autobot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class UCurRequest extends BaseActivity{
    private Button CurRequestConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("UCurRequest");
        View rootView = getLayoutInflater().inflate(R.layout.current_request_of_user, frameLayout);

        CurRequestConfirm = findViewById(R.id.Cur_Request_confirm);
        TextView EstimatedFare = findViewById(R.id.estimatedFare);
        Spinner modelTochoose = findViewById(R.id.spinnerCarModel);

        final Intent intent = getIntent();

        //calculate estimated fare
        //EstimatedFare.setText(...);


        CurRequestConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCancelRequest = new Intent(UCurRequest.this, DriverIsOnTheWayActivity.class);
                startActivity(intentCancelRequest);
            }
        });
    }

}
