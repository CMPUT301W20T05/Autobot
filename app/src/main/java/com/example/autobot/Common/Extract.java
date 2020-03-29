package com.example.autobot.Common;

import com.example.autobot.Request;
import com.example.autobot.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Extract {
    public static User RebuildUser(String username){
        User user = new User(username);
        FirebaseFirestore dbuser = FirebaseFirestore.getInstance();
        CollectionReference reference = dbuser.collection("users");
        reference.document(username)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user.setEmailAddress((String) documentSnapshot.get("EmailAddress"));
                        user.setFirstName((String) documentSnapshot.get("FirstName"));
                        user.setLastName((String) documentSnapshot.get("LastName"));
                        System.out.println(documentSnapshot.get("CurrentLocation"));
                        double Lat = Double.valueOf((String) documentSnapshot.get("CurrentLocationLat"));
                        double Lnt = Double.valueOf((String) documentSnapshot.get("CurrentLocationLnt"));
                        LatLng CurrentLocation = new LatLng(Lat, Lnt);
                        user.updateCurrentLocation(CurrentLocation);
                        user.setEmergencyContact((String) documentSnapshot.get("EmergencyContact"));
                        user.setHomeAddress((String) documentSnapshot.get("HomeAddress"));
                        user.setPassword((String) documentSnapshot.get("Password"));
                        user.setPhoneNumber((String) documentSnapshot.get("PhoneNumber"));
                        user.setStars(Double.valueOf((String) documentSnapshot.get("StarsRate")));
                        user.setUserType((String) documentSnapshot.get("Type"));
                        user.setUsername((String) documentSnapshot.get("Username"));
                        String uri = ((String) documentSnapshot.get("ImageUri"));
//                                    if (uri != null) {
//                                        user.setUri(Uri.parse(uri));
//                                    }
                        user.setUri(uri);
                        user.setGoodRate((String) documentSnapshot.get("GoodRate"));
                        user.setBadRate((String) documentSnapshot.get("BadRate"));
                    }
                });
        return user;
    }
    public static Request RebuildRequest(String requestID) throws ParseException {
        Request r = new Request();
        FirebaseFirestore dbrequest = FirebaseFirestore.getInstance();
        CollectionReference reference = dbrequest.collection("Request");
        reference.document(requestID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override

            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    LatLng BeginningLocation = new LatLng(Double.valueOf((String) documentSnapshot.getString("BeginningLocationLat")), Double.parseDouble((String) documentSnapshot.getString("BeginningLocationLnt")));
                    r.setBeginningLocation(BeginningLocation);
                    LatLng Destination = new LatLng(Double.valueOf((String) documentSnapshot.getString("DestinationLat")), Double.valueOf((String) documentSnapshot.getString("DestinationLnt")));
                    r.setDestination(Destination);
                    r.setDriver(RebuildUser((String) documentSnapshot.getString("Driver")));
                    r.setRequestID((String) documentSnapshot.getString("RequestID"));
                    r.setRider(RebuildUser((String) documentSnapshot.getString("Rider")));

                    SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyy hh:mm:ss");
                    try {
                        r.resetAcceptTime(formatter.parse((String) documentSnapshot.getString("AcceptTime")));
                        r.resetArriveTime(formatter.parse((String) documentSnapshot.getString("ArriveTime")));
                        r.resetSendTime(formatter.parse((String) documentSnapshot.getString("SendTime")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    r.reset_Request_Status((String) documentSnapshot.getString("RequestStatus"));
                    r.resetEstimateCost(Double.valueOf((String) documentSnapshot.getString("EstimateCost")));
                    r.setRequestID((String) documentSnapshot.getString("ID"));
                    r.setCost(Double.valueOf(documentSnapshot.getString("Cost")));
                    r.setTips(Double.valueOf(documentSnapshot.getString("Tips")));
                }

            }
        });
        return r;


    }

}
