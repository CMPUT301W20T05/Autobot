package com.example.autobot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class OrderComplete extends BaseActivity implements EditProfilePage.EditProfilePageListener {

    private Database db;
    private String username;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("Rider Mode");
        View rootView = getLayoutInflater().inflate(R.layout.accurate_fair, frameLayout);

        db = new Database();

        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        setProfile(username); // set profile
        user = db.rebuildUser(username);

        TextView Destination = findViewById(R.id.setOriginLocation);
        TextView OriginalLoc = findViewById(R.id.setDestinationLocation);
        TextView Price = findViewById(R.id.setFare);
        Spinner Tips = findViewById(R.id.addTip);
        Button Confirm = findViewById(R.id.confirmFee);

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRateDriver = new Intent(OrderComplete.this, RateDriver.class);
                intentRateDriver.putExtra("Username",username);
                startActivity(intentRateDriver);
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
