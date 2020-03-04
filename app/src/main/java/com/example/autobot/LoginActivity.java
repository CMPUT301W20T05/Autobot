package com.example.autobot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.autobot.ui.Driverhomepage;

public class LoginActivity extends AppCompatActivity {
    Intent next_activity;
    String phone_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setTitle("Login");

        Button buttonLogin = findViewById(R.id.buttonLogin);
        final Intent intent = getIntent();
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new add
                //get the phone number which user input
                Spinner spinner = findViewById(R.id.spinnerPhoneNumberCountry);
                String phone_number_region = spinner.getSelectedItem().toString();
                EditText input =  findViewById(R.id.editTextEmergencyContact);
                phone_number = phone_number_region + input.getText().toString();
                Log.d("number",input.getText().toString());

                //get account info from the firebase(by searching phone number), 0 is driver 1 is rider
                //this is testing
                int user_type = 0;
                //check what role we are playing
                if(user_type == 0){
                    next_activity = new Intent(LoginActivity.this, Driverhomepage.class);
                }
                //otherwise change to rider version
                else{
                    next_activity = new Intent(LoginActivity.this, HomePageActivity.class);
                }
                //Intent intentHomePage = new Intent(LoginActivity.this, HomePageActivity.class);
                //edit
                Intent intentHomePage = next_activity;
                //transfer the phone number to the next activity
                intentHomePage.putExtra("phone number",phone_number);
                startActivity(intentHomePage);
            }
        });
    }
}
