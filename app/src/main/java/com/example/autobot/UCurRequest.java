package com.example.autobot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.protobuf.StringValue;

public class UCurRequest extends BaseActivity implements EditProfilePage.EditProfilePageListener{
    private Button CurRequestConfirm;
    private Database db;
    private String username;
    private User user;
    private Request request;
    private String reID;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("UCurRequest");
        View rootView = getLayoutInflater().inflate(R.layout.current_request_of_user, frameLayout);

        CurRequestConfirm = findViewById(R.id.Cur_Request_confirm);
        TextView EstimatedFare = findViewById(R.id.estimatedFare);
        Spinner modelTochoose = findViewById(R.id.spinnerCarModel);

        db = HomePageActivity.db;
        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        reID = intent.getStringExtra("reid");

        setProfile(username); // set profile
        //get user from firebase
        user = db.rebuildUser(username);
        //get request from firebase
        request = db.rebuildRequest(reID, user);

        //calculate estimated fare
        double estimateFare = request.getEstimateCost();
        EstimatedFare.setText(String.valueOf(estimateFare));


        CurRequestConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCancelRequest = new Intent(UCurRequest.this, DriverIsOnTheWayActivity.class);
                intentCancelRequest.putExtra("Username",username);
                intentCancelRequest.putExtra("reid",reID);
                startActivity(intentCancelRequest);
            }
        });
    }

    @Override
    public void updateInformation(String FirstName, String LastName, String EmailAddress, String HomeAddress, String emergencyContact) { // change the name on the profile page to the new input name
        name = findViewById(R.id.driver_name);
        String fullName = FirstName + " " + LastName;
        name.setText(fullName);

        User newUser = db.rebuildUser(username);
        newUser.setFirstName(FirstName);
        newUser.setLastName(LastName);

        db.add_new_user(newUser);

    }
}
