package com.example.autobot;

import android.location.Location;


import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Request {

    private User Rider;
    private User Driver;
    private LatLng Destination;
    private LatLng BeginningLocation;
    private Date SendTime;
    private Date AcceptTime;
    private double EstimateCost;
    private String RequestStatus;
    private Date ArriveTime;
    private long RequestID;
    private ArrayList<String> requestStatusList;

    public Request(){
        this.Rider = null;
        this.Destination = null;
        this.BeginningLocation = null;
        this.SendTime = new Date();
        this.requestStatusList = new ArrayList<>();
        this.requestStatusList.add("Request Sending");
        this.requestStatusList.add("Request Accepted");
        this.requestStatusList.add("Rider picked");
        this.requestStatusList.add("Trip Completed");
        this.RequestStatus = requestStatusList.get(0);
        this.SendTime = new Date(System.currentTimeMillis());
        this.AcceptTime = null;
        this.ArriveTime = null;
        this.EstimateCost = EstimateCost(this.Destination,this.BeginningLocation);
        generateRequestID();



    }
    public Date getSendDate(){
        return this.SendTime;
    }
    public void generateRequestID(){
        Database db = new Database();
        final boolean[] judge = {true};
        while (judge[0]){
            final double d = Math.random();
            final long ID = (long)(d*1000000000);
            db.collectionReference_request.document(String.valueOf(ID)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                    }else{
                        judge[0] = false;
                        setRequestID(ID);
                    }
                }
            });
        }

    }
    public void setRider(User user){
        this.Rider = user;
    }

    public void setRequestID(long ID){
        this.RequestID = ID;
    }
    public String getRequestID(){
        return String.valueOf(this.RequestID);
    }
    public User getRider(){
        return this.Rider;
    }
    public void setDriver(User driver){
        this.Driver = driver;
    }
    public User getDriver(){
        return this.Driver;
    }

    public void setDestination(LatLng destination){
        this.Destination = destination;
    }
    public LatLng getDestination(){return this.Destination;}
    public void setBeginningLocation(LatLng beginningLocation){
        this.BeginningLocation = beginningLocation;
    }
    public LatLng getBeginningLocation(){return this.BeginningLocation;}
    public void getAcceptTime(){
        this.AcceptTime = new Date();
    }
    public void getArriveTime(){
        this.ArriveTime = new Date();
    }
    public double EstimateCost(LatLng destination, LatLng beginningLocation){
        //double distance = destination.distanceTo(beginningLocation);
        return 0.0;

    }
    public void UpdateCurrentCost(Location CurrentLocation, Location beginningLocation){

    }
    public void UpdateStatus(int item){
        this.RequestStatus = requestStatusList.get(item);

    }
    public void resetSendTime(Date d){
        this.SendTime = d;
    }
    public void resetAcceptTime(Date d){
        this.AcceptTime = d;
    }
    public void resetArriveTime(Date d){
        this.ArriveTime = d;
    }
    public void resetRequestStatus(String status){
        this.RequestStatus = status;
    }
    public void resetEstimateCost(double cost){
        this.EstimateCost = cost;
    }
    public String getStatus(){return this.RequestStatus;}
    //public LatLng getCurrentLocation(){
        //return this.Rider.getCurrentLocation();
    //}
}
