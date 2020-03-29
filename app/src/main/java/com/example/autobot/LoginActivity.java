package com.example.autobot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.List;

import io.paperdb.Paper;

/**
 * this is class for login activity
 * user can login our system using their phone number/user name/email account
 * But email log in not complete
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Login";
    public static Database db;
    public static User user;
    public static Request loaded_request;
    SharedPreferences sharedPreferences;
    public long firstPressedTime;
    public Toast backToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = MainActivity.db;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setTitle("Login");
        Spinner spinner = findViewById(R.id.spinnerPhoneNumberCountry);
        EditText editAccount = findViewById(R.id.editAccount);
        EditText editTextInputPassword = findViewById(R.id.editTextInputPassword);
        CheckBox checkBoxRememberMe = findViewById(R.id.rememberMe);

        Paper.init(this);
        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserPhoneKey != null && UserPasswordKey != null)
        {
            if (!TextUtils.isEmpty(UserPhoneKey)  &&  !TextUtils.isEmpty(UserPasswordKey))
            {
                if (user.getUserType() == "Rider") {
                    Intent intentHomePage = new Intent(LoginActivity.this, HomePageActivity.class);
                    startActivity(intentHomePage);
                }
                else {
                    Intent intentHomePage = new Intent(LoginActivity.this, DriverhomeActivity.class);
                    startActivity(intentHomePage);
                }
            }
        }
        else {
            TextView textViewNoAccount = findViewById(R.id.textViewGoToSignUp);
            textViewNoAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intentSignUp);
                }});


            Button buttonLogin = findViewById(R.id.buttonLogin);
            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        db = new Database();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String Status = spinner.getSelectedItem().toString();
                    String Account = editAccount.getText().toString();
                    String Password = editTextInputPassword.getText().toString();

                    if (checkBoxRememberMe.isChecked()){
//                        Paper.book().write(Prevalent.UserPhoneKey, Account);
//                        Paper.book().write(Prevalent.UserPasswordKey, Password);
                    }

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
                                                        //get user infor from database
                                                         user = new User(username);
                                                         DocumentReference documentReference = db.collectionReference_user.document(username);
                                                         documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                         @Override
                                                         public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            DocumentSnapshot document = task.getResult();
                                                            user.setEmailAddress((String) document.get("EmailAddress"));
                                                            user.setFirstName((String) document.get("FirstName"));
                                                            user.setLastName((String) document.get("LastName"));
                                                            double Lat = Double.valueOf((String) document.get("CurrentLocationLat"));
                                                            Log.d("Testing",(String) document.get("CurrentLocationLat"));
                                                            double Lnt = Double.valueOf((String) document.get("CurrentLocationLnt"));
                                                            LatLng CurrentLocation = new LatLng(Lat, Lnt);
                                                            user.updateCurrentLocation(CurrentLocation);
                                                            user.setEmergencyContact((String) document.get("EmergencyContact"));
                                                            user.setHomeAddress((String) document.get("HomeAddress"));
                                                            user.setPassword((String) document.get("Password"));
                                                            user.setPhoneNumber((String) document.get("PhoneNumber"));
                                                            user.setStars(Double.valueOf((String) document.get("StarsRate")));
                                                            user.setUserType((String) document.get("Type"));
                                                            user.setUsername((String) document.get("Username"));
                                                            String uri = ((String) document.get("ImageUri"));
//                                    if (uri != null) {
//                                        user.setUri(Uri.parse(uri));
//                                    }
                                                            user.setUri(uri);
                                                            user.setGoodRate((String) document.get("GoodRate"));
                                                            user.setBadRate((String) document.get("BadRate"));
                                                            Log.d("Testing",user.getUserType()+"hihih");
                                                            //save user in shareprefence, don't need to login when you reopen the app
                                                            save_user_login();
                                                        }
                                                    });
                                                        if (TruePassword.equals(Password)){
                                                            // determine to go rider mode or driver mode
                                                            if (Type.equals("Rider")) {
                                                                Intent intentHomePage = new Intent(LoginActivity.this, HomePageActivity.class);
                                                                startActivity(intentHomePage);
                                                            }
                                                            else {
                                                                Intent intentHomePage = new Intent(LoginActivity.this, DriverhomeActivity.class);
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
                                                String userName = Account; // set username to username
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

    public void load_user(){
        try{
            sharedPreferences = getPreferences(MODE_PRIVATE);
        //if the user had login in before, retrieve that user from sharedpreferences, dont need to do the login again
            user = Offline.ExtractUser(sharedPreferences);
        Log.d("loaduser",user.toString());
        }
        catch (Exception e){
            Log.d("loaduser","no saved user"+e.toString());
        }
    }

    public void load_request(){
        try{
            //if the user had login in before, retrieve that user from sharedpreferences, dont need to do the login again
            sharedPreferences = getPreferences(MODE_PRIVATE);
            loaded_request = Offline.ExtractRequest(sharedPreferences);
            Log.d("loadrequest",loaded_request.get_active_requset_tostring());
        }
        catch (Exception e){
            Log.d("loadrequest","no saved request"+e.toString());
        }
    }

    public void save_user_login(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        Offline.UploadUser(sharedPreferences,user);
    }
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstPressedTime < 2000) {
            backToast.cancel();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        } else {
            backToast = Toast.makeText(LoginActivity.this, "Press another time to Quit", Toast.LENGTH_SHORT);
            backToast.show();
            firstPressedTime = System.currentTimeMillis();
        }
    }




}
