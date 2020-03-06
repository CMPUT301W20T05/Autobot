package com.example.autobot;


import android.media.Image;

import android.provider.ContactsContract;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;


public class User implements Driver, Rider , Serializable{
    private String Username;
    private String EmailAddress;
    private String PhoneNumber;
    private double longitude;
    private double latitude;
    private String Password;
    private int Photo;
    private double Stars;
    private PayInfo PaymentInfo;
    private String UserType;
    private String FirstName;
    private String LastName;

    public User(){
        this.Username = "";
        this.EmailAddress = "";
        this.Password = "";
        this.UserType = "";
        this.PhoneNumber = "";
        this.longitude = 0;
        this.latitude = 0;
        this.Stars = 0.0;
        this.PaymentInfo = new PayInfo();
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
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
    public void getCurrentLocation(){

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


    public Double getStars(){
        return this.Stars;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
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
    public Double getLatitude(){
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public Double getLongitude(){
        return this.longitude;
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
