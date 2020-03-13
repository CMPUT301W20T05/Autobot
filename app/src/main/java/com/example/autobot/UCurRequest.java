package com.example.autobot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.protobuf.StringValue;

import java.text.ParseException;

public class UCurRequest extends BaseActivity implements EditProfilePage.EditProfilePageListener{
    private Button CurRequestConfirm;
    private Database db;
    private String username;
    private User user;
    private Request request;
    private String reID;
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

        db = HomePageActivity.db;
        Intent intent = getIntent();
        //username = intent.getStringExtra("Username");
        //reID = intent.getStringExtra("reid");

        //get user from firebase
        //user = db.rebuildUser(username);
        user = HomePageActivity.user;
        username = user.getUsername();
        //get request from firebase
        //request = db.rebuildRequest(reID, user);
        request = HomePageActivity.request;
        reID = request.getRequestID();

        setProfile(username); // set profile

        //calculate estimated fare
        double estimateFare = request.getEstimateCost();
        EstimatedFare.setText(String.valueOf(estimateFare));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Models, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelTochoose.setAdapter(adapter);

        //modelTochoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /*@Override
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
            }*/
        //});

        //Toast.makeText(UCurRequest,String.valueOf(distance),Toast.LENGTH_SHORT).show();

        //EstimatedFare.setText(String.valueOf(fare));

        CurRequestConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intentCancelRequest = new Intent(UCurRequest.this, RiderWaitDriverAcceptRequest.class);
                Intent intentCancelRequest = new Intent(UCurRequest.this, RiderWaitDriverAcceptRequest.class);
//                intentCancelRequest.putExtra("Username",user.getUsername());
//                intentCancelRequest.putExtra("reid",request.getRequestID());
                startActivity(intentCancelRequest);
            }
        });
    }

    @Override
    public void updateInformation(String FirstName, String LastName, String EmailAddress, String HomeAddress, String emergencyContact) { // change the name on the profile page to the new input name
        name = findViewById(R.id.driver_name);
        String fullName = FirstName + " " + LastName;
        name.setText(fullName);

        User newUser = user;
        newUser.setFirstName(FirstName); // save the changes that made by user
        newUser.setLastName(LastName);
        newUser.setEmailAddress(EmailAddress);
        newUser.setHomeAddress(HomeAddress);
        newUser.setEmergencyContact(emergencyContact);
        db.add_new_user(newUser);

    }
    @Override
    public String getUsername() {
        return username;
    }
}
