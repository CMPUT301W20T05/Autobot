package com.example.autobot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UCurRequest extends BaseActivity implements AdapterView.OnItemSelectedListener {

    static double distance;
    private String model;
    public double fare;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("UCurRequest");
        View rootView = getLayoutInflater().inflate(R.layout.current_request_of_user, frameLayout);

        Button CurRequestConfirm = findViewById(R.id.Cur_Request_confirm);
        TextView EstimatedFare = findViewById(R.id.estimatedFare);
        Spinner modelTochoose = (Spinner) findViewById(R.id.spinnerCarModel);

        final Intent intent = getIntent();

        //calculate estimated fare
        //EstimatedFare.setText(...);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Models, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelTochoose.setAdapter(adapter);
        modelTochoose.setOnItemSelectedListener(this);

        if (model.equals("Normal")){
            fare = 5 + 2 * distance;
        }else if(model.equals("Pleasure")){
            fare = 8+ 2 * distance;
        }else{
            fare = 10 + 2 * distance;
        }
        EstimatedFare.setText(String.valueOf(fare));

        CurRequestConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCancelRequest = new Intent(UCurRequest.this, DriverIsOnTheWayActivity.class);
                startActivity(intentCancelRequest);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        model = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
