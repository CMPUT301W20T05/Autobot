package com.example.autobot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.textclassifier.TextClassifierEvent;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.autobot.Adapter.HistoryRequestAdapter;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.ClusterManager;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.android.volley.VolleyLog.TAG;
import static com.example.autobot.App.CHANNEL_1_ID;

/**
 * this is a class of base activity, it contains google map api, side bar, notifications and so on
 * Reference:
 * https://www.tutorialspoint.com/how-to-show-current-location-on-a-google-map-on-android
 * direction api example: https://www.youtube.com/watch?v=wRDLjUK8nyU, Created by Vishal on 10/20/2018.
 */

//GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AddPaymentFragment.OnFragmentInteractionListener, OnMapReadyCallback, TaskLoadedCallback, LocationListener,AddCreditFragment.OnFragmentInteractionListener, EditProfilePage.EditProfilePageListener, PaymentInformationFragment.OnFragmentInteractionListenerPayment{
    public Database userbase;
    public DrawerLayout drawer;
    public ListView paymentList;
    public ArrayAdapter<PaymentCard> mAdapter;
    public ArrayList<PaymentCard> mDataList;
    public User user = LoginActivity.user;
    public FrameLayout frameLayout;
    public String un;
    //pls dont private these part
    //private GoogleMap mMap;
    public static GoogleMap mMap;
    private static Location currentLocation;
    LatLng searchedLatLng;
    private LatLng pickupLoc;
    //----------------------------------
    LocationCallback locationCallback; //for updating users request if last known location is null
    FusedLocationProviderClient fusedLocationProviderClient; //fetching the current location
    PlacesClient placesClient;
    List<AutocompletePrediction> predictionList;
    Marker currentLocationMarker;

    protected Polyline currentPolyline;
    public AutocompleteSupportFragment autocompleteFragment;
    public NavigationView navigationView;
    public Fragment fragment;

    //private final float DEFAULT_ZOOM = 18;
    final float DEFAULT_ZOOM = 18;
    public TextView name;
    public CircleImageView profilePhoto;
    public TextView goodrate;
    public TextView badrate;
    public StorageReference storageReference;
    public FirebaseStorage storage;

    private static final int REQUEST_CODE = 101;
    //private Object LatLng;
    private static final String TAG = "BaseActivity";
    //notification
    public NotificationManagerCompat notificationManager;
    //api key
    protected final String apiKey = "AIzaSyAk4LrG7apqGcX52ROWvhSMWqvFMBC9WAA";
    public int anInt = 0;
    public Bitmap mybitmap;
    public Uri myuri;
    private String temp1, temp2;

    public static Database db = LoginActivity.db; // get database
    public long firstPressedTime;
    public Toast backToast;
    public String username = user.getUsername();

    private static final int REQUEST_PHONE_CALL = 101;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        //set up google map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
//            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//            fetchLocation();
//            mapView = mapFragment.getView();
        }

        //set framelayout so the extended children of base activity can inflate its own layout
        frameLayout = (FrameLayout) findViewById(R.id.content);

        //set up tool bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get drawer
        drawer = findViewById(R.id.drawer_layout);
        // get navigation view
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0); // get header of the navigation view
        profilePhoto = header.findViewById(R.id.profile_photo);

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new EditProfilePage();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                navigationView.getMenu().getItem(0).setChecked(true);
                setTitle("Edit Profile");
                if (drawer.isDrawerOpen(GravityCompat.START)) {  // if the drawer is opened, when a item is clicked, close the drawer
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //initialize the location provider
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(BaseActivity.this);
        //initialize the places
        Places.initialize(BaseActivity.this, apiKey);
        placesClient = Places.createClient(this);
    }

    /**
     * set profile that display in sidebar
     * @param username
     * @param db
     */
    public void setProfile(String username, Database db){
        Database userBase = db;
        User usertemp = LoginActivity.user;

        drawer = findViewById(R.id.drawer_layout);
        // get navigation view
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0); // get header of the navigation view
        name = header.findViewById(R.id.driver_name);
        profilePhoto = header.findViewById(R.id.profile_photo);
        goodrate = header.findViewById(R.id.favorable_rate);
        badrate = header.findViewById(R.id.poor_rate);

        DocumentReference docRef = userBase.getRef(username);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {  // display username on navigation view
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String theUserName = document.getData().get("Username").toString();
                        String gr = document.getData().get("GoodRate").toString();
                        String br = document.getData().get("BadRate").toString();

                        Integer ggr = 0;
                        Integer bbr = 0;
                        if (!gr.equals("")){
                            ggr = Integer.parseInt(gr);
                        }
                        if (!br.equals("")){
                            bbr = Integer.parseInt(br);
                        }

                        DecimalFormat decimalFormat= new DecimalFormat(".00");
                        if (ggr == 0 && bbr == 0) {
                            temp1 = "0%";
                            temp2 = "0%";
                        } else{
                            float f1 = (float) ggr/(ggr + bbr)*100;
                            temp1 = decimalFormat.format(f1) + "%";
                            float f2 = (float) bbr/(ggr + bbr)*100;
                            temp2 = decimalFormat.format(f2) + "%";
                        }
                        goodrate.setText(temp1);
                        badrate.setText(temp2);
                        Object temp = document.getData().get("FirstName");
                        if (temp != null) {
                            String fullName = temp.toString() + " " + document.getData().get("LastName").toString();

                            name.setText(fullName);
                        }
                        TextView username = header.findViewById(R.id.user_name);

                        //get profile photo
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        try {
                            if (document.getData().get("ImageUri") != null){
                                mybitmap = BitmapFactory.decodeStream((InputStream)new URL(document.getData().get("ImageUri").toString()).getContent());
                                profilePhoto.setImageBitmap(mybitmap);
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        username.setText(theUserName);
                        //TextView
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * get user current location
     * @return currentlocation (Location)
     */
    public Location getCurrentLocation() {
        return currentLocation;
    }

    /**
     * get the location user searched
     * @return searched location (Latlng)
     */
    public LatLng getSearchedLatLng() {
        return searchedLatLng;
    }

    /**
     * get the picked up location if user searched
     * @return picked up location
     */
    public LatLng getPickupLoc() {
        return pickupLoc;
    }

    /**
     * set google autocomplete fragment
     * it contains setOnPlaceSelectedListener (add marker to the selected location and move camera)
     * @param autocompleteFragment autocomplete fragment (AutocompleteSupportFragment)
     */
    public void setAutocompleteSupportFragment(AutocompleteSupportFragment autocompleteFragment, String wheretogo) {

        if (autocompleteFragment != null) {
            // Specify the types of place data to return.
            autocompleteFragment.setPlaceFields(Arrays.asList(
                    Place.Field.ID,
                    Place.Field.LAT_LNG,
                    Place.Field.NAME,
                    Place.Field.ADDRESS,
                    Place.Field.PHONE_NUMBER));
        }

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                searchedLatLng = place.getLatLng();

                if (wheretogo == "curr") {
                    pickupLoc = searchedLatLng;
                }

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
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .build();

        placesClient.findAutocompletePredictions(predictionsRequest).addOnSuccessListener((response) -> {
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                Log.i(TAG, prediction.getPlaceId());
                Log.i(TAG, prediction.getPrimaryText(null).toString());
            }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
            }
        });
    }

    @Override
    protected void onResume() { // cancel all item onClicked
        super.onResume();
        for (int i = 0; i < navigationView.getMenu().size(); i++) {  // cancel selected on edit profile page of the menu item
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.my_request_history:
                fragment = new RequestHistoryFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.myMap, fragment).addToBackStack(null).commit();
                frameLayout.setVisibility(View.GONE);
                navigationView.getMenu().getItem(1).setChecked(true);
                setTitle("My Request History");
                break;
            case R.id.settings:
                fragment = new SettingsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.myMap, fragment).addToBackStack(null).commit();
                frameLayout.setVisibility(View.GONE);
                navigationView.getMenu().getItem(4).setChecked(true);
                setTitle("About Us");
                break;
            case R.id.payment_information:
                fragment = new PaymentInformationFragment();
                anInt = 1;
                getSupportFragmentManager().beginTransaction().replace(R.id.myMap, fragment).addToBackStack(null).commit();
                frameLayout.setVisibility(View.GONE);
                navigationView.getMenu().getItem(3).setChecked(true);
                setTitle("Payment Information");
                break;
            case R.id.my_notification:
                fragment = new Notifications();
                anInt = 1;
                getSupportFragmentManager().beginTransaction().replace(R.id.myMap, fragment).addToBackStack(null).commit();
                navigationView.getMenu().getItem(2).setChecked(true);
                frameLayout.setVisibility(View.GONE);
                setTitle("My Notifications");
                break;
            case R.id.log_out:
                navigationView.getMenu().getItem(5).setChecked(true);

                final AlertDialog.Builder alert = new AlertDialog.Builder(BaseActivity.this);
                alert.setTitle("Logout");
                alert.setMessage("Are you sure you wish to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //clear save instance
                                Offline.clear_user(LoginActivity.sharedPreferences);
                                Offline.clear_request(LoginActivity.sharedPreferences);
                                Intent logout = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(logout);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                navigationView.getMenu().getItem(5).setChecked(false);
                            }
                        });

                alert.show();
                break;
            case R.id.edit_profile:
                fragment = new EditProfilePage();
//                Bundle bundle = new Bundle();
//                bundle.putString("username",username);
//                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.myMap,fragment).addToBackStack(null).commit();
                frameLayout.setVisibility(View.GONE);
                navigationView.getMenu().getItem(0).setChecked(true);
                setTitle("Edit Profile");
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public Bundle setUserName(String username) {
        Bundle bundle = new Bundle();
        bundle.putString("username",username);
        return bundle;
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
            super.onBackPressed(); // back to the last activity
        } else if (onNavigationItemSelected(emItem)) { // if the edit profile page is opened, back to main page
            if (fragment != null){
                ft.remove(fragment).commit();
                onResume();
                fragment = null;
                setTitle("Home Page");
                frameLayout.setVisibility(View.VISIBLE);
                frameLayout.invalidate();
            }

        } else if (onNavigationItemSelected(mhItem)){ // if the my request history page is opened, back to main page
            if (fragment != null){
                ft.remove(fragment).commit();
                onResume();
                fragment = null;
                setTitle("Home Page");
                frameLayout.setVisibility(View.VISIBLE);
                frameLayout.invalidate();
            }

        } else if (onNavigationItemSelected(piItem)){ // if the payment information page is opened, back to main page
            if (fragment != null){
                Fragment wallet_fragment = fragmentManager.findFragmentByTag("WALLET_FRAGMENT");
                if (wallet_fragment instanceof Wallet_fragment && wallet_fragment.isVisible()) {
                    fragmentManager.popBackStackImmediate();
                } else {
                    ft.remove(fragment).commit();
                    onResume();
                    fragment = null;
                    setTitle("Home Page");
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.invalidate();
                }
            }

        } else if (onNavigationItemSelected(sItem)){ // if the settings page is opened, back to main page
            if (fragment != null){
                ft.remove(fragment).commit();
                onResume();
                fragment = null;
                setTitle("Home Page");
                frameLayout.setVisibility(View.VISIBLE);
                frameLayout.invalidate();
            }
        } else if (onNavigationItemSelected(mnItem)){ // if the notifications page is opened, back to main page
            if (fragment != null){
                ft.remove(fragment).commit();
                onResume();
                setTitle("Home Page");
                frameLayout.setVisibility(View.VISIBLE);
                frameLayout.invalidate();
            }
        }

    }

    @Override
    public void onOkPressed(PaymentCard newPayment,int i) {
        if (i == 1) Toast.makeText(this, "You have added this card.", Toast.LENGTH_SHORT).show();
        else ((PaymentInformationFragment) fragment).updateList(newPayment);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) { //will be called when map is ready and loaded
        mMap = googleMap;

        //make sure to have the location permission
        boolean fail = true;
        while (fail) {
            //check location permission
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {
                //buildGoogleApiClient();
                mMap.setMyLocationEnabled(true); //require location permission
                mMap.getUiSettings().setMyLocationButtonEnabled(true); //my location button will be shown
                fail = false;
            }else {
                //request location permission
                ActivityCompat.requestPermissions(this, new String[] {
                                ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION },
                        REQUEST_CODE);
            }
        }

        //check if gps is enabled or not and then request user to enable it
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(BaseActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(BaseActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });

        task.addOnFailureListener(BaseActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(BaseActivity.this, 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                return false;
            }
        });

    }

    //stop location updates when Activity is no longer active
    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * this method will be automatically called when user moves more than 10 meters, or update the current location per 10 seconds
     * and also, the current location will be updated to database
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        Database db2 = MainActivity.db;
        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }

        //place a new marker for current location
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        currentLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM));

        HashMap<String, Object> CurrentLocation = new HashMap<>();
        CurrentLocation.put("CurrentLocationLat", String.valueOf(currentLocation.getLatitude()));
        CurrentLocation.put("CurrentLocationLnt", String.valueOf(currentLocation.getLongitude()));

        db2.collectionReference_user.document(LoginActivity.user.getUsername())
                .update(CurrentLocation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Data addition successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data addition failed" + e.toString());
                    }
                });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
                getDeviceLocation();
            }
        }
    }

    /**
     * get current location
     */
    @SuppressLint("MissingPermission")
    protected void getDeviceLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            currentLocation = task.getResult();
                            if (currentLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM));
                                //set current location to firebase
                                Database db2 = MainActivity.db;
                                HashMap<String, Object> CurrentLocation = new HashMap<>();
                                CurrentLocation.put("CurrentLocationLat", String.valueOf(currentLocation.getLatitude()));
                                CurrentLocation.put("CurrentLocationLnt", String.valueOf(currentLocation.getLongitude()));

                                db2.collectionReference_user.document(LoginActivity.user.getUsername())
                                        .update(CurrentLocation)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Data addition successful");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG, "Data addition failed" + e.toString());
                                            }
                                        });

                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setSmallestDisplacement(1);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        currentLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM));
                                        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            Toast.makeText(BaseActivity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * send notification to the channel
     * @param message the message you want to show
     */
    public void sendOnChannel(String message) {

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_drive)
                .setContentTitle("New Notification")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(1, notification);
    }

    /**
     * use user's avatar as marker and add it to the map
     * @param location
     * @param user
     * @param title
     * @param snippet
     */
    public void addMapMarkers(LatLng location, User user, String title, String snippet){

        if(mMap != null){
            if (currentLocationMarker != null) {
                currentLocationMarker.remove();
            }
            //user photo as marker
            BitmapDescriptor locIcon = BitmapDescriptorFactory.fromResource(R.id.icon);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                Bitmap temp = BitmapFactory.decodeStream((InputStream)new URL(user.getUri()).getContent());
                locIcon = BitmapDescriptorFactory.fromBitmap(getResizedBitmap(temp, 100, 100));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e){
                Log.d(TAG, "addMapMarkers: no avatar, setting default.");
            }
            MarkerOptions marker = new MarkerOptions().position(location).title(title).snippet(snippet).icon(locIcon);
            mMap.addMarker(marker);
        }
    }

    /**
     * resize the bitmap to custom width and height
     * @param bm the bitmap to resize
     * @param newWidth
     * @param newHeight
     * @return return the resized bitmap
     */
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    /**
     * draw route between two locations
     * @param origin origin of user's request
     * @param destination destination of user's request
     */
    public void drawRoute(LatLng origin, LatLng destination) {
        MarkerOptions place1, place2;

        place1 = new MarkerOptions().position(origin).title("Origin");
        place2 = new MarkerOptions().position(destination).title("Destination");

        //add marker
        Log.d("mylog", "Added Markers");
        //remove old marker and add new marker
        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }

        if (mMap != null) {
            mMap.addMarker(place1);
            mMap.addMarker(place2);
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        // Add your locations to bounds using builder.include, maybe in a loop
        builder.include(origin);
        builder.include(destination);
        LatLngBounds bounds = builder.build();
        //Then construct a cameraUpdate
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 300);
        //Then move the camera
        if (mMap != null) {
            mMap.animateCamera(cameraUpdate);
        }

        String url = getUrl(place1.getPosition(), place2.getPosition(), "driving");
        new FetchURL(BaseActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
    }

    /**
     * draw route and user the user's avatar as marker
     * @param origin start location
     * @param destination end location
     * @param startUser the user at the start location
     * @param endUser the user at the end location
     */
    public void drawRouteWithAvatar(LatLng origin, LatLng destination, User startUser, User endUser) {

        //add marker
        Log.d("mylog", "Added Markers");
        //remove old marker and add new marker
        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }

        if(mMap != null){
            //user photo as marker
            BitmapDescriptor locIcon1 = BitmapDescriptorFactory.fromResource(R.id.icon);
            BitmapDescriptor locIcon2 = BitmapDescriptorFactory.fromResource(R.id.icon);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            MarkerOptions marker1 = new MarkerOptions().position(origin).title("Origin");
            MarkerOptions marker2 = new MarkerOptions().position(destination).title("Destination");;
            try {
                Bitmap temp1 = BitmapFactory.decodeStream((InputStream)new URL(startUser.getUri()).getContent());
                locIcon1 = BitmapDescriptorFactory.fromBitmap(getResizedBitmap(temp1, 100, 100));
                Bitmap temp2 = BitmapFactory.decodeStream((InputStream)new URL(endUser.getUri()).getContent());
                locIcon2 = BitmapDescriptorFactory.fromBitmap(getResizedBitmap(temp2, 100, 100));
                marker1 = new MarkerOptions().position(origin).title("Driver").snippet("This is driver").icon(locIcon1);
                marker2 = new MarkerOptions().position(destination).title("Rider").snippet("This is me").icon(locIcon2);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e){
                Log.d(TAG, "addMapMarkers: no avatar, setting default.");
            }
            mMap.addMarker(marker1);
            mMap.addMarker(marker2);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            // Add your locations to bounds using builder.include, maybe in a loop
            builder.include(origin);
            builder.include(destination);
            LatLngBounds bounds = builder.build();
            //Then construct a cameraUpdate
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 500);
            //Then move the camera
            mMap.animateCamera(cameraUpdate);

            String url = getUrl(marker1.getPosition(), marker2.getPosition(), "driving");
            new FetchURL(BaseActivity.this).execute(getUrl(marker1.getPosition(), marker2.getPosition(), "driving"), "driving");
        }
    }

    /**
     * connect origin, destination to generate a url
     * @param origin origin of user's request
     * @param dest destination of user's request
     * @param directionMode transportation method
     * @return url
     */
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + apiKey;
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    /**
     * This method is called to send email
     * @param email
     */
    public void sendEmail(String email) {
        if (!email.equals("")) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "No email address provided", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * this method is called to make a phone call
     * @param phoneNumber
     */
    public void makePhoneCall(String phoneNumber) {
        boolean success = false;
        if (!phoneNumber.equals("")) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));//change the number.
            while (!success) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "No permission for calling", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                } else {
                    success = true;
                    startActivity(callIntent);
                }
            }
        }
        else {
            Toast.makeText(this, "No phone number provided", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * this method is called to set avatar for the given user
     * @param user
     * @param imageViewAvatar
     */
    public void setAvatar(User user, ImageView imageViewAvatar) {
        try  {
            Bitmap avatar = BitmapFactory.decodeStream((InputStream)new URL(user.getUri()).getContent());
            imageViewAvatar.setImageBitmap(avatar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method is called to calculate the driver's rate
     * @param user
     * @return
     */
    public String calculateRate(User user) {
        DecimalFormat df = new DecimalFormat("0.0");
        float goodRate = Float.parseFloat(user.getGoodRate());
        float badRate = Float.parseFloat(user.getBadRate());
        float rate = goodRate / (goodRate + badRate) * 100;
        return String.format("%s%%", df.format(rate));
    }

    /**
     *  this method is called to calculate the distance between two locations
     * @param origin
     * @param destination
     * @return
     */
    public String calculateDistance(LatLng origin, LatLng destination) {
        double distance = Math.round(SphericalUtil.computeDistanceBetween(origin, destination));
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(distance);
    }

    /**
     * this method is called to set the readable address for given textview
     * @param request
     * @param location
     * @param textView
     */
    public void setReadableAddress(Request request, LatLng location, TextView textView) {
        if (location != null) {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                String address = request.ReadableAddress(location, geocoder);
                textView.setText(address);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This the wallet add credit fragment's ok button
     * It can upload new data to database
     * @param money
     */
    @Override
    public void onOkPressedC(String money) {
        Database userBaseC = LoginActivity.db;
        User userC = LoginActivity.user;
        String Oldmoney = userC.getBalance();
        Float O = Float.parseFloat(Oldmoney);
        Float N = Float.parseFloat(money);
        Float Final = O + N;
        String newbalance = String.valueOf(Final);
        userC.setBalance(newbalance);
        userBaseC.add_new_user(userC);
        TextView balance = findViewById(R.id.balance);
        balance.setText(newbalance);
        Toast.makeText(BaseActivity.this, "Add Credit Success!" , Toast.LENGTH_SHORT).show();
    }

    /**
     * This is to update information of edit profile and upload information
     * @param FirstName
     * @param LastName
     * @param EmailAddress
     * @param HomeAddress
     * @param emergencyContact
     * @param bitmap
     */
    @Override
    public void updateInformation(String FirstName, String LastName, String EmailAddress, String HomeAddress, String emergencyContact, Bitmap bitmap) { // change the name on the profile page to the new input name
        User newUser = user;
        name = findViewById(R.id.driver_name);
        String fullName = FirstName + " " + LastName;
        name.setText(fullName);
        profilePhoto = findViewById(R.id.profile_photo);
        mybitmap = bitmap;
        if (mybitmap != null) profilePhoto.setImageBitmap(mybitmap);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://cmput301w20t05.appspot.com/");

        String username = newUser.getUsername();
        StorageReference LOAD = storageReference.child("Image").child(username+".jpg");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (mybitmap != null) {
            mybitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        }

        byte[] thumb = byteArrayOutputStream.toByteArray();
        UploadTask uploadTask = LOAD.putBytes(thumb);
        LOAD.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Uri downloadUrl = uri;
                newUser.setUri(downloadUrl.toString());
                Toast.makeText(BaseActivity.this, "Upload photo success!" , Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BaseActivity.this, "Upload photo fail! please try again", Toast.LENGTH_SHORT).show();
            }
        });

        newUser.setFirstName(FirstName); // save the changes that made by user
        newUser.setLastName(LastName);
        newUser.setEmailAddress(EmailAddress);
        newUser.setHomeAddress(HomeAddress);
        newUser.setEmergencyContact(emergencyContact);
        LoginActivity.save_user_login();
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

    @Override
    public void onOkPressed(Fragment thisFragment) {
        fragment = thisFragment;
        frameLayout.setVisibility(View.GONE);
        navigationView.getMenu().getItem(3).setChecked(true);
    }

}
