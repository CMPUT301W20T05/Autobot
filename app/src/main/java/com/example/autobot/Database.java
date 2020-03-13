package com.example.autobot;

import android.util.Log;
import android.widget.TextView;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;

import static com.android.volley.VolleyLog.TAG;

public class Database {
    protected FirebaseFirestore db;
    public CollectionReference collectionReference_user;
    public CollectionReference collectionReference_request;
    private User user;


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
    public DocumentReference getRef(String username) {
        return this.collectionReference_user.document(username);
    }


    public User rebuildUser(String username){
        collectionReference_user
                .whereEqualTo("Username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            user = new User();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //user = document.toObject(User.class);
                                user.setEmailAddress((String) document.get("EmailAddress"));
                                user.setFirstName((String) document.get("FirstName"));
                                user.setLastName((String) document.get("LastName"));
                                System.out.println(document.get("CurrentLocation"));
                                double Lat = Double.valueOf((String) document.get("CurrentLocationLat"));
                                double Lnt = Double.valueOf((String) document.get("CurrentLocationLnt"));
                                LatLng CurrentLocation = new LatLng(Lat, Lnt);
                                user.updateCurrentLocation(CurrentLocation);
                                user.setPassword((String) document.get("Password"));
                                user.setPhoneNumber((String) document.get("PhoneNUmber"));
                                user.setStars(Double.valueOf((String) document.get("StarsRate")));
                                user.setUserType((String) document.get("Type"));
                                user.setUsername((String) document.get("Username"));
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return user;
    }
    public void add_new_request(Request request){
        HashMap<String,String> request_data = new HashMap<>();
        request_data.put("Rider",request.getRider().getUsername());
        request_data.put("DestinationLat",String.valueOf(request.getDestination().latitude));
        request_data.put("DestinationLnt",String.valueOf(request.getDestination().longitude));

        request_data.put("BeginningLocationLat",String.valueOf(request.getBeginningLocation().latitude));
        request_data.put("BeginningLocationLnt",String.valueOf(request.getBeginningLocation().longitude));

        request_data.put("RequestID",request.getRequestID());
        request_data.put("SendTime",request.getSendDate().toString());
        request_data.put("AcceptTime",null);
        request_data.put("ArriveTime",null);
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
    public Request rebuildRequest(long RequestID, User user){
        Request r = new Request(user);
        collectionReference_request.document(String.valueOf(RequestID))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        LatLng BeginningLocation = new LatLng(Double.valueOf((String)documentSnapshot.get("BeginningLocationLat")),Double.valueOf((String)documentSnapshot.get("BeginningLocationLnt")));
                       r.setBeginningLocation(BeginningLocation);
                        LatLng Destination = new LatLng(Double.valueOf((String)documentSnapshot.get("DestinationLat")),Double.valueOf((String)documentSnapshot.get("DestinationLnt")));
                       r.setDestination(Destination);
                       r.setDriver(rebuildUser((String) documentSnapshot.get("Driver")));
                       r.setRequestID((String) documentSnapshot.get("RequestID"));
                       r.setRider(rebuildUser((String)documentSnapshot.get("Rider")));
                       r.resetAcceptTime((Date)documentSnapshot.get("AcceptTime"));
                       r.resetArriveTime((Date)documentSnapshot.get("ArriveTime"));
                       r.resetSendTime((Date)documentSnapshot.get("SendTime"));
                       r.resetRequestStatus((String) documentSnapshot.get("RequestStatus"));
                       r.resetEstimateCost(Double.valueOf((String)documentSnapshot.get("EstimateCost")));
                       r.setRequestID((String)documentSnapshot.get("ID"));










                    }
                });
        return r;
    }












}




