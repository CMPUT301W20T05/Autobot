package com.example.autobot;

/**
 * This class is the user information bus.
 */

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.Serializable;
import java.util.HashMap;

import static com.android.volley.VolleyLog.TAG;


public class User implements Driver, Rider, Serializable {
    private String Username;
    private String EmailAddress;
    private String PhoneNumber;
    private LatLng CurrentLocation;
    private String Password;
    private int Photo;
    private double Stars;
    private PayInfo PaymentInfo;
    private String UserType;
    private String FirstName;
    private String LastName;
    private String EmergencyContact;
    private String HomeAddress;
    private String uri;
    private String GoodRate;
    private String BadRate;
    private String Balance;

    public User(String username){
        this.Username = username;
        this.EmailAddress = "";
        this.Password = "";
        this.UserType = "";
        this.PhoneNumber = "";
        this.EmergencyContact = "";
        this.HomeAddress = "";
        this.CurrentLocation = new LatLng(0.0,0.0);
        this.Stars = 0.0;
        this.PaymentInfo = new PayInfo();
        this.uri = String.valueOf(R.id.icon);
        this.GoodRate = "0";
        this.BadRate = "0";
        this.Balance = "100";
    }

    public String getEmergencyContact() {
        return EmergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.EmergencyContact = emergencyContact;
    }

    /**
     * This is to update new balance to database
     * @param balance
     * @param db
     */

    public void resetbalance(String balance, Database db){
        this.Balance = balance;
        HashMap<String, Object> update = new HashMap<>();
        update.put("Balance", balance);
        db.collectionReference_user.document(Username)
                .update(update)
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
     * This is to update new Goodrate to database
     * @param goodrate
     * @param db
     */

    public void resetGoodrate(String goodrate,Database db) {
        this.GoodRate = goodrate;
        HashMap<String, Object> update = new HashMap<>();
        update.put("GoodRate", goodrate);
        db.collectionReference_user.document(Username)
                .update(update)
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
     * This is to updata new Badtate to database
     * @param badrate
     * @param db
     */
    public void resetBadrate(String badrate,Database db) {
        this.BadRate = badrate;
        HashMap<String, Object> update = new HashMap<>();
        update.put("BadRate", badrate);
        db.collectionReference_user.document(Username)
                .update(update)
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

    public String getUri(){return this.uri;}
    public void setUri(String urii){this.uri = urii;}

    //-----------------------------------

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getGoodRate() {
        return this.GoodRate;
    }

    public void setGoodRate(String goodRate) {
        this.GoodRate = goodRate;
    }

    public String getBadRate() {
        return this.BadRate;
    }

    public void setBadRate(String badRate) {
        this.BadRate = badRate;
    }

    public String getHomeAddress() {
        return this.HomeAddress;
    }
    public void setHomeAddress(String homeAddress) {
        this.HomeAddress = homeAddress;
    }
    public String getFirstName() {
        return this.FirstName;
    }
    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }
    public String getLastName() {
        return this.LastName;
    }
    public void setLastName(String lastName) {
        this.LastName = lastName;
    }
    public String getUsername(){
        return this.Username;
    }
    public void setUsername(String username){
        this.Username = username;
    }
    public String getEmailAddress(){
        return this.EmailAddress;
    }
    public void setEmailAddress(String emailaddress){
        this.EmailAddress = emailaddress;
    }
    public String getPhoneNumber(){
        return this.PhoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
        this.PhoneNumber = phoneNumber;
    }
    public LatLng getCurrentLocation(){
        return this.CurrentLocation;
    }
    public void updateCurrentLocation(LatLng Location){
        this.CurrentLocation = Location;
    }
    public String getPassword(){
        return this.Password;
    }

    public void setPassword(String password){

        this.Password = password;
    }
    public int getPhoto(){
        return this.Photo;
    }
    public void uploadPhoto(int photo){

    }
    public void setStars(double stars){
        this.Stars = stars;
    }


    public Double getStars(){
        return this.Stars;
    }

    public String getUserType() {
        return this.UserType;
    }

    public void setUserType(String userType) {
        this.UserType = userType;
    }

    public PayInfo getPayInfo(){
        return this.PaymentInfo;
    }
    public void setPayInfo (PayInfo payinfo){
        this.PaymentInfo =  payinfo;
    }
    public void ContactOtherByPhone(String PhoneNUmber){

    }
    public void ContactOtherByEmail(String Email){

    }

    @Override
    public void AcceptRequest(Request request) {
        request.setDriver(this);
    }

    @Override
    public void ScanQRcode(int QRcode) {

    }

    @Override
    public void SendRequest(Request request) {

    }

    @Override
    public int GanerateQRcode() {
        return 0;
    }


}
