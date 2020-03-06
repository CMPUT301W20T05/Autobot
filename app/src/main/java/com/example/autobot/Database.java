package com.example.autobot;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import static com.android.volley.VolleyLog.TAG;

public class Database {
    protected FirebaseFirestore db;
    public static CollectionReference collectionReference_user;


    public Database() {
        db = FirebaseFirestore.getInstance();
        collectionReference_user = db.collection("users");
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




}
