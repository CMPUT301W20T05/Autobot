package com.example.autobot;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.maps.android.SphericalUtil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Date;
import java.util.zip.Inflater;


public class DriverhomeActivity extends BaseActivity implements ActiverequestsFragment.OnBackPressed, ShowSelectedActiveRequestFragment.ButtonPress{
    public static User user;
    private String user_id;
    String phone_num;
    FragmentManager active_request_fm;
    Button confirm;
    LatLng origin;
    View header;
    static ArrayList<Request> requests_list;
    View rootView;
    ActiveRequestsAdapter adapter;
    private String username;
    public static Database db;
    private static final String TAG = "DriverhomeActivity";
    Marker beginning_location;
    Fragment fragment1;
    Fragment fragment2;
    public StorageReference storageReference;
    public FirebaseStorage storage;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("driver mode");
        user = LoginActivity.user;
        db = LoginActivity.db; // get database
        username = user.getUsername(); // get username
        requests_list = new ArrayList<Request>();
        Log.d("username3",username+"hi");
        setProfile(username,db); // set profile
        if(LoginActivity.load_request(getApplicationContext()) != null){
            notificationManager = NotificationManagerCompat.from(this);
            sendOnChannel("You have a current incomplete request. Please check your request history.");
        }
        //attach listener
        //testing
        /*user = new User();
        user.setFirstName("jc");
        user.setLastName("lyu");*/
        //update_navigation_view(user);
        //--------------------------------
        rootView = getLayoutInflater().inflate(R.layout.home_page, frameLayout);
        //hide that useless bar
        rootView.findViewById(R.id.autocomplete_destination).setVisibility(View.GONE);
        rootView.findViewById(R.id.buttonConfirmRequest).setVisibility(View.GONE);
        rootView.findViewById(R.id.buttonShowDirection).setVisibility(View.GONE);
        //initial the search bar
        AutocompleteSupportFragment autocompleteFragmentOrigin = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_origin);
        // Specify the types of place data to return.
        if (autocompleteFragmentOrigin != null) {
            autocompleteFragmentOrigin.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
            setAutocompleteSupportFragment(autocompleteFragmentOrigin,"");
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
                        fragment1 = new ActiverequestsFragment(requests_list,adapter);
                        load_requests(searchedLatLng);
                        active_request_fm = getSupportFragmentManager();
                        active_request_fm.beginTransaction().replace(R.id.myMap,fragment1).addToBackStack(null).commit();
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
    /*
     * @param searchedLatLng where of searching location
     */
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
                                if( 3000 >= Math.round(SphericalUtil.computeDistanceBetween(searchedLatLng,BeginningLocation)) && (b.equals("Request Sending"))){
                                    //clone all the info of satisfied request
                                    String request_id = (String) document.get("RequestID");
                                    String rider_id = (String) document.get("Rider");
                                    double Estcost = Double.parseDouble(document.get("EstimateCost").toString());
                                    double tips =  Double.parseDouble(document.get("Tips").toString());
                                    String Accepttime = (String) document.get("AcceptTime");
                                    String send_time = (String) document.get("SendTime");
                                    LatLng Destination = new LatLng(Double.valueOf((String)document.get("DestinationLat")),Double.valueOf((String)document.get("DestinationLnt")));
                                    //retrieve_request(request_id,rider_id,BeginningLocation,Destination,Estcost,Accepttime,send_time);
                                    try {
                                        Request active_request = retrieve_request(request_id,rider_id,BeginningLocation,Destination,Estcost,Accepttime,send_time,tips);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(BeginningLocation);
                                        markerOptions.title(request_id);
                                        //mark down order Location
                                        mMap.addMarker(markerOptions);
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BeginningLocation, DEFAULT_ZOOM));
                                        //Request active_request = db.rebuildRequest((String)request_id, db.rebuildUser(user_id));
                                        //testing
                                        //User user3 = new User("jc");
                                        //Request active_request = new Request(user3);
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

    //--------------listener of show detail of active request fragment
     //* @param request the request we want to select
    //handle confirm order event
    @Override
    public void confirm_request(Request request){
        //update request in the database
        request.UpdateStatus(1);
        //user = LoginActivity.user;
        //set up the drive
        request.setDriver(user);
        Date date = new Date(System.currentTimeMillis());
        request.resetAcceptTime(date, db);
        Log.d("username",username);
        db.ChangeRequestStatus(request);
        //notify need to modify database
        //db.ChangeRequestStatus(request);
        Log.d("debug",request.getStatus());

        DriveIsGoing.request = request;
        LoginActivity.save_request(request);
        //start new activity
        Intent intent = new Intent(DriverhomeActivity.this,DriveIsGoing.class);
        intent.putExtra("Username",username);
        finish();
        startActivity(intent);
    }
    //handle back press event
    @Override
    public void back_press(){
        active_request_fm.popBackStack();
        //hide the beginning marker
        remove_beginning_location();
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(searchedLatLng.latitude, searchedLatLng.longitude), DEFAULT_ZOOM));
    }
    //-----------------------------------------
    //handle hiding listview
    @Override
    public void hide() {
        //hide the request list view
        requests_list.clear();
        adapter.notifyDataSetChanged();
        active_request_fm.popBackStack();
        rootView.findViewById(R.id.autocomplete_origin).setVisibility(View.VISIBLE);
        //remove_beginning_location();
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(searchedLatLng.latitude, searchedLatLng.longitude), DEFAULT_ZOOM));
    }
<<<<<<< HEAD
=======

>>>>>>> 4b5f5f61380a7acf68df9e9f83ff7799e532709e
    /*this function for showing the selected request
     *@param showSelectedActiveRequestFragment the view detail fragment we want to rise
     *@param pos the postion of that request in requests list
     */
    @Override
    public void show_detail(ShowSelectedActiveRequestFragment showSelectedActiveRequestFragment, int pos) {
        //mark up the beginning location of the sletected reuqest
        MarkerOptions marker = new MarkerOptions();
        marker.position(requests_list.get(pos).getBeginningLocation());
        marker.title("Beginning Location");
        //mark down the mark action, need to be removed if i click on the another request
        beginning_location = mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(requests_list.get(pos).getBeginningLocation(), DEFAULT_ZOOM));
        //inflate the fragment
        fragment2 = showSelectedActiveRequestFragment;
        active_request_fm.beginTransaction().replace(R.id.myMap,showSelectedActiveRequestFragment).addToBackStack(null).commit();

    }
    /*
    *param adapter the adapter of list view
    * this function for notify adapter that data changed
     */
    @Override
    public void update_adapter(ActiveRequestsAdapter adapter){
        adapter.notifyDataSetChanged();
    }
    /*
    *@param request_id id of that request
    * @param rider_id rider id
    * @param EstCost the estimated cost
    * @param Accepttime when driver accept the oeder
    * @param BeginningLocation where is the passenger
    * @param tips the tips of the order
    * @param send time the send time of the order
<<<<<<< HEAD
=======
    * @return the request corresponding to the input request id
>>>>>>> 4b5f5f61380a7acf68df9e9f83ff7799e532709e
     */
    //for rebuilding a request
    public Request retrieve_request(String request_id, String rider_id,LatLng BeginningLocation, LatLng Destination,double EstCost,String Accepttime,String send_time,double tips)throws ParseException{
        Log.d("request_id",request_id);
        Log.d("rider_id",rider_id);
        Log.d("cost",String.valueOf(EstCost));
        Log.d("time",Accepttime);
        Log.d("stime",send_time);

        User rider = new User(rider_id) ;
        RebuildTool.rebuild_user(db.collectionReference_user.document(rider_id),rider);
        Log.d("Testing",rider.getUsername()+rider.getUserType());
        Log.d("driver",user.getUsername());
        //User rider = db.rebuildUser(rider_id);
        Request request = new Request(rider,BeginningLocation,Destination);
        request.setRequestID(request_id);
        request.setTips(tips);
        request.setCost(tips+EstCost);
        //set up date format
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //set up all time related attributes
        try{
            Date acceptedtime = formatter.parse(Accepttime);
            Date Sendtime = formatter.parse(send_time);
            request.setAcceptTime(acceptedtime);
            request.resetSendTime(Sendtime);
        } catch (ParseException e){
            e.printStackTrace();
        }

        request.direct_setEstimateCost(EstCost);
        return request;
    }
    //for moving the map focus
    public void remove_beginning_location(){
        if(beginning_location != null){
            beginning_location.remove();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(searchedLatLng.latitude, searchedLatLng.longitude), DEFAULT_ZOOM));
    }

    @Override
    public void onBackPressed(){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();  // setup fragmentTransaction

        navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu(); // get the menu
        MenuItem emItem = menu.findItem(R.id.edit_profile); // item edit profile
        MenuItem mhItem = menu.findItem(R.id.my_request_history); // item my request history
        MenuItem mnItem = menu.findItem(R.id.my_notification); // item my notification
        MenuItem piItem = menu.findItem(R.id.payment_information); // item payment information
        MenuItem sItem = menu.findItem(R.id.settings); // item settings
        MenuItem lItem = menu.findItem(R.id.log_out); // item log out

        //  store the menu to var when creating options menu

        if (drawer.isDrawerOpen(GravityCompat.START)) {  // if the drawer is opened, when a item is clicked, close the drawer
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (fragment == null){
            int i = fragmentManager.getFragments().size()-1;
            if (fragmentManager.getFragments().get(i) == fragment1){
                ft.remove(fragment1);
                requests_list.clear();
                adapter.notifyDataSetChanged();
                super.onBackPressed();
            } else if (fragmentManager.getFragments().get(i) == fragment2){
                ft.remove(fragment2);
                super.onBackPressed();
            } else {
                if(System.currentTimeMillis() - firstPressedTime<2000){
                    backToast.cancel();
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                }else{
                    backToast = Toast.makeText(DriverhomeActivity.this,"Press another time to Quit",Toast.LENGTH_SHORT);
                    backToast.show();
                    firstPressedTime = System.currentTimeMillis();
                }
            }
        } else {
            ft.remove(fragment).commit();
            fragment = null;
            setTitle("driver mode");
            onResume();
            try {
                requests_list.clear();
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
            }
            frameLayout.setVisibility(View.VISIBLE);
            frameLayout.invalidate();
        }

    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        //check if there is a unfinished request
        Request localRequest = LoginActivity.load_request(DriverhomeActivity.this);
        if (localRequest != null) {
            notificationManager = NotificationManagerCompat.from(this);
            sendOnChannel("You have a current incomplete request. Please check your request history.");

            @SuppressLint("InflateParams") View view = LayoutInflater.from(DriverhomeActivity.this).inflate(R.layout.current_request, null);
            TextView startLoc = view.findViewById(R.id.startLoc);
            TextView endLoc = view.findViewById(R.id.endLoc);
            ImageView avatar = view.findViewById(R.id.avatar);
            TextView name = view.findViewById(R.id.Name);
            ImageButton phone = view.findViewById(R.id.phoneButton);
            ImageButton email = view.findViewById(R.id.emailButton);
            TextView status = view.findViewById(R.id.status);
            TextView distance = view.findViewById(R.id.Appro_distance);
            TextView price = view.findViewById(R.id.Appro_price);
            //should be set as driver's infor
            User driver = localRequest.getRider();
            if (driver != null) {
                try{
                    AsnycProcess mytask = new AsnycProcess(driver.getUri(),avatar);
                    mytask.execute();
                //setAvatar(driver, avatar);
                }
                catch (Exception e){
                    Log.d("error",e.toString());
                }
                name.setText(String.format("%s %s", driver.getFirstName(), driver.getLastName()));
                phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        makePhoneCall(driver.getPhoneNumber());
                    }
                });
                email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendEmail(driver.getEmailAddress());
                    }
                });
            }
            else {
                name.setText("No driver accepted yet.");
            }
            //set request infor
            setReadableAddress(localRequest, localRequest.getBeginningLocation(), startLoc);
            setReadableAddress(localRequest, localRequest.getDestination(), endLoc);
            status.setText(localRequest.getStatus());
            distance.setText(calculateDistance(localRequest.getBeginningLocation(), localRequest.getDestination()));
            price.setText(String.valueOf(localRequest.getCost()));

            final AlertDialog.Builder alert = new AlertDialog.Builder(DriverhomeActivity.this);
            alert.setView(view)
                    .setTitle("Request Details")
                    .setNegativeButton("Close",null);
            alert.show();
        }

        Offline.clear_request(LoginActivity.sharedPreferences);
    }
    //creating this class for Asnyc loading the profile imgae
    class AsnycProcess extends AsyncTask<Void, Void, Void> {
        String url_web;
        Bitmap avatar;
        ImageView imageViewAvatar;
        public AsnycProcess(String url_web,ImageView imageViewAvatar){
            this.url_web = url_web;
            this.imageViewAvatar = imageViewAvatar;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            URL url;
            try{
            url = new URL(url_web);
            avatar = BitmapFactory.decodeStream((InputStream) url.getContent());}
            catch (Exception e){
                Log.d("IOerror",e.toString());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            imageViewAvatar.setImageBitmap(avatar);
        }
    }
}
