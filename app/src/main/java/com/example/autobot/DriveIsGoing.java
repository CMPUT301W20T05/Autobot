package com.example.autobot;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;


public class DriveIsGoing extends BaseActivity implements EditProfilePage.EditProfilePageListener {

    protected static Request request;
    private Database db;
    private String username;
    private User user;
    private Button buttonCancelOrder;
    private static final int REQUEST_PHONE_CALL = 101;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Driver Mode");
        View rootView = getLayoutInflater().inflate(R.layout.cancel_ride, frameLayout);

        db = DriverhomeActivity.db;
        user = LoginActivity.user; // get User
        //username = user.getUsername(); // get username
        username = request.getDriver().getUsername();
        detect_cancel_order();//detect whether user cancel order
        setProfile(username,db); // set profile
        set_profile_picture(rootView);//set profile picture
        //Log.d("debug",username);

        User rider = request.getRider();
        TextView reminder = findViewById(R.id.driver_condition);
        reminder.setText("drive safe");
        TextView rider_name = findViewById(R.id.Driver_name);
        rider_name.setText("passenger: "+rider.getUsername());
        Button buttonSeeProfile = findViewById(R.id.see_profile);
        ImageButton imageButtonPhone = findViewById(R.id.phoneButton);
        ImageButton imageButtonEmail = findViewById(R.id.emailButton);
        buttonCancelOrder = findViewById(R.id.cancel_order);

        //for driver to call rider
        //String dphoneNumber = rider.getPhoneNumber();
        String rphoneNumber = "5875576400";

        //make a phone call to driver
        imageButtonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + rphoneNumber));//change the number.
                if (ActivityCompat.checkSelfPermission(DriveIsGoing.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(DriveIsGoing.this, "No permission for calling", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(DriveIsGoing.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                } else {
                    startActivity(callIntent);
                }
            }
        });

        // added by yiping, implementation of view profile of the rider

        TextView see_profile_button = rootView.findViewById(R.id.Driver_name); // 需改button id
        see_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view = LayoutInflater.from(DriveIsGoing.this).inflate(R.layout.profile_viewer, null);


                TextView fname = view.findViewById(R.id.FirstName);
                TextView lname = view.findViewById(R.id.LastName);
                TextView pnumber = view.findViewById(R.id.PhoneNumber);
                TextView email = view.findViewById(R.id.EmailAddress);

                fname.setText(rider.getFirstName());
                lname.setText(rider.getLastName());
                pnumber.setText(rider.getPhoneNumber());
                email.setText(rider.getEmailAddress());

                final AlertDialog.Builder alert = new AlertDialog.Builder(DriveIsGoing.this);
                alert.setView(view)
                        .setTitle("Details")
                        .setNegativeButton("Close",null);
                alert.show();
            }
        });

        //set the onclick function for button
        buttonCancelOrder.setText("Pick up passenager");
        pick_up_rider();
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


    //reset the button onclick function--------------------------------------
    /*public void accept_order(){
        buttonCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request.UpdateStatus(1);
                //need to add
                //update db
                //change the text view of button after accept order
                buttonCancelOrder.setText("Pick up passenager");
                Log.d("check",request.getStatus());
                //overide onclick
                pick_up_rider();
            }
        });
    }*/

    public void pick_up_rider(){
        buttonCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request.UpdateStatus(3);
                db.ChangeRequestStatus(request);
                //update db
                //db.ChangeRequestStatus(request);
                //change the text view of button after accept order
                buttonCancelOrder.setText("Finish");
                Log.d("check",request.getStatus());
                update_map();
                //override onlick
                finish_order();
            }
        });
    }


    public void finish_order(){
        buttonCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                request.UpdateStatus(4);
                //need to add
                //update db
                db.ChangeRequestStatus(request);

                Intent intentOrderComplete = new Intent(DriveIsGoing.this, Driver_ordercomplete.class);
                //intentOrderComplete.putExtra("Username",username);
                finish();
                intentOrderComplete.putExtra("Username",username);
                finish();
                startActivity(intentOrderComplete);
            }
        });
    }

    //manage the situation if the rider cancel the order
    public void detect_cancel_order(){
        //create listener for the selected request
        DocumentReference request_ref = db.collectionReference_request.document(request.getRequestID());
        request_ref.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                //error exist, cant find that ref
                if(e != null){}
                else{
                    //check whether the request is on cancel state
                    if((documentSnapshot.get("RequestStatus").toString()).equals("Cancel")){
                        Toast.makeText(DriveIsGoing.this,"Cancel",Toast.LENGTH_LONG).show();
                        //if order cancel return to the home page
                        Intent intent = new Intent(DriveIsGoing.this, DriverhomeActivity.class);
                        int pause_time = 3000;

                        FragmentManager fm = getSupportFragmentManager();
                        Fragment notification = new CancelNotifiFragment();
                        fm.beginTransaction().add(R.id.cancel_notification_fragment,notification).addToBackStack(null).commit();
                        delay(pause_time,intent);}
                    else if ((documentSnapshot.get("RequestStatus").toString()).equals("Rider Accepted")){
                        int pause_time = 3000;
                        FragmentManager fm = getSupportFragmentManager();
                        Fragment notification = new SuccessfulNotification();
                        update_map();
                        fm.beginTransaction().add(R.id.cancel_notification_fragment,notification).addToBackStack(null).commit();
                        delay(pause_time,fm);
                    }
                }
            }
        });
    }

    //this function is for managing the process of swapping 2 activities
    public void delay(int pause_time,Intent intent){
        Timer timer = new Timer();
        TimerTask task = new TimerTask()
        {
            @Override
            public void run(){
                finish();startActivity(intent);
            }
        };
        timer.schedule(task,pause_time);

    }


    //successful notification
    public void delay(int pause_time,FragmentManager fm){
        Timer timer = new Timer();
        TimerTask task = new TimerTask()
        {
            @Override
            public void run(){
                fm.popBackStack();
            }
        };
        timer.schedule(task,pause_time);

    }

    public void update_map(){
        LatLng start = request.getBeginningLocation();
        LatLng destination = request.getDestination();
        drawRoute(start,destination);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void set_profile_picture(View view){
        ImageView profile = view.findViewById(R.id.imageViewAvatar);
        //if user dose have profile,get the url of pic resource
        String url = request.getRider().getUri();
        try {
            Bitmap profile_pic= BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
            profile.setAdjustViewBounds(true);
            profile.setImageBitmap(profile_pic);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e){
        }
    }


}
