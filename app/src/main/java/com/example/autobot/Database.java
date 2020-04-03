package com.example.autobot;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import static com.android.volley.VolleyLog.TAG;

public class Database{
    protected FirebaseFirestore db1;
    public CollectionReference collectionReference_user;
    public CollectionReference collectionReference_request;
    public CollectionReference collectionReference_payment;
    public StorageReference storageReference;
    public FirebaseStorage storage;
    private User user = new User("");
    private Request r = new Request();
    private Uri downloadUri;
    String a = "0";

    public Database() throws ParseException {
        //   mAuth = FirebaseAuth.getInstance();
        db1 = FirebaseFirestore.getInstance();
        collectionReference_user = db1.collection("users");
        collectionReference_request = db1.collection("Request");
        collectionReference_payment = db1.collection("PaymentInf");
    }

    /**
     *The function will implement when status changed in database.
     * @param requestID the unique id of request
     * @param requeststatus what status u are looking for
     * @param start which textView u want to change
     * @param intent the changed content of textView
     */
    public void NotifyStatusChange(String requestID, String requeststatus, Activity start, Intent intent){
        DocumentReference ref = collectionReference_request.document(requestID);
        ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    Log.w(TAG, "listen:error",e);
                    return;
                }
                if(documentSnapshot != null&&documentSnapshot.exists()){
                    if (documentSnapshot.getString("RequestStatus").equals(requeststatus)){
                        Log.d(TAG,"Current status: "+ requeststatus);
                        start.finish();
                        start.startActivity(intent);
                    }
                }

            }
        });
    }

    /**
     * This function is to change interface textview if database has specific change
     * @param requestID the unique id of request
     * @param requeststatus what status u are looking for
     * @param textView which textView u want to change
     * @param text the changed content of textView
     */
    public void NotifyStatusChangeEditText(String requestID, String requeststatus, TextView textView, String text){
        DocumentReference ref = collectionReference_request.document(requestID);
        ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    Log.w(TAG, "listen:error",e);
                    return;
                }
                if(documentSnapshot != null&&documentSnapshot.exists()){
                    if (documentSnapshot.getString("RequestStatus").equals(requeststatus)){
                        Log.d(TAG,"Current status: "+ requeststatus);
                        textView.setText(text);
                    }
                }

            }
        });
    }

    /**
     *
     * @param requestID the unique id of request
     * @param requeststatus what status u are looking for
     * @param button
     * @param visible
     */

    public void NotifyStatusChangeButton(String requestID, String requeststatus, Button button, boolean visible){
        DocumentReference ref = collectionReference_request.document(requestID);
        ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    Log.w(TAG, "listen:error",e);
                    return;
                }
                if(documentSnapshot != null&&documentSnapshot.exists()){
                    if (documentSnapshot.getString("RequestStatus").equals(requeststatus)){
                        Log.d(TAG,"Current status: "+ requeststatus);
                        if (visible) {
                            button.setVisibility(View.VISIBLE);
                        }
                        else {
                            button.setVisibility(View.GONE);
                        }
                    }
                }

            }
        });
    }

    /**
     * this function can return current location for user.
     * @param user
     * @return
     */
    public LatLng getCurrentLocation(User user){
        final LatLng[] currentLocation = new LatLng[1];
        this.collectionReference_user.document(user.getUsername())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        double Lat = Double.valueOf((String)documentSnapshot.get("CurrentLocationLat"));
                        double Lnt = Double.valueOf((String)documentSnapshot.get("CurrentLocationLnt"));
                        currentLocation[0] = new LatLng(Lat,Lnt);
                    }
                });
        return currentLocation[0];
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
        user_data.put("ImageUri",user.getUri());
        user_data.put("GoodRate",user.getGoodRate());
        user_data.put("BadRate",user.getBadRate());
        user_data.put("Balance",user.getBalance());
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

    public Uri upload(Bitmap mybitmap,String username){
        StorageReference LOAD = storageReference.child("Image").child(username+".jpg");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        mybitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] thumb = byteArrayOutputStream.toByteArray();
        UploadTask uploadTask = LOAD.putBytes(thumb);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return LOAD.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUri = task.getResult();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

        if (downloadUri == null){
            downloadUri = Uri.parse("");
        }

        return downloadUri;
    }



    /**
     * This function is to get the all information by the username
     * @param username
     * @return the User that include all information
     */

    public User rebuildUser(String username){
        FirebaseFirestore db2 = FirebaseFirestore.getInstance();
        collectionReference_user = db2.collection("users");
        user.setUsername(username);
        Query query = db2.collection("users").whereEqualTo("Username", username);
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
                                    String uri = ((String) document.get("ImageUri"));
//                                    if (uri != null) {
//                                        user.setUri(Uri.parse(uri));
//                                    }
                                    user.setUri(uri);
                                    user.setGoodRate((String) document.get("GoodRate"));
                                    user.setBadRate((String) document.get("BadRate"));
                                    user.setBalance((String) document.get("Balance"));
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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        request_data.put("SendTime",formatter.format(request.getSendDate()));
        request_data.put("AcceptTime",formatter.format(request.getAcceptTime()));
        request_data.put("ArriveTime",formatter.format(request.getArriveTime()));
        //request_data.put("CurrentLocation",)
        request_data.put("RequestStatus",request.getStatus());
        request_data.put("EstimateCost",String.valueOf(request.getEstimateCost()));
        request_data.put("Driver","");
        request_data.put("ID",request.getRequestID());
        request_data.put("Cost","0.0");
        request_data.put("Tips","0.0");

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
     *  This function is to get all information from the RequsetionID and user
     *      * @param RequestID
     *      * @param user
     *      * @return r is the all information of request that can be used from other class
     */

    public Request rebuildRequest(String RequestID, User user) throws ParseException {
        FirebaseFirestore db3 = FirebaseFirestore.getInstance();
        collectionReference_request = db3.collection("Request");
        r.setRider(user);
        db3.collection("Request")
                .whereEqualTo("ID",RequestID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    LatLng BeginningLocation = new LatLng(Double.valueOf((String) document.getString("BeginningLocationLat")), Double.parseDouble((String) document.getString("BeginningLocationLnt")));
                                    r.setBeginningLocation(BeginningLocation);
                                    LatLng Destination = new LatLng(Double.valueOf((String) document.getString("DestinationLat")), Double.valueOf((String) document.getString("DestinationLnt")));
                                    r.setDestination(Destination);
                                    r.setDriver(rebuildUser((String) document.getString("Driver")));
                                    r.setRequestID((String) document.getString("RequestID"));
                                    r.setRider(rebuildUser((String) document.getString("Rider")));

                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    try {
                                        r.setAcceptTime(formatter.parse((String) document.getString("AcceptTime")));
                                        r.setArriveTime(formatter.parse((String) document.getString("ArriveTime")));
                                        r.resetSendTime(formatter.parse((String) document.getString("SendTime")));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    r.reset_Request_Status((String) document.getString("RequestStatus"));
                                    r.resetEstimateCost(Double.valueOf((String) document.getString("EstimateCost")));
                                    r.setRequestID((String) document.getString("ID"));
                                    r.setCost(Double.valueOf(document.getString("Cost")));
                                    r.setTips(Double.valueOf(document.getString("Tips")));
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            }

                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

        return r;
    }

    /**
     * Cancel the request from database.
     * @param requestID
     */

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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HashMap<String,String> payment_data = new HashMap<>();
        payment_data.put("CardNumber",PayInfCard.getCardNumber().toString() );
        payment_data.put("HoldName", PayInfCard.getHoldName());
        payment_data.put("ExpireDate", formatter.format(PayInfCard.getExpireDate()));
        payment_data.put("BillingAddress",PayInfCard.getBillingAddress());
        payment_data.put("PostalCode", PayInfCard.getPostalCode());
        payment_data.put("CardType", PayInfCard.getCardLogo()+"");
        payment_data.put("UserName", PayInfCard.getUserName());
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

    /**
     * change request status in database.
     * @param request
     */

    public void ChangeRequestStatus(Request request) {
        HashMap<String, Object> requestChanged = new HashMap<>();
        requestChanged.put("RequestStatus", request.getStatus());
        requestChanged.put("Driver", request.getDriver().getUsername());
        collectionReference_request.document(request.getRequestID())
                .update(requestChanged)
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