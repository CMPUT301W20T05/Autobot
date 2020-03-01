package com.example.autobot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        final Intent intent = getIntent();
        TextView textViewBackToLogin = findViewById(R.id.textViewGoToSignUp);

        Button ContinueButton = findViewById(R.id.ContinueButton);
        RadioButton radioButtonDriver = findViewById(R.id.radioButtonDriver);
        RadioButton radioButtonRider = findViewById(R.id.radioButtonRider);
        CheckBox checkBoxPolicy = findViewById(R.id.checkBoxPolicy);
        final Database db = new Database();
        //final DocumentReference Userref = db.collectionReference_request.document("Username");


        ContinueButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  EditText editTextPhoneNumber = findViewById(R.id.accountPhoneNumber);
                  EditText editTextUserName = findViewById(R.id.accountUserName);
                  String Username = editTextUserName.getText().toString();
                  String PhoneNumber = editTextPhoneNumber.getText().toString();
                  Database.getUsername(Username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                      @Override
                      public void onSuccess(DocumentSnapshot documentSnapshot) {
                          if (documentSnapshot.exists()) {
                              Toast.makeText(SignUpActivity.this, "Username is exist", Toast.LENGTH_SHORT).show();
                          } else {
                              Intent intentSetPassword = new Intent(SignUpActivity.this, SetPasswordActivity.class);
                              startActivity(intentSetPassword);
                          }
                      }
                  });
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
