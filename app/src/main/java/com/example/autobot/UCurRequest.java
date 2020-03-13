package com.example.autobot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class UCurRequest extends BaseActivity {

    static double distance;
    public double fare;
    public String model;

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

        modelTochoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                model = parent.getItemAtPosition(position).toString();
                adapter.notifyDataSetChanged();
                if ("Normal".equals(model)){
                    fare = 5 + 2 * 10;
                }else if("Pleasure".equals(model)){
                    fare = 8+ 2 * 10;
                }else{
                    fare = 10 + 2 * 10;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                model = "Normal";
                fare = 5 + 2 * 10;
            }
        });

        //Toast.makeText(UCurRequest,String.valueOf(distance),Toast.LENGTH_SHORT).show();

        EstimatedFare.setText(String.valueOf(fare));

        CurRequestConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCancelRequest = new Intent(UCurRequest.this, DriverIsOnTheWayActivity.class);
                startActivity(intentCancelRequest);
            }
        });
    }
}
