package com.example.autobot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class SignUpActivity extends AppCompatActivity implements AddLicenseFragment.OnFragmentInteractionListener{

    int Type_Rider = 0 ;
    boolean Checkbox = false;
    boolean UserValid = false;
    boolean PhoneValid = false;
    Database db;
    String driving_license = "";
    String car_license = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        TextView textViewBackToLogin = findViewById(R.id.textViewGoToSignUp);
        final Button ContinueButton = findViewById(R.id.ContinueButton);
        final CheckBox checkBoxPolicy = findViewById(R.id.checkBoxPolicy);
        final RadioButton radioButtonDriver = findViewById(R.id.radioButtonDriver);
        final RadioButton radioButtonRider = findViewById(R.id.radioButtonRider);

        radioButtonDriver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //if driver is selected, rider cant be choose.
                if(buttonView.isChecked()){
                    Type_Rider = 1;
                    //jump out of loop until input correctly
                    new AddLicenseFragment().show(getSupportFragmentManager(),"Input_license");
                    radioButtonRider.setChecked(false);
                }
            }
        });

        radioButtonRider.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //if driver is selected, rider cant be choose.
                if(buttonView.isChecked()){
                    Type_Rider = 2;
                    radioButtonDriver.setChecked(false);
                }
            }
        });

        checkBoxPolicy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxPolicy.isChecked()) {
                    Checkbox = true;
                }
            }
        });

        ContinueButton.setOnClickListener(new OnClickListener() {
              @Override
              public void onClick(View v) {
                  db = MainActivity.db;
                  final EditText editTextPhoneNumber = findViewById(R.id.accountPhoneNumber);
                  final EditText editTextUserName = findViewById(R.id.accountUserName);
                  final String Username = editTextUserName.getText().toString();
                  final String PhoneNumber = editTextPhoneNumber.getText().toString();

                  if (Username.length() != 0 && PhoneNumber.length() != 0) {
                      db.getRef(Username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                          @Override
                          public void onSuccess(DocumentSnapshot documentSnapshot) {
                              if (documentSnapshot.exists()) {
                                  editTextUserName.setError("The User name is exist");
                                  UserValid = false;
                              } else UserValid = true;
                          }
                      });
                      Query query = db.collectionReference_user.whereEqualTo("PhoneNumber", PhoneNumber);
                      query.get()
                              .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                  @Override
                                  public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                      if (task.isSuccessful()) {
                                          if (task.getResult().size() != 0) {
                                              editTextPhoneNumber.setError("PhoneNumber is exist");
                                              PhoneValid = false;
                                          } else PhoneValid = true;
                                      }
                                  }
                              });
                  }
                  else {
                      if(Username.length() == 0) editTextUserName.setError("Please input Username!");
                      if (PhoneNumber.length() == 0) editTextPhoneNumber.setError("Please input PhoneNumber!"); }
                  if (Type_Rider == 0) radioButtonRider.setError("Please choose Driver or Rider!");
                  else radioButtonRider.setError(null,null);
                  if (Checkbox == false) checkBoxPolicy.setError("Please agree policy!");
                  else checkBoxPolicy.setError(null,null);

                  if (UserValid == true && PhoneValid == true && Type_Rider != 0 && Checkbox == true) {
                      User user = new User(Username);
                      user.setUsername(editTextUserName.getText().toString());
                      user.setPhoneNumber(editTextPhoneNumber.getText().toString());
                      if (Type_Rider == 2) user.setUserType("Rider");
                      else user.setUserType("Driver");
                      UserValid = false;
                      PhoneValid = false;
                      Intent intentSetPassword = new Intent(SignUpActivity.this, SetPasswordActivity.class);
                      intentSetPassword.putExtra("Username",user.getUsername());
                      intentSetPassword.putExtra("PhoneNumber",user.getPhoneNumber());
                      intentSetPassword.putExtra("Type",user.getUserType());
                      startActivity(intentSetPassword);
                  }


              }


        });

        textViewBackToLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intentLogin);
            }
        });

    }
    @Override
    public void get_license_input(String driving_license, String car_license) {
        this.driving_license = driving_license;
        this.car_license = car_license;
        if(driving_license.length() != 9 && car_license.length() != 7){
            new AddLicenseFragment().show(getSupportFragmentManager(),"Input_license");
            Toast.makeText(SignUpActivity.this,"input again",Toast.LENGTH_LONG).show();
        }
    }
}
