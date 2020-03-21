package com.example.autobot;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.maps.android.SphericalUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Locale;

/**
 * this class is the homepage activity
 */
public class HomePageActivity extends BaseActivity implements EditProfilePage.EditProfilePageListener {

    private LatLng destination;
    private LatLng origin;
    private Button HPConfirmButton, HPDirectionButton;
    public  Database db;
    private String username;
    public static User user;
    public static Request request;

    private static final int REQUEST_PHONE_CALL = 101;

    private static final String TAG = "HomePageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("Home page");
        View rootView = getLayoutInflater().inflate(R.layout.home_page, frameLayout);

        HPConfirmButton = findViewById(R.id.buttonConfirmRequest);
        HPConfirmButton.setVisibility(View.GONE);

        db = MainActivity.db; // get database
        user = LoginActivity.user; // get User
        username = user.getUsername(); // get username

        setProfile(username,db); // set profile

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
                request.setEstimateCost(origin, destination);
                db.add_new_request(request);
                String reID = request.getRequestID();
                //db.NotifyStatusChange(reID,"Request Accepted",HomePageActivity.this);

                //for model choosing
                final BottomSheetDialog uCurRequestDialog = new BottomSheetDialog(HomePageActivity.this);
                uCurRequestDialog.setContentView(R.layout.current_request_of_user);
                uCurRequestDialog.setCancelable(false);

                //for wait for driver accept
                //RiderBottomSheetFragment bottomSheetFragment = new RiderBottomSheetFragment();
                //bottomSheetFragment.show(getSupportFragmentManager(), "RiderBottomSheet");
                final BottomSheetDialog dialog = new BottomSheetDialog(HomePageActivity.this);
                dialog.setContentView(R.layout.rider_bottom_sheet);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(false);


                //1: for uCurRequestDialog
                Button CurRequestConfirm = uCurRequestDialog.findViewById(R.id.Cur_Request_confirm);
                TextView EstimatedFare = uCurRequestDialog.findViewById(R.id.estimatedFare);
                Spinner modelTochoose = uCurRequestDialog.findViewById(R.id.spinnerCarModel);

                //calculate estimated fare
                DecimalFormat df = new DecimalFormat("0.00");
                double estimateFare = request.getEstimateCost();
                if (EstimatedFare != null) {
                    EstimatedFare.setText(String.valueOf(df.format(estimateFare)));
                }

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(HomePageActivity.this, R.array.Models, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                modelTochoose.setAdapter(adapter);

                CurRequestConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //choose model

                        //finish current activity
                        uCurRequestDialog.dismiss();
                        //wait driver to accept
                        Intent intent = new Intent(HomePageActivity.this, DriverIsOnTheWayActivity.class);
                        db.NotifyStatusChange(reID, "Request Accepted", HomePageActivity.this, intent);
                        dialog.show();
                    }
                });
                uCurRequestDialog.show();

                //2: bottom sheet for waiting driver to accept
                TextView driverCondition = dialog.findViewById(R.id.driver_condition);
                TextView startLocation = dialog.findViewById(R.id.origin_loc);
                TextView endLocation = dialog.findViewById(R.id.Destination);
                TextView approDistance = dialog.findViewById(R.id.Appro_distance);
                TextView approPrice = dialog.findViewById(R.id.Appro_price);

                //set location for dialog
                LatLng destination = request.getDestination();
                LatLng origin = request.getBeginningLocation();
                if (destination != null && origin != null) {
                    Geocoder geocoder = new Geocoder(HomePageActivity.this, Locale.getDefault());
                    try {
                        String oaddress = request.ReadableAddress(origin, geocoder);
                        String daddress = request.ReadableAddress(destination, geocoder);
                        startLocation.setText(oaddress);
                        endLocation.setText(daddress);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //set distance and price for dialog
                //distance between two locations
                double distance = Math.round(SphericalUtil.computeDistanceBetween(origin, destination));
                //DecimalFormat df = new DecimalFormat("0.00");
                approDistance.setText(df.format(distance));
                approPrice.setText(df.format(request.getEstimateCost()));

                //change driver condition when needed
                //driverCondition.setText("");

//                Button seeProfile = dialog.findViewById(R.id.see_profile);
//                seeProfile.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        view = LayoutInflater.from(HomePageActivity.this).inflate(R.layout.profile_viewer, null);
//
//                        TextView fname = view.findViewById(R.id.FirstName);
//                        TextView lname = view.findViewById(R.id.LastName);
//                        TextView pnumber = view.findViewById(R.id.PhoneNumber);
//                        TextView email = view.findViewById(R.id.EmailAddress);
//                        //should be set as driver's infor
//                        fname.setText(user.getFirstName());
//                        lname.setText(user.getLastName());
//                        pnumber.setText(user.getPhoneNumber());
//                        email.setText(user.getEmailAddress());
//
//                        final AlertDialog.Builder alert = new AlertDialog.Builder(HomePageActivity.this);
//                        alert.setView(view)
//                                .setTitle("Details")
//                                .setNegativeButton("Close",null);
//                        alert.show();
//                    }
//                });

                Button cancelButton = (Button) dialog.findViewById(R.id.cancel_order);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //pop out dialog
                        final AlertDialog.Builder alert = new AlertDialog.Builder(HomePageActivity.this);
                        alert.setTitle("Cancel Order");
                        alert.setMessage("Are you sure you wish to cancel current request?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //delete current request
                                        db.CancelRequest(reID);
                                        //close dialog
                                        dialog.dismiss();
                                        //set homepage to initial state
                                        recreateActivity();
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

//                ImageButton phoneButton = (ImageButton) dialog.findViewById(R.id.phoneButton);
//                phoneButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
//                        callIntent.setData(Uri.parse("tel:" + "123"));//change the number.
//                        if (ActivityCompat.checkSelfPermission(HomePageActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                            ActivityCompat.requestPermissions(HomePageActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
//                            Toast.makeText(HomePageActivity.this, "No permission for calling", Toast.LENGTH_LONG).show();
//                        } else {
//                            startActivity(callIntent);
//                        }
//                    }
//                });

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
    public void updateInformation(String FirstName, String LastName, String EmailAddress, String HomeAddress, String emergencyContact, Uri imageUri) { // change the name on the profile page to the new input name
        name = findViewById(R.id.driver_name);
        String fullName = FirstName + " " + LastName;
        name.setText(fullName);
        profilePhoto = findViewById(R.id.profile_photo);
        try {
            if (imageUri != Uri.parse("http://www.google.com")) {
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                mybitmap = BitmapFactory.decodeStream(imageStream);
                profilePhoto.setImageBitmap(mybitmap);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //Toast.makeText(HomePageActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

        User newUser = user;
        newUser.setFirstName(FirstName); // save the changes that made by user
        newUser.setLastName(LastName);
        newUser.setEmailAddress(EmailAddress);
        newUser.setHomeAddress(HomeAddress);
        newUser.setEmergencyContact(emergencyContact);
        if (imageUri != Uri.parse("http://www.google.com")) newUser.setUri(imageUri.toString());
        db.add_new_user(newUser);

    }
    @Override
    public String getUsername() {
        return username;
    }

    public void recreateActivity() {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);

        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public Bitmap getBitmap(){
        return mybitmap;
    }
}
