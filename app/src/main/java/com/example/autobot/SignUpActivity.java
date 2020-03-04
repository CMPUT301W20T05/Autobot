package com.example.autobot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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







        textViewBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intentLogin);
            }
        });

    }
}
