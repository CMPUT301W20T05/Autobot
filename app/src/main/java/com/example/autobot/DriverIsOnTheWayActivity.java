package com.example.autobot;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static com.android.volley.VolleyLog.TAG;

public class DriverIsOnTheWayActivity extends BaseActivity implements EditProfilePage.EditProfilePageListener {

    private Request request;
    private Database db;
    private String username;
    private User user;
    private String reID;
    private static final int REQUEST_PHONE_CALL = 101;
    private User rider;
    public static User driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Rider Mode");
        View rootView = getLayoutInflater().inflate(R.layout.cancel_ride, frameLayout);

        db = MainActivity.db;

        Intent intent = getIntent();

        //when driver arrived, show notification
        boolean value1 = true; // default value if no value was found
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("isChecked", 0);
        value1 = sharedPreferences.getBoolean("isChecked1", value1); // retrieve the value of your key
        if (value1){
            notificationManager = NotificationManagerCompat.from(this);
            sendOnChannel("Driver has accepted your request. Please wait for picking up.");
        }

        //get user from firebase
        //user = db.rebuildUser(username);
        user = HomePageActivity.user;
        username = user.getUsername();
        //get request from firebase
        //request = db.rebuildRequest(reID, user);
        request = HomePageActivity.request;
        reID = request.getRequestID();

        db.db1.collection("Request").document(reID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    request.reset_Request_Status((String) document.getString("RequestStatus"));
                    request.setDriver(driver);
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyy hh:mm:ss");
                    try {
                        request.resetAcceptTime(formatter.parse((String) document.getString("AcceptTime")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String drivername = (String) document.getString("Driver");
                    driver = new User(drivername);
                    //RebuildTool.rebuild_user(db.db1.collection("users").document(drivername), driver);
                    db.db1.collection("users").document(drivername).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                driver.setEmailAddress((String) document.get("EmailAddress"));
                                driver.setFirstName((String) document.get("FirstName"));
                                driver.setLastName((String) document.get("LastName"));
                                System.out.println(document.get("CurrentLocation"));
                                double Lat = Double.valueOf((String) document.get("CurrentLocationLat"));
                                double Lnt = Double.valueOf((String) document.get("CurrentLocationLnt"));
                                LatLng CurrentLocation = new LatLng(Lat, Lnt);
                                driver.updateCurrentLocation(CurrentLocation);
                                driver.setEmergencyContact((String) document.get("EmergencyContact"));
                                driver.setHomeAddress((String) document.get("HomeAddress"));
                                driver.setPassword((String) document.get("Password"));
                                driver.setPhoneNumber((String) document.get("PhoneNumber"));
                                driver.setStars(Double.valueOf((String) document.get("StarsRate")));
                                driver.setUserType((String) document.get("Type"));
                                driver.setUsername((String) document.get("Username"));
                                String uri = ((String) document.get("ImageUri"));
                                driver.setUri(uri);
                                driver.setGoodRate((String) document.get("GoodRate"));
                                driver.setBadRate((String) document.get("BadRate"));

                                //driver = request.getDriver();
                                rider = request.getRider();

                                setProfile(username,db); // set profile

                                //rider accepted
                                final BottomSheetDialog riderAcceptedDialog = new BottomSheetDialog(DriverIsOnTheWayActivity.this);
                                riderAcceptedDialog.setContentView(R.layout.rider_accept);
                                riderAcceptedDialog.setCancelable(false);
                                ImageView driverAvatar = riderAcceptedDialog.findViewById(R.id.imageViewAvatar);
                                TextView driverName = riderAcceptedDialog.findViewById(R.id.Driver_name);
                                TextView driverRate = riderAcceptedDialog.findViewById(R.id.driverRate);

                                setAvatar(driver, driverAvatar);
                                driverName.setText(String.format("%s %s", driver.getLastName(), driver.getFirstName()));
                                driverRate.setText(calculateRate(driver));

                                Button accept = riderAcceptedDialog.findViewById(R.id.acceptDriver);
                                Button reject = riderAcceptedDialog.findViewById(R.id.rejectDriver);
                                accept.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        request.resetRequestStatus("Rider Accepted",db);
                                        db.ChangeRequestStatus(request);
                                        //mark driver and rider location in map
                                        LatLng driverCurrent = driver.getCurrentLocation();
                                        LatLng riderCurrent = rider.getCurrentLocation();
                                        drawRouteWithAvatar(driverCurrent, riderCurrent, driver, rider);
                                        riderAcceptedDialog.dismiss();
                                    }
                                });
                                reject.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        request.resetRequestStatus("Cancel",db);
                                        db.ChangeRequestStatus(request);
                                        riderAcceptedDialog.dismiss();
                                        //return to homepage
                                        Intent finishRequest = new Intent(getApplicationContext(), HomePageActivity.class);
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(finishRequest);
                                        overridePendingTransition(0, 0);
                                    }
                                });
                                riderAcceptedDialog.show();

                                //new page components
                                TextView textViewDriverCondition = findViewById(R.id.driver_condition);
                                ImageView imageViewAvatar = findViewById(R.id.imageViewAvatar);
                                TextView textViewDriverRate = findViewById(R.id.driverRate);
                                TextView textViewDriverName = findViewById(R.id.Driver_name);
                                ImageButton imageButtonPhone = findViewById(R.id.phoneButton);
                                ImageButton imageButtonEmail = findViewById(R.id.emailButton);
                                TextView textViewEstimateTime = findViewById(R.id.EstimatedTime);
                                TextView textViewEstimateDist = findViewById(R.id.EstimatedDist);
                                Button buttonCancelOrder = findViewById(R.id.cancel_order);
                                Button buttonComplete = findViewById(R.id.complete);

                                //get location
                                LatLng destination = request.getDestination();
                                LatLng origin = request.getBeginningLocation();
                                //distance between two locations
                                double distance = Math.round(SphericalUtil.computeDistanceBetween(origin, destination));
                                DecimalFormat df1 = new DecimalFormat("0.00");
                                textViewEstimateDist.setText(df1.format(distance));
                                //calculate time
                                double time = distance / 1008.00 + 5.00;
                                textViewEstimateTime.setText(df1.format(time));
                                //set driver infor
                                setAvatar(driver, imageViewAvatar);
                                textViewDriverName.setText(String.format("%s%s", driver.getLastName(), driver.getFirstName()));
                                //good rate infor
                                textViewDriverRate.setText(calculateRate(driver));

                                //make a phone call to driver
                                imageButtonPhone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        makePhoneCall(driver.getPhoneNumber());
                                    }
                                });
                                //write a email to driver
                                imageButtonEmail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        sendEmail(driver.getEmailAddress());
                                    }
                                });

                                textViewDriverName.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        view = LayoutInflater.from(DriverIsOnTheWayActivity.this).inflate(R.layout.profile_viewer, null);

                                        TextView fname = view.findViewById(R.id.FirstName);
                                        TextView lname = view.findViewById(R.id.LastName);
                                        TextView pnumber = view.findViewById(R.id.PhoneNumber);
                                        TextView email = view.findViewById(R.id.EmailAddress);
                                        //should be set as driver's infor
                                        fname.setText(driver.getFirstName());
                                        lname.setText(driver.getLastName());
                                        pnumber.setText(driver.getPhoneNumber());
                                        email.setText(driver.getEmailAddress());

                                        final AlertDialog.Builder alert = new AlertDialog.Builder(DriverIsOnTheWayActivity.this);
                                        alert.setView(view)
                                                .setTitle("Details")
                                                .setNegativeButton("Close",null);
                                        alert.show();
                                    }
                                });

                                buttonCancelOrder.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //pop out dialog
                                        final AlertDialog.Builder alert = new AlertDialog.Builder(DriverIsOnTheWayActivity.this);
                                        alert.setTitle("Cancel Order");
                                        alert.setMessage("Are you sure you wish to cancel current request?")
                                                .setCancelable(false)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        //cancel current request
                                                        //db.CancelRequest(reID);
                                                        request.resetRequestStatus("Cancel",db);
                                                        db.ChangeRequestStatus(request);
                                                        //return to homepage
                                                        Intent finishRequest = new Intent(getApplicationContext(), HomePageActivity.class);
                                                        finish();
                                                        overridePendingTransition(0, 0);
                                                        startActivity(finishRequest);
                                                        overridePendingTransition(0, 0);
                                                    }
                                                })
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        return;
                                                    }
                                                });

                                        alert.show();
                                    }
                                });

                                buttonComplete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //arrive destination
                                        request.resetRequestStatus("Trip Completed",db);
                                        db.ChangeRequestStatus(request);
                                        Intent intentComplete = new Intent(DriverIsOnTheWayActivity.this, OrderComplete.class);
                                        finish();
                                        startActivity(intentComplete);
                                    }
                                });

                                //picked up rider
                                db.NotifyStatusChangeEditText(reID, "Rider picked", textViewDriverCondition, "Driving to destination...");

                                //rider confirm completion
                                db.NotifyStatusChangeButton(reID, "Rider picked", buttonCancelOrder, false);
                                db.NotifyStatusChangeButton(reID, "Rider picked", buttonComplete, true);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void updateInformation(String FirstName, String LastName, String EmailAddress, String HomeAddress, String emergencyContact, Bitmap bitmap) { // change the name on the profile page to the new input name
        name = findViewById(R.id.driver_name);
        String fullName = FirstName + " " + LastName;
        name.setText(fullName);
        profilePhoto = findViewById(R.id.profile_photo);
        mybitmap = bitmap;
        if (mybitmap != null) profilePhoto.setImageBitmap(mybitmap);

        User newUser = user;
        newUser.setFirstName(FirstName); // save the changes that made by user
        newUser.setLastName(LastName);
        newUser.setEmailAddress(EmailAddress);
        newUser.setHomeAddress(HomeAddress);
        newUser.setEmergencyContact(emergencyContact);

        db.add_new_user(newUser);

    }

    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public Bitmap getBitmap(){
        return mybitmap;
    }

    //request attributes
    public Request retrieve_request(String request_id, String rider_id,LatLng BeginningLocation, LatLng Destination,double EstCost,String Accepttime,String send_time,double tips)throws ParseException {
        Log.d("request_id",request_id);
        Log.d("rider_id",rider_id);
        Log.d("cost",String.valueOf(EstCost));
        Log.d("time",Accepttime);
        Log.d("stime",send_time);

        User user = db.rebuildUser(rider_id);
        Request request = new Request(user,BeginningLocation,Destination);
        request.setRequestID(request_id);
        request.setTips(tips);
        //set up date format
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyy hh:mm:ss");
        //set up all time related attributes
        try{
            Date acceptedtime = formatter.parse(Accepttime);
            Date Sendtime = formatter.parse(send_time);
            request.resetAcceptTime(acceptedtime);
            request.resetArriveTime(acceptedtime);
            request.resetSendTime(Sendtime);
        } catch (ParseException e){
            e.printStackTrace();
        }

        request.direct_setEstimateCost(EstCost);
        Log.d("testing",request.testing_rebuild_request());
        return request;
    }

    private void UpdateRequest(){
        //create listener for the selected request
        DocumentReference request_ref = FirebaseFirestore.getInstance().collection("Request").document(request.getRequestID());
        request_ref.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String driver_id = (String) documentSnapshot.get("Driver");
                            User driver = db.rebuildUser(driver_id);
                            request.setDriver(driver);
                        }
                    }
                });
    }

}
