package com.example.autobot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        final Intent intent = getIntent();
        TextView textViewBackToLogin = findViewById(R.id.textViewGoToSignUp);
        final EditText editTextPhoneNumber = findViewById(R.id.accountPhoneNumber);
        final EditText editTextUserName = findViewById(R.id.accountUserName);
        Button signUpButton = findViewById(R.id.signUpButton);
        RadioButton radioButtonDriver = findViewById(R.id.radioButtonDriver);
        RadioButton radioButtonRider = findViewById(R.id.radioButtonRider);
        CheckBox checkBoxPolicy = findViewById(R.id.checkBoxPolicy);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSetPassword = new Intent(SignUpActivity.this, SetPasswordActivity.class);
                startActivity(intentSetPassword);
                User user= new User();
                user.setUsername(editTextUserName.getText().toString());
                user.setEmailAddress(editTextPhoneNumber.getText().toString());
                Database db = new Database();
                db.add_new_user(user);



        }
        });

        textViewBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intentLogin);
            }
        });

    }

}
