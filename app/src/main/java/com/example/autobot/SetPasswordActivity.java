package com.example.autobot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SetPasswordActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_password_activity);

        Button buttonConfirmPassword = findViewById(R.id.buttonConfirmPassword);

        final Intent intent = getIntent();

        buttonConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHomePage = new Intent(SetPasswordActivity.this, HomePageActivity.class);
                startActivity(intentHomePage);
            }
        });
    }


}
