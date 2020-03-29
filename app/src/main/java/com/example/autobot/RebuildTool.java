package com.example.autobot;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.UploadTask;

public class RebuildTool {
    static User user_new;
    public static void rebuild_user(DocumentReference documentReference,User rider){
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                rider.setEmailAddress((String) document.get("EmailAddress"));
                rider.setFirstName((String) document.get("FirstName"));
                rider.setLastName((String) document.get("LastName"));
                double Lat = Double.valueOf((String) document.get("CurrentLocationLat"));
                Log.d("Testing",(String) document.get("CurrentLocationLat"));
                double Lnt = Double.valueOf((String) document.get("CurrentLocationLnt"));
                LatLng CurrentLocation = new LatLng(Lat, Lnt);
                rider.updateCurrentLocation(CurrentLocation);
                rider.setEmergencyContact((String) document.get("EmergencyContact"));
                rider.setHomeAddress((String) document.get("HomeAddress"));
                rider.setPassword((String) document.get("Password"));
                rider.setPhoneNumber((String) document.get("PhoneNumber"));
                rider.setStars(Double.valueOf((String) document.get("StarsRate")));
                rider.setUserType((String) document.get("Type"));
                rider.setUsername((String) document.get("Username"));
                String uri = ((String) document.get("ImageUri"));
//                                    if (uri != null) {
//                                        user.setUri(Uri.parse(uri));
//                                    }
                rider.setUri(uri);
                rider.setGoodRate((String) document.get("GoodRate"));
                rider.setBadRate((String) document.get("BadRate"));
                Log.d("Testing",rider.getUserType()+"hihih");
            }
        });
    }
}
