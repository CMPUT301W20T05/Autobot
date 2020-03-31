package com.example.autobot;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import io.paperdb.Paper;

/**
 * this class is the homepage activity
 */
public class HomePageActivity extends BaseActivity {

    private LatLng destination;
    private LatLng origin;
    private Button HPConfirmButton, HPDirectionButton;
    public Database db;
    private String username;
    public static User user;
    public static Request request;
    private String reID;
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
            setAutocompleteSupportFragment(autocompleteFragmentOrigin, "curr");
        }

        if (autocompleteFragmentDestination != null) {
            autocompleteFragmentDestination.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
            autocompleteFragmentDestination.setHint("Destination");
            setAutocompleteSupportFragment(autocompleteFragmentDestination, "dest");
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
                    //save current request to local
                    LoginActivity.save_request(request);

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
                        EstimatedFare.setText(df.format(estimateAddModelFee));
                    }

                    CurRequestConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //add tips
                            EditText editTextTip = uCurRequestDialog.findViewById(R.id.addTip);
                            Double tips = 0.0;
                            double totalFare = addPrice + estimateFare;
                            String temp = editTextTip.getText().toString();
                            if (!temp.equals("")){
                                tips = Double.valueOf(temp);
                            }
                            request.resetTips(tips, db);
                            totalFare += tips;
                            //check if affordable
                            if (Double.parseDouble(user.getBalance()) >= totalFare){
                                BigDecimal bg = new BigDecimal(totalFare);
                                request.resetCost(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(), db);

                                //finish current activity
                                uCurRequestDialog.dismiss();
                                //wait driver to accept
                                Intent intent = new Intent(HomePageActivity.this, DriverIsOnTheWayActivity.class);
                                db.NotifyStatusChange(reID, "Driver Accepted", HomePageActivity.this, intent);

                                //set price have to go here to display
                                approPrice.setText(df.format(request.getCost()));
                                dialog.show();
                            }
                            else{
                                Toast.makeText(HomePageActivity.this,"Sorry the balance in wallet bot enough.Please add credit!",Toast.LENGTH_SHORT).show();
                                db.CancelRequest(reID);
                                recreateActivity();
                            }
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
                                            Offline.clear_request(LoginActivity.sharedPreferences);
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
                                            Offline.clear_request(LoginActivity.sharedPreferences);
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
                LoginActivity.save_request(request);
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
        Location loc = getCurrentLocation();
        origin = new LatLng(loc.getLatitude(), loc.getLongitude()); //convert to latlng
        LatLng temp = getPickupLoc();
        if (temp != null) {
            origin = temp;
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

    /**
     * recreate homepage activity
     */
    public void recreateActivity() {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);

        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        //check if there is a unfinished request
        Request localRequest = LoginActivity.load_request(HomePageActivity.this);
        if (localRequest != null) {
            notificationManager = NotificationManagerCompat.from(this);
            sendOnChannel("You have a current incomplete request. Please check your request history.");

            @SuppressLint("InflateParams") View view = LayoutInflater.from(HomePageActivity.this).inflate(R.layout.current_request, null);
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
            User driver = localRequest.getDriver();
            if (driver != null) {
                try{
                    AsnycProcess mytask = new AsnycProcess(driver.getUri(), avatar);
                    mytask.execute();
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

            final AlertDialog.Builder alert = new AlertDialog.Builder(HomePageActivity.this);
            alert.setView(view)
                    .setTitle("Request Details")
                    .setNegativeButton("Close",null);
            alert.show();
        }

        request = localRequest;
        Offline.clear_request(LoginActivity.sharedPreferences);
    }

    @Override
    public void onBackPressed() {
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
        } else if (fragment == null) {
            if (System.currentTimeMillis() - firstPressedTime < 2000) {
                backToast.cancel();
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            } else {
                backToast = Toast.makeText(HomePageActivity.this, "Press another time to Quit", Toast.LENGTH_SHORT);
                backToast.show();
                firstPressedTime = System.currentTimeMillis();
            }
        } else {
            ft.remove(fragment).commit();
            fragment = null;
            setTitle("Home Page");
            onResume();
            frameLayout.setVisibility(View.VISIBLE);
            frameLayout.invalidate();
        }
    }

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
