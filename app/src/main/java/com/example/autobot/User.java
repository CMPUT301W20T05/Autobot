package com.example.autobot;

import android.location.Location;
import android.media.Image;

public class User implements Driver, Rider {
    private String Username;
    private String EmailAddress;
    private String PhoneNumber;
    private Location CurrentLocation;
    private String Password;
    private Image Photo;
    private int Stars;
    private PayInfo PaymentInfo;
    private String UserType;

    public User(String username, String emailAddress, String password, String usertype, Location location_1, String userType){
        this.Username = username;
        this.EmailAddress = emailAddress;
        this.Password = password;
        this.UserType = usertype;
        this.PhoneNumber = "";
        this.CurrentLocation = new Location(location_1);
        this.Stars = 5;
        this.PaymentInfo = new PayInfo();
        this.UserType = userType;
    }
    public User(String username,  String password, String usertype, Location location_1, String userType, String phoneNumber){
        this.Username = username;
        this.EmailAddress = "";
        this.Password = password;
        this.UserType = usertype;
        this.PhoneNumber = phoneNumber;
        this.CurrentLocation = new Location(location_1);
        this.Stars = 5;
        this.PaymentInfo = new PayInfo();
        this.UserType = userType;
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
        this.EmailAddress = phoneNumber;
    }
    public void getCurrentLocation(){

    }
    public String getPassword(){
        return this.Password;
    }
    public void setPassord(String password){
        this.Password = password;
    }
    public Image getPhoto(){
        return this.Photo;
    }
    public void uploadPhoto(Image photo){

    }
    public int getStars(){
        return this.Stars;
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
    public void ScanQRcode(Image QRcode) {

    }

    @Override
    public void SendRequest(Request request) {

    }

    @Override
    public Image GanerateQRcode() {
        return null;
    }
}
