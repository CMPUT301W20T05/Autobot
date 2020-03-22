package com.example.autobot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.maps.android.SphericalUtil;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.zip.Inflater;

public class DriverhomeActivity extends BaseActivity implements ActiverequestsFragment.OnBackPressed ,EditProfilePage.EditProfilePageListener, ShowSelectedActiveRequestFragment.ButtonPress{
    public static User user;
    private String user_id;
    String phone_num;
    FragmentManager active_request_fm;
    Button confirm;
    LatLng origin;
    View header;
    static ArrayList<Request> requests_list = new ArrayList<Request>();;
    View rootView;
    ActiveRequestsAdapter adapter;
    private String username;
    public static Database db;
    private static final String TAG = "DriverhomeActivity";
    Marker beginning_location;



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        db = LoginActivity.db;

        //load_user();
        setTitle("driver mode");

        db = LoginActivity.db; // get database
        user = LoginActivity.user; // get User
        username = user.getUsername(); // get username

        setProfile(username,db); // set profile

        //attach listener
        //testing
        /*user = new User();
        user.setFirstName("jc");
        user.setLastName("lyu");*/
        //update_navigation_view(user);
        //--------------------------------
        rootView = getLayoutInflater().inflate(R.layout.home_page, frameLayout);
        //hide that useless bar
        rootView.findViewById(R.id.autocomplete_destination).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.buttonConfirmRequest).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.buttonShowDirection).setVisibility(View.INVISIBLE);
        //initial the search bar
        AutocompleteSupportFragment autocompleteFragmentOrigin = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_origin);
        // Specify the types of place data to return.
        if (autocompleteFragmentOrigin != null) {
            autocompleteFragmentOrigin.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
            setAutocompleteSupportFragment(autocompleteFragmentOrigin);
            autocompleteFragmentOrigin.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                    searchedLatLng = place.getLatLng();

                    //remove old marker and add new marker
                    if (currentLocationMarker != null) {
                        currentLocationMarker.remove();
                    }

                    mMap.clear();
                    MarkerOptions markerOptions = new MarkerOptions();
                    if (searchedLatLng != null) {
                        markerOptions.position(new LatLng(searchedLatLng.latitude, searchedLatLng.longitude));
                        markerOptions.title("Current Location");
                        currentLocationMarker = mMap.addMarker(markerOptions);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(searchedLatLng.latitude, searchedLatLng.longitude), DEFAULT_ZOOM));
                        remove_beginning_location();
                        //get all the satisfied active requests
                        //load_requests(searchedLatLng);
                        //rise the show active requests fragment, manage the fragments activities----------------------
                        /*requests_list = new ArrayList<Request>();
                        try{
                            User user3 = new User("jc");
                            Request active_request = new Request(user3);
                            requests_list.add(active_request);} catch (ParseException e){}*/
                        adapter = new ActiveRequestsAdapter(DriverhomeActivity.this,0,requests_list);
                        Fragment fragment = new ActiverequestsFragment(requests_list,adapter);
                        load_requests(searchedLatLng);
                        active_request_fm = getSupportFragmentManager();
                        active_request_fm.beginTransaction().replace(R.id.myMap,fragment).addToBackStack(null).commit();
                        //----------------------------------------------------------------------------------------------
                    }
                }

                @Override
                public void onError(@NonNull Status status) {
                               // TODO: Handle the error.
                        Log.i(TAG, "An error occurred: " + status);
                }
            });
        }

        /*Fragment fragment = new ActiverequestsFragment();
        active_request_fm = getSupportFragmentManager();
        active_request_fm.beginTransaction().replace(R.id.myMap,fragment).addToBackStack(null).commit();*/

    }
    //load the driver info from db and rebuilt it
    public void load_user(){
        final Intent intent = getIntent();
        String user_id = intent.getStringExtra("User");
        //user = db.rebuildUser(username);
    }

    //loading all the satisfied requests
    public void load_requests(LatLng searchedLatLng) {
           /*try{
           User user3 = new User("jc");
           Request active_request = new Request(user3);
           active_request.setBeginningLocation(searchedLatLng);
           requests_list.add(active_request);} catch (ParseException e){}*/
           db.collectionReference_request.get()
                   .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                           if (task.isSuccessful()){
                               for (DocumentSnapshot document: task.getResult()){
                                   //get the location of start
                                   Log.d("abc",(String)document.get("BeginningLocationLat")+(String)document.get("BeginningLocationLnt"));
                                   LatLng BeginningLocation = new LatLng(Double.valueOf((String)document.get("BeginningLocationLat")),Double.valueOf((String)document.get("BeginningLocationLnt")));
                                   //if the distance between beginning location and search place is within the range and the request is inactive, select that request
                                   long a = Math.round(SphericalUtil.computeDistanceBetween(searchedLatLng,BeginningLocation));
                                   String b = (String) document.get("RequestStatus");
                                   if( 30000000 >= Math.round(SphericalUtil.computeDistanceBetween(searchedLatLng,BeginningLocation)) && (b.equals("Request Sending"))){
                                       //clone all the info of satisfied request
                                       String request_id = (String) document.get("RequestID");
                                       String rider_id = (String) document.get("Rider");
                                       double Estcost = Double.parseDouble((String) document.get("EstimateCost"));
                                       String Accepttime = (String) document.get("AcceptTime");
                                       String send_time = (String) document.get("SendTime");
                                       LatLng Destination = new LatLng(Double.valueOf((String)document.get("DestinationLat")),Double.valueOf((String)document.get("DestinationLnt")));
                                       //retrieve_request(request_id,rider_id,BeginningLocation,Destination,Estcost,Accepttime,send_time);
                                       try {
                                           Request active_request = retrieve_request(request_id,rider_id,BeginningLocation,Destination,Estcost,Accepttime,send_time);
                                           //Request active_request = db.rebuildRequest((String)request_id, db.rebuildUser(user_id));
                                           //testing
                                           //User user3 = new User("jc");
                                           //Request active_request = new Request(user3);
                                           Toast.makeText(DriverhomeActivity.this,"success", Toast.LENGTH_SHORT).show();
                                           requests_list.add(active_request);
                                           adapter.notifyDataSetChanged();
                                       } catch (ParseException e) {
                                           e.printStackTrace();
                                       }
                                       //requests_list.add(active_request);
                                   }
                               }
                           } else {
                               Log.d("suck", "error getting documents: ", task.getException());
                           }
                       }
                   });
    }

    /*public void update_navigation_view(User user){
        NavigationView navigationView = getWindow().getDecorView().getRootView().findViewById(R.id.nav_view);
        //find the layout of header layout
        header = navigationView.getHeaderView(0);

        //find the text view of user name
        TextView user_name = header.findViewById(R.id.driver_name);
        ImageView profile = header.findViewById(R.id.profile_photo);

        //edit
        user_name.setText(user.getFirstName()+user.getLastName());
        Bitmap pic = Bitmap.createBitmap(200,200,Bitmap.Config.RGB_565);
        profile.setImageBitmap(pic);
    }*/


    @Override
    //--------------listener of show detail of active request fragment
    public void confirm_request(Request request){
        //update request in the database
        request.UpdateStatus(1);

        //set up the drive
        request.setDriver(user);

        //notify need to modify database
        //db.ChangeRequestStatus(request);
        Log.d("debug",request.getStatus());

        DriveIsGoing.request = request;

        //start new activity
        Intent intent = new Intent(DriverhomeActivity.this,DriveIsGoing.class);
        intent.putExtra("Username",username);
        startActivity(intent);
    }

    @Override
    public void back_press(){
        active_request_fm.popBackStack();
        //hide the beginning marker
        remove_beginning_location();
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(searchedLatLng.latitude, searchedLatLng.longitude), DEFAULT_ZOOM));
    }
    //-----------------------------------------
    @Override
    public void hide() {
              //hide the request list view
              requests_list.clear();
              adapter.notifyDataSetChanged();
              active_request_fm.popBackStack();
              rootView.findViewById(R.id.autocomplete_origin).setVisibility(View.VISIBLE);
              //hide the beginning marker
              remove_beginning_location();
              //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(searchedLatLng.latitude, searchedLatLng.longitude), DEFAULT_ZOOM));
    }
    @Override
    //show the detail of the selected active request
    public void show_detail(ShowSelectedActiveRequestFragment fragment, int pos) {
        //mark up the beginning location of the sletected reuqest
        MarkerOptions marker = new MarkerOptions();
        marker.position(requests_list.get(pos).getBeginningLocation());
        marker.title("Beginning Location");
        //mark down the mark action, need to be removed if i click on the another request
        beginning_location = mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(requests_list.get(pos).getBeginningLocation(), DEFAULT_ZOOM));

        //inflate the fragment
        active_request_fm.beginTransaction().replace(R.id.myMap,fragment).addToBackStack(null).commit();
    }

    //for edit profile info
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
    public void update_adapter(ActiveRequestsAdapter adapter){
        adapter.notifyDataSetChanged();
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
    public Request retrieve_request(String request_id, String rider_id,LatLng BeginningLocation, LatLng Destination,double EstCost,String Accepttime,String send_time)throws ParseException{
        Log.d("request_id",request_id);
        Log.d("rider_id",rider_id);
        Log.d("cost",String.valueOf(EstCost));
        Log.d("time",Accepttime);
        Log.d("stime",send_time);

        User user = new User(rider_id);
        Request request = new Request(user,BeginningLocation,Destination);
        request.setRequestID(request_id);
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

    public void remove_beginning_location(){
        if(beginning_location != null){
            beginning_location.remove();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(searchedLatLng.latitude, searchedLatLng.longitude), DEFAULT_ZOOM));
    }
}
