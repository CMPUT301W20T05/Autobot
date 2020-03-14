package com.example.autobot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.SphericalUtil;

import java.text.ParseException;
import java.util.Arrays;

import static android.os.AsyncTask.execute;
import static com.android.volley.VolleyLog.TAG;

/**
 * this class is the homepage activity
 */
public class HomePageActivity extends BaseActivity implements EditProfilePage.EditProfilePageListener{

    private LatLng destination;
    private LatLng origin;
    private Button HPConfirmButton, HPDirectionButton;
    public static Database db;
    private String username;
    public static User user;
    public static Request request;

    private static final String TAG = "HomePageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("Home page");
        View rootView = getLayoutInflater().inflate(R.layout.home_page, frameLayout);

        HPConfirmButton = findViewById(R.id.buttonConfirmRequest);
        HPConfirmButton.setVisibility(View.GONE);

        db = LoginActivity.db; // get database
        user = LoginActivity.user; // get User
        username = user.getUsername(); // get username
        setProfile(username); // set profile

        // Initialize the AutocompleteSupportFragment.
        // Specify the types of place data to return.
        //origin
        AutocompleteSupportFragment autocompleteFragmentOrigin = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_origin);
        //destination
        AutocompleteSupportFragment autocompleteFragmentDestination = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_destination);

        //get user infor from database
        //user = db.rebuildUser(username);

        if (autocompleteFragmentOrigin != null) {
            autocompleteFragmentOrigin.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
            autocompleteFragmentOrigin.setHint("Current Location");
            setAutocompleteSupportFragment(autocompleteFragmentOrigin);
        }

        if (autocompleteFragmentDestination != null) {
            autocompleteFragmentDestination.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
            autocompleteFragmentDestination.setHint("Destination");
            setAutocompleteSupportFragment(autocompleteFragmentDestination);
        }


        HPDirectionButton = (Button) findViewById(R.id.buttonShowDirection);
        HPDirectionButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {

                origin = getOrigin(autocompleteFragmentOrigin);
                destination = getDestination(autocompleteFragmentDestination);
                if (destination == null) {
                    Toast.makeText(HomePageActivity.this, "Please select destination", Toast.LENGTH_SHORT).show();
                }
                else {
                    //distance between two locations
                    double distance = Math.round(SphericalUtil.computeDistanceBetween(origin, destination));
                    //draw route between two locations
                    drawRoute(origin, destination);
                    HPConfirmButton.setVisibility(View.VISIBLE);
                }

            }
        });

        HPConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request = null;
                try {
                    request = new Request(user, origin, destination);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                request.setDestination(destination);
//                request.setBeginningLocation(origin);
                request.setEstimateCost(origin, destination);
                db.add_new_request(request);
                String reID = request.getRequestID();
                //next activity
                Intent intentUCurRequest = new Intent(HomePageActivity.this, UCurRequest.class);
                intentUCurRequest.putExtra("Username",user.getUsername());
                intentUCurRequest.putExtra("reid",request.getRequestID());
                startActivity(intentUCurRequest);
            }
        });

    }

    /**
     * this function gets the origin location of user request
     * the default location is the current location, but user can use search bar to choose another location
     * @param autocompleteFragmentOrigin this is the Google location search bar
     * @return origin location (Latlng)
     */
    public LatLng getOrigin(AutocompleteSupportFragment autocompleteFragmentOrigin){
        Location temp = getCurrentLocation();
        if (temp != null) {
            origin = new LatLng(temp.getLatitude(), temp.getLongitude()); //convert to latlng

            autocompleteFragmentOrigin.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                    origin = place.getLatLng();
                }

                @Override
                public void onError(@NonNull Status status) {
                    // TODO: Handle the error.
                    Log.i(TAG, "An error occurred: " + status);
                }
            });
        }
        return origin;
    }

    /**
     * this function used to get destination of user's request
     * @param autocompleteFragmentDestination this is the Google location search bar
     * @return destination location (Latlng)
     */
    public LatLng getDestination(AutocompleteSupportFragment autocompleteFragmentDestination) {
        destination = getSearchedLatLng();
        return destination;
    }

    @Override
    public void updateInformation(String FirstName, String LastName, String EmailAddress, String HomeAddress, String emergencyContact) { // change the name on the profile page to the new input name
        name = findViewById(R.id.driver_name);
        String fullName = FirstName + " " + LastName;
        name.setText(fullName);

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
}
