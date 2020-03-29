package com.example.autobot;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firestore.v1.StructuredQuery;
import com.google.maps.android.SphericalUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

import io.paperdb.Paper;

/**
 * this class is the homepage activity
 */
public class HomePageActivity extends BaseActivity implements EditProfilePage.EditProfilePageListener {

    private LatLng destination;
    private LatLng origin;
    private Button HPConfirmButton, HPDirectionButton;
    public Database db;
    private String username;
    public static User user;
    public static Request request;
    private String reID;
    private static final int REQUEST_PHONE_CALL = 101;
    public StorageReference storageReference;
    public FirebaseStorage storage;
    Uri downloadUri;
    private static final String TAG = "HomePageActivity";

    private String model;
    private double addPrice;
    private boolean clicked = false;

    DecimalFormat df = new DecimalFormat("0.00");

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
                HPDirectionButton.setVisibility(View.GONE); //make sure only generate one current request

                //for model choosing
                final BottomSheetDialog uCurRequestDialog = new BottomSheetDialog(HomePageActivity.this);
                uCurRequestDialog.setContentView(R.layout.current_request_of_user);
                uCurRequestDialog.setCancelable(false);

                //1: for uCurRequestDialog
                Button CurRequestConfirm = uCurRequestDialog.findViewById(R.id.Cur_Request_confirm);
                TextView EstimatedFare = uCurRequestDialog.findViewById(R.id.estimatedFare);
                Spinner modelTochoose = uCurRequestDialog.findViewById(R.id.spinnerCarModel);

                //for wait for driver accept
                //RiderBottomSheetFragment bottomSheetFragment = new RiderBottomSheetFragment();
                //bottomSheetFragment.show(getSupportFragmentManager(), "RiderBottomSheet");
                final BottomSheetDialog dialog = new BottomSheetDialog(HomePageActivity.this);
                dialog.setContentView(R.layout.rider_bottom_sheet);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(true);

                TextView startLocation = dialog.findViewById(R.id.origin_loc);
                TextView endLocation = dialog.findViewById(R.id.Destination);
                TextView approDistance = dialog.findViewById(R.id.Appro_distance);
                TextView approPrice = dialog.findViewById(R.id.Appro_price);
                Button cancelButton = (Button) dialog.findViewById(R.id.cancel_order);

                if (!clicked) {
                    clicked = true;
                    HPConfirmButton.setText("View request");
                    request = null;
                    try {
                        request = new Request(user, origin, destination);
//                        SharedPreferences requestPref = getApplicationContext().getSharedPreferences("Request", 0); // 0 - for private mode
//                        Offline.UploadRequest(requestPref, request);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    request.setEstimateCost(origin, destination);
                    db.add_new_request(request);
                    reID = request.getRequestID();
                    //db.NotifyStatusChange(reID,"Request Accepted",HomePageActivity.this);

                    //calculate estimated fare
                    double estimateFare = request.getEstimateCost();
                    if (EstimatedFare != null) {
                        EstimatedFare.setText(df.format(estimateFare));
                    }

                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(HomePageActivity.this, R.array.Models, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    modelTochoose.setAdapter(adapter);
                    //choose model
                    modelTochoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            model = parent.getItemAtPosition(position).toString();
                            adapter.notifyDataSetChanged();

                            if ("Normal".equals(model)){
                                addPrice = 0;
                            }else if("Pleasure".equals(model)){
                                addPrice = 5;
                            }else{
                                addPrice = 10;
                            }

                            EstimatedFare.setText((df.format(estimateFare + addPrice)));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            model = "Normal";
                            addPrice = 0;
                        }
                    });

                    double estimateAddModelFee = request.EstimateAddModelFee(addPrice);
                    if (EstimatedFare != null) {
                        EstimatedFare.setText(String.valueOf(df.format(estimateAddModelFee)));
                    }

                    CurRequestConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //add tips
                            EditText editTextTip = uCurRequestDialog.findViewById(R.id.addTip);
                            Double tips = 0.0;
                            double totalFare = addPrice + estimateFare;
                            Editable temp = editTextTip.getText();
                            if (temp!=null) {
                                tips = Double.valueOf(String.valueOf(temp));
                                request.resetTips(tips, db);
                                totalFare += tips;
                            }
                            request.resetCost(totalFare, db);

                            //finish current activity
                            uCurRequestDialog.dismiss();
                            //wait driver to accept
                            Intent intent = new Intent(HomePageActivity.this, DriverIsOnTheWayActivity.class);
                            db.NotifyStatusChange(reID, "Driver Accepted", HomePageActivity.this, intent);

                            //set price have to go here to display
                            approPrice.setText(df.format(request.getCost()));
                            dialog.show();
                        }
                    });
                    uCurRequestDialog.show();

                    //2: bottom sheet for waiting driver to accept
                    startLocation = dialog.findViewById(R.id.origin_loc);
                    endLocation = dialog.findViewById(R.id.Destination);
                    approDistance = dialog.findViewById(R.id.Appro_distance);

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
                    //set distance for dialog
                    approDistance.setText(calculateDistance(origin, destination));

                    cancelButton = (Button) dialog.findViewById(R.id.cancel_order);
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
                else {
                    //2: bottom sheet for waiting driver to accept
                    String reID = request.getRequestID();

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
                    //set distance for dialog
                    approDistance.setText(calculateDistance(origin, destination));
                    approPrice.setText(df.format(request.getCost()));

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

                    dialog.show();
                }

            }
        });
    }

    /**
     * two time back pressed will return to home window
     */
    /*private long firstPressedTime;
    private Toast backToast;
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis() - firstPressedTime<2000){
            backToast.cancel();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }else{
            backToast = Toast.makeText(HomePageActivity.this,"Press another time to Quit",Toast.LENGTH_SHORT);
            backToast.show();
            firstPressedTime = System.currentTimeMillis();
        }
    }*/

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
                Toast.makeText(HomePageActivity.this, "Upload success! URL - " + downloadUrl.toString() , Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomePageActivity.this, "Upload fail! please try again", Toast.LENGTH_SHORT).show();
            }
        });

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
