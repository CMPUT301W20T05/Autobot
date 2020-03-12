package com.example.autobot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.Inflater;

public class DriverhomeActivity extends BaseActivity implements ActiverequestsFragment.OnBackPressed ,EditProfilePage.EditProfilePageListener,ShowSelectedActiveRequestFragment.ButtonPress{
    private User user;
    String phone_num;
    FragmentManager active_request_fm;
    Button confirm;
    LatLng origin;
    View header;
    ArrayList<Request> requests_list;
    View rootView;
    private static final String TAG = "DriverSearchActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //View rootView = getLayoutInflater().inflate(..., frameLayout);
        setTitle(phone_num);
        //testing
        user = new User();
        user.setFirstName("jc");
        user.setLastName("lyu");
        update_navigation_view(user);
        //--------------------------------
        rootView = getLayoutInflater().inflate(R.layout.home_page, frameLayout);
        //hide that useless bar
        rootView.findViewById(R.id.autocomplete_destination).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.buttonConfirmLocation).setVisibility(View.INVISIBLE);
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
                        //get all the satisfied active requests
                        requests_list = new ArrayList<Request>();
                        //rise the show active requests fragment, manage the fragments activities----------------------
                        active_request_fm = getSupportFragmentManager();
                        Fragment fragment = new ActiverequestsFragment(requests_list);
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
        load_user();

    }

    public void load_user(){
        Intent intent = getIntent();
        phone_num = intent.getExtras().getString("phone number");
    }

    public void update_navigation_view(User user){
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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()) {
            case R.id.my_request_history:
                Log.d("check","1");
                break;
            case R.id.settings:
                Log.d("check","2");
                break;
            case R.id.payment_information:
                getSupportFragmentManager().beginTransaction().replace(R.id.myMap, new PaymentInformationFragment()).commit();
                break;
            case R.id.log_out:
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);startActivity(intent);
                break;
            case R.id.edit_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EditProfilePage()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
    @Override
    //--------------listener of show detail of active request fragment
    public void confirm_request(Request request){
        //update request in the database
        request.UpdateStatus(1);
        //start new activity
        Bundle bundle = new Bundle();
        Intent intent = new Intent(DriverhomeActivity.this,DriverIsOnTheWayActivity.class);
        bundle.putSerializable("accepted request",request);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void back_press(){
        active_request_fm.popBackStack();
    }
    //-----------------------------------------
    @Override
    public void hide() {
              rootView.findViewById(R.id.autocomplete_origin).setVisibility(View.VISIBLE);
    }
    @Override
    //show the detail of the selected active request
    public void show_detail(ShowSelectedActiveRequestFragment fragment) {
        active_request_fm.beginTransaction().add(R.id.myMap,fragment).addToBackStack(null).commit();
    }

    //for edit profile info
    @Override
    public void updateName(String Name) {
        TextView user_name = header.findViewById(R.id.driver_name);
        user_name.setText(Name);
    }

}
