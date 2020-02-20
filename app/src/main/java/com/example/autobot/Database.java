package com.example.autobot;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Database {
    private FirebaseFirestore db;
    CollectionReference collectionReference_user;
    CollectionReference collectionReference_request;

    public Database() {
        db = FirebaseFirestore.getInstance();
        collectionReference_user = db.collection("users");
        collectionReference_request = db.collection("requests");
    }

    public void add_new_user(User user) {
        HashMap<String,String> user_data = new HashMap<>();
        user_data.put("Username", user.getUsername());
        user_data.put("FirstName", user.getFirstName());
        user_data.put("LastName", user.getLastName());
        user_data.put("EmailAddress", user.getEmailAddress());
        user_data.put("PhoneNumber", user.getPhoneNumber());
        user_data.put("StarsRate", user.getStars().toString());
        user_data.put("Type", user.getUserType());
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
}
