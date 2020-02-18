package com.example.autobot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("Registered")
public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        final Intent intent = getIntent();
        EditText editTextPhoneNumber = findViewById(R.id.accountPhoneNumber);
        EditText editTextUserName = findViewById(R.id.accountUserName);
        Button signUpButton = findViewById(R.id.signUpButton);
        RadioButton radioButtonDriver = findViewById(R.id.radioButtonDriver);
        RadioButton radioButtonRider = findViewById(R.id.radioButtonRider);
        CheckBox checkBoxPolicy = findViewById(R.id.checkBoxPolicy);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSetPassword = new Intent(SignUpActivity.this, SetPasswordActivity.class);
                startActivity(intentSetPassword);
        }
        });

    }

}
