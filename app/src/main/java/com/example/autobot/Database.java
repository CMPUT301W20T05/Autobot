package com.example.autobot;

import android.util.Log;

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
import java.util.HashMap;

import static com.android.volley.VolleyLog.TAG;

public class Database{
    protected FirebaseFirestore db;
    public CollectionReference collectionReference_user;
    public CollectionReference collectionReference_request;
    public CollectionReference collectionReference_payment;
    User user = new User("");
    Request r = new Request();


    public Database() throws ParseException {
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
        collectionReference_payment = db.collection("PaymentInf");
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
        request_data.put("EstimateCost",String.valueOf(request.getEstimateCost()));
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
        r.setRider(user);
        collectionReference_request
                .document(RequestID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override

                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            LatLng BeginningLocation = new LatLng(Double.valueOf((String) documentSnapshot.getString("BeginningLocationLat")), Double.parseDouble((String) documentSnapshot.getString("BeginningLocationLnt")));
                            r.setBeginningLocation(BeginningLocation);
                            LatLng Destination = new LatLng(Double.valueOf((String) documentSnapshot.getString("DestinationLat")), Double.valueOf((String) documentSnapshot.getString("DestinationLnt")));
                            r.setDestination(Destination);
                            r.setDriver(rebuildUser((String) documentSnapshot.getString("Driver")));
                            r.setRequestID((String) documentSnapshot.getString("RequestID"));
                            r.setRider(rebuildUser((String) documentSnapshot.getString("Rider")));

                            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyy hh:mm:ss");
                            try {
                                r.resetAcceptTime(formatter.parse((String) documentSnapshot.getString("AcceptTime")));
                                r.resetArriveTime(formatter.parse((String) documentSnapshot.getString("ArriveTime")));
                                r.resetSendTime(formatter.parse((String) documentSnapshot.getString("SendTime")));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            r.resetRequestStatus((String) documentSnapshot.getString("RequestStatus"));
                            r.resetEstimateCost(Double.valueOf((String) documentSnapshot.getString("EstimateCost")));
                            r.setRequestID((String) documentSnapshot.getString("ID"));
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

    /**
     * Add information about payment card to firebase
     * @param PayInfCard
     */
    public void add_new_Payment(PaymentCard PayInfCard) {
        HashMap<String,String> payment_data = new HashMap<>();
        payment_data.put("CardNumber",PayInfCard.getCardNumber().toString() );
        payment_data.put("HoldName", PayInfCard.getHoldName());
        payment_data.put("ExpireDate", PayInfCard.getExpireDate().toString());
        payment_data.put("BillingAddress",PayInfCard.getBillingAddress());
        payment_data.put("PostalCode", PayInfCard.getPostalCode());
        collectionReference_payment
                .document(payment_data.get("CardNumber"))
                .set(payment_data)
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

   //public String StatusChangeNotify










}




