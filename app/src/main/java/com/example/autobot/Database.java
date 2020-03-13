package com.example.autobot;

import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.android.volley.VolleyLog.TAG;
import static java.text.SimpleDateFormat.*;

public class Database{
    protected FirebaseFirestore db;
    public CollectionReference collectionReference_user;
    public CollectionReference collectionReference_request;
    User user = new User("");


    public Database() {
        FirebaseFirestore.getInstance().clearPersistence();
        db = FirebaseFirestore.getInstance();
        // to disable clean-up.
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();

        db.setFirestoreSettings(settings);
        collectionReference_user = db.collection("users");
        collectionReference_request = db.collection("Request");
    }

    /**
     * This function is to add and edit user information and store them to firestone.
     * The primary key is username, if username not exist, will add a new user document
     * if username exist, the new information will cover the old information.
     * @param user
     */
    public void add_new_user(User user) {
        HashMap<String,String> user_data = new HashMap<>();
        user_data.put("Username", user.getUsername());
        user_data.put("FirstName", user.getFirstName());
        user_data.put("LastName", user.getLastName());
        user_data.put("EmailAddress", user.getEmailAddress());
        user_data.put("PhoneNumber", user.getPhoneNumber());
        user_data.put("StarsRate", user.getStars().toString());
        user_data.put("Type", user.getUserType());
        user_data.put("Password",user.getPassword());
        user_data.put("EmergencyContact",user.getEmergencyContact());
        user_data.put("HomeAddress",user.getHomeAddress());
        user_data.put("CurrentLocationLat",String.valueOf(user.getCurrentLocation().latitude));
        user_data.put("CurrentLocationLnt",String.valueOf(user.getCurrentLocation().longitude));
        collectionReference_user
                .document(user_data.get("Username"))
                .set(user_data)
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

    /**
     * This is for get DocumentReference from username
     * @param username
     * @return the documentReference
     */
    public DocumentReference getRef(String username) {
        return this.collectionReference_user.document(username);
    }

    /**
     * This function is to get the all information by the username
     * @param username
     * @return the User that include all information
     */

    public User rebuildUser(String username){
        user.setUsername(username);
        Query query = collectionReference_user.whereEqualTo("Username", username);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() != 0) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    user.setEmailAddress((String) document.get("EmailAddress"));
                                    user.setFirstName((String) document.get("FirstName"));
                                    user.setLastName((String) document.get("LastName"));
                                    System.out.println(document.get("CurrentLocation"));
                                    double Lat = Double.valueOf((String) document.get("CurrentLocationLat"));
                                    double Lnt = Double.valueOf((String) document.get("CurrentLocationLnt"));
                                    LatLng CurrentLocation = new LatLng(Lat, Lnt);
                                    user.updateCurrentLocation(CurrentLocation);
                                    user.setEmergencyContact((String) document.get("EmergencyContact"));
                                    user.setHomeAddress((String) document.get("HomeAddress"));
                                    user.setPassword((String) document.get("Password"));
                                    user.setPhoneNumber((String) document.get("PhoneNumber"));
                                    user.setStars(Double.valueOf((String) document.get("StarsRate")));
                                    user.setUserType((String) document.get("Type"));
                                    user.setUsername((String) document.get("Username"));
                                }
                            } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    }
                });
        return user;
    }

    /**
     * This function is store request to firebase
     * The request id is primary key that consist of Username and order time so it is unique
     * All information will auto get such as location, time
     * @param request
     */
    
    public void add_new_request(Request request){
        HashMap<String,String> request_data = new HashMap<>();
        request_data.put("Rider",request.getRider().getUsername());
        request_data.put("DestinationLat",String.valueOf(request.getDestination().latitude));
        request_data.put("DestinationLnt",String.valueOf(request.getDestination().longitude));
        request_data.put("BeginningLocationLat",String.valueOf(request.getBeginningLocation().latitude));
        request_data.put("BeginningLocationLnt",String.valueOf(request.getBeginningLocation().longitude));
        request_data.put("RequestID",request.getRequestID());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyy hh:mm:ss");
        request_data.put("SendTime",formatter.format(request.getSendDate()));
        request_data.put("AcceptTime",formatter.format(request.getAcceptTime()));
        request_data.put("ArriveTime",formatter.format(request.getArriveTime()));
        //request_data.put("CurrentLocation",)
        request_data.put("RequestStatus",request.getStatus());
        request_data.put("EstimateCost","0");
        request_data.put("Driver","");
        request_data.put("ID",request.getRequestID());

        collectionReference_request.document(request.getRequestID())
                .set(request_data)
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

    /**
     * This function is to get all information from the RequsetionID and user
     * @param RequestID
     * @param user
     * @return r is the all information of request that can be used from other class
     */
    public Request rebuildRequest(String RequestID, User user) throws ParseException {
        Request r = new Request(user);
        Query query = collectionReference_request.whereEqualTo("ID", RequestID);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() != 0) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    LatLng BeginningLocation = new LatLng(Double.valueOf((String) document.get("BeginningLocationLat")), Double.valueOf((String) document.get("BeginningLocationLnt")));
                                    r.setBeginningLocation(BeginningLocation);
                                    LatLng Destination = new LatLng(Double.valueOf((String) document.get("DestinationLat")), Double.valueOf((String) document.get("DestinationLnt")));
                                    r.setDestination(Destination);
                                    r.setDriver(rebuildUser((String) document.get("Driver")));
                                    r.setRequestID((String) document.get("RequestID"));
                                    r.setRider(rebuildUser((String) document.get("Rider")));

                                    SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyy hh:mm:ss");
                                    try {
                                        r.resetAcceptTime(formatter.parse((String) document.get("AcceptTime")));
                                        r.resetArriveTime(formatter.parse((String) document.get("ArriveTime")));
                                        r.resetSendTime(formatter.parse((String) document.get("SendTime")));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    r.resetRequestStatus((String) document.get("RequestStatus"));
                                    r.resetEstimateCost(Double.valueOf((String) document.get("EstimateCost")));
                                    r.setRequestID((String) document.get("ID"));
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    }
                });
        return r;
    }
    public void CancelRequest(String requestID){
        collectionReference_request.document(requestID)
                .delete()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data deleting failed" + e.toString());
                    }
                });

    }

    public void CancelUser(String username){
        collectionReference_user.document(username)
                .delete()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data deleting failed" + e.toString());
                    }
                });
    }












}




