package com.example.autobot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    private RadioButton radioButtonDriver;
    private RadioButton radioButtonRider;
    private CheckBox checkBoxPolicy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        final Intent intent = getIntent();
        TextView textViewBackToLogin = findViewById(R.id.textViewGoToSignUp);
        final EditText editTextPhoneNumber = findViewById(R.id.accountPhoneNumber);
        final EditText editTextUserName = findViewById(R.id.accountUserName);
        Button signUpButton = findViewById(R.id.signUpButton);
        radioButtonDriver = findViewById(R.id.radioButtonDriver);
        radioButtonRider = findViewById(R.id.radioButtonRider);
        //make this 2 button global within the class
        //RadioButton radioButtonDriver = findViewById(R.id.radioButtonDriver);
        //RadioButton radioButtonRider = findViewById(R.id.radioButtonRider);
        // register onlickfunction for RadioButton
        //new add start
        radioButtonDriver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //if driver is selected, rider cant be choose.
                if(buttonView.isChecked()){
                    radioButtonRider.setChecked(false);
                }
            }
        });

        radioButtonRider.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //if driver is selected, rider cant be choose.
                if(buttonView.isChecked()){
                    radioButtonDriver.setChecked(false);
                }
            }
        });
        //new add end


        checkBoxPolicy = findViewById(R.id.checkBoxPolicy);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add
                //if agree with policy and choose role, then allow to sign in, otherwise warning to agree with policy(done)
                //to do(name restriction)
                if(checkBoxPolicy.isChecked() && (radioButtonDriver.isChecked() || radioButtonRider.isChecked())){
                    Intent intentSetPassword = new Intent(SignUpActivity.this, SetPasswordActivity.class);
                    startActivity(intentSetPassword);
                    User user= new User();
                    user.setUsername(editTextUserName.getText().toString());
                    user.setEmailAddress(editTextPhoneNumber.getText().toString());
                    Database db = new Database();}
                else{
                    Toast.makeText(SignUpActivity.this,"please Enter all the information",Toast.LENGTH_LONG).show();
                }
                //db.add_new_user(user);
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
