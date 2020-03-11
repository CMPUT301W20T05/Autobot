package com.example.autobot;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;

import static com.android.volley.VolleyLog.TAG;

public class Database {
    protected FirebaseFirestore db;
    public static CollectionReference collectionReference_user;
    public static CollectionReference collectionReference_request;


    public Database() {
        db = FirebaseFirestore.getInstance();
        collectionReference_user = db.collection("users");
        collectionReference_request = db.collection("Request");
    }

    //    CollectionReference collectionReference_request;





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
        user_data.put("Longitude",user.getLongitude().toString());
        user_data.put("Latitude",user.getLatitude().toString());
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
    public static DocumentReference getUsername(String username) {
        return collectionReference_user.document(username);
    }


    public User rebuildUser(String username){
        User user = new User();
        collectionReference_user.document(username)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user.setEmailAddress((String) documentSnapshot.get("EmailAddress"));
                        user.setFirstName((String) documentSnapshot.get("FirstName"));
                        user.setLastName((String) documentSnapshot.get("LastName"));
                        user.setLatitude((Double) documentSnapshot.get("Latitude"));
                        user.setLongitude((Double) documentSnapshot.get("Longitude"));
                        user.setPassword((String) documentSnapshot.get("Password"));
                        user.setPhoneNumber((String) documentSnapshot.get("PhoneNUmber"));
                        user.setStars((Double)documentSnapshot.get("StarsRate"));
                        user.setUserType((String) documentSnapshot.get("Type"));
                        user.setUsername((String) documentSnapshot.get("Username"));








                    }
                });
        return user;
    }
    public void add_new_request(Request request){
        HashMap<String,String> request_data = new HashMap<>();
        request_data.put("Rider",request.getRider().getUsername());
        request_data.put("Destination",request.getDestination().toString());
        request_data.put("BeginningLoctation",request.getBeginningLocation().toString());
        request_data.put("RequestID",request.getRequestID());
        request_data.put("SendTime",request.getSendDate().toString());
        request_data.put("AcceptTime",null);
        request_data.put("ArriveTime",null);
        //request_data.put("CurrentLocation",)
        request_data.put("RequestStatus",request.getStatus());
        request_data.put("EstimateCost","0");
        request_data.put("Driver","");
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
    public Request rebuildRequest(long RequestID){
        Request r = new Request();
        collectionReference_request.document(String.valueOf(RequestID))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                       r.setBeginningLocation((LatLng)documentSnapshot.get("BeginningLocation"));
                       r.setDestination((LatLng)documentSnapshot.get("Destination"));
                       r.setDriver(rebuildUser((String) documentSnapshot.get("Driver")));
                       r.setRequestID((Long) documentSnapshot.get("RequestID"));
                       r.setRider(rebuildUser((String)documentSnapshot.get("Rider")));
                       r.resetAcceptTime((Date)documentSnapshot.get("AcceptTime"));
                       r.resetArriveTime((Date)documentSnapshot.get("ArriveTime"));
                       r.resetSendTime((Date)documentSnapshot.get("SendTime"));
                       r.resetRequestStatus((String) documentSnapshot.get("RequestStatus"));
                       r.resetEstimateCost((double)documentSnapshot.get("EstimateCost"));










                    }
                });
        return r;
    }




}
