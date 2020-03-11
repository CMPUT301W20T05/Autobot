package com.example.autobot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setTitle("Login");
        Spinner spinner = findViewById(R.id.spinnerPhoneNumberCountry);
        EditText editAccount = findViewById(R.id.editAccount);
        EditText editTextInputPassword = findViewById(R.id.editTextInputPassword);



        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database db = new Database();
                String Status = spinner.getSelectedItem().toString();
                String Account = editAccount.getText().toString();
                String Password = editTextInputPassword.getText().toString();
                if (Status.equals("Phone Number")){
                    if (Account.length() == 0) editAccount.setError("Please input PhoneNumber");
                    else {
                        Query query = db.collectionReference_user.whereEqualTo("PhoneNumber", Account);
                        query.get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().size() != 0) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    String TruePassword = document.get("Password").toString();
                                                    String Type = document.get("Type").toString();
                                                    String username = document.get("Username").toString();
                                                    if (TruePassword.equals(Password)){
                                                        if (Type.equals("Rider")) {
                                                            Intent intentHomePage = new Intent(LoginActivity.this, HomePageActivity.class);
                                                            intentHomePage.putExtra("User",username);
                                                            startActivity(intentHomePage);
                                                        }
                                                        else {
                                                            Intent intentHomePage = new Intent(LoginActivity.this, DriverhomeActivity.class);
                                                            intentHomePage.putExtra("User",username);
                                                            startActivity(intentHomePage);
                                                        }
                                                    }
                                                    else {
                                                        editTextInputPassword.setError("The Wrong password!");
                                                    }
                                                }

                                            } else editAccount.setError("PhoneNumber is not exist");
                                        }
                                    }
                                });
                    }
                }
                else if (Status.equals("User Name"))
                {
                    if (Account.length() == 0) editAccount.setError("Please input Username");
                    else {
                        db.getRef(Account).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()){
                                            String RightPassword = documentSnapshot.getString("Password");
                                            String Type = documentSnapshot.getString("Type");
                                            if (RightPassword.equals(Password)) {
                                                if (Type.equals("Rider")) {
                                                    Intent intentHomePage = new Intent(LoginActivity.this, HomePageActivity.class);
                                                    intentHomePage.putExtra("User",Account);
                                                    startActivity(intentHomePage);
                                                }
                                                else {
                                                    Intent intentHomePage = new Intent(LoginActivity.this, DriverhomeActivity.class);
                                                    intentHomePage.putExtra("User",Account);
                                                    startActivity(intentHomePage);
                                                }
                                            }
                                            else editTextInputPassword.setError("The Wrong password!");
                                        }
                                        else {
                                            editAccount.setError("User name is not exist!");
                                        }
                                    }
                                });
                     }
                }
                }


        });
    }
}
