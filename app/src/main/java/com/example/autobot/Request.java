package com.example.autobot;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;


import com.google.android.gms.maps.model.LatLng;

import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Request class is a information collection of request, we can set and get information of request through the method in this class.
 */

public class Request implements Serializable {

    private User Rider;
    private User Driver;
    private LatLng Destination;
    private LatLng BeginningLocation;
    private Date SendTime;
    private Date AcceptTime;
    private double EstimateCost;
    private String RequestStatus;
    private Date ArriveTime;
    private String RequestID;
    private ArrayList<String> requestStatusList;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyy hh:mm:ss");

    public Request(User user) throws ParseException {
        this.Rider = user;
        this.Destination = null;
        this.BeginningLocation = null;
        this.requestStatusList = new ArrayList<>();
        this.requestStatusList.add("Request Sending");
        this.requestStatusList.add("Request Accepted");
        this.requestStatusList.add("Rider picked");
        this.requestStatusList.add("Trip Completed");
        this.RequestStatus = requestStatusList.get(0);
        this.SendTime = new Date(System.currentTimeMillis());
        String defaultTimeString = "00-0-0000 00:00:00";
        this.AcceptTime = formatter.parse(defaultTimeString);
        this.ArriveTime = formatter.parse(defaultTimeString);
        this.EstimateCost = 0.0;
        this.RequestID = generateRequestID();

    }
    public Request() throws ParseException {
        this.Rider = null;
        this.Destination = null;
        this.BeginningLocation = null;
        this.requestStatusList = new ArrayList<>();
        this.requestStatusList.add("Request Sending");
        this.requestStatusList.add("Request Accepted");
        this.requestStatusList.add("Rider picked");
        this.requestStatusList.add("Trip Completed");
        this.RequestStatus = requestStatusList.get(0);
        this.SendTime = new Date(System.currentTimeMillis());
        String defaultTimeString = "00-0-0000 00:00:00";
        this.AcceptTime = formatter.parse(defaultTimeString);
        this.ArriveTime = formatter.parse(defaultTimeString);
        this.EstimateCost = 0.0;
        this.RequestID = null;

    }

    public Request(User user, LatLng origin, LatLng destination) throws ParseException {
        this.Rider = user;
        this.Destination = destination;
        this.BeginningLocation = origin;
        this.requestStatusList = new ArrayList<>();
        this.requestStatusList.add("Request Sending");
        this.requestStatusList.add("Request Accepted");
        this.requestStatusList.add("Rider picked");
        this.requestStatusList.add("Trip Completed");
        this.RequestStatus = requestStatusList.get(0);
        this.SendTime = new Date(System.currentTimeMillis());
        String defaultTimeString = "00-0-0000 00:00:00";
        this.AcceptTime = formatter.parse(defaultTimeString);
        this.ArriveTime = formatter.parse(defaultTimeString);
        this.EstimateCost = 0.0;
        this.RequestID = generateRequestID();

    }

    public Date getSendDate() {
        return this.SendTime;
    }


    public String generateRequestID() {
        String ID = this.Rider.getUsername()+this.SendTime.toString();
        return ID;
    }

    public void setRider(User user){
        this.Rider = user;
    }


    public String getRequestID(){
        return this.RequestID;
    }
    public User getRider(){
        return this.Rider;
    }
    //public Location getDestination() {return this.Destination;}
    public void setDriver(User driver){
        this.Driver = driver;
    }
    public User getDriver(){
        return this.Driver;
    }
    public void setRequestID(String ID){
        this.RequestID = ID;
    }

    public void setDestination(LatLng destination){
        this.Destination = destination;
    }
    public LatLng getDestination(){return this.Destination;}
    public void setBeginningLocation(LatLng beginningLocation){
        this.BeginningLocation = beginningLocation;
    }
    public LatLng getBeginningLocation(){return this.BeginningLocation;}

    public void setEstimateCost(LatLng destination, LatLng beginningLocation){
        double estimateCost = 0.0;
        if (destination != null && beginningLocation != null) {
            double distance = Math.round(SphericalUtil.computeDistanceBetween(beginningLocation, destination));
            estimateCost = 5.0 + distance * 0.01;
        }
        this.EstimateCost = estimateCost;
    }
    public double getEstimateCost() {
        return this.EstimateCost;
    }
    public void UpdateCurrentCost(Location CurrentLocation, Location beginningLocation){

    }
    public void UpdateStatus(int item){
        this.RequestStatus = requestStatusList.get(item);

    }
    //the active request string representation
    public String get_active_requset_tostring(){
        //this is the hard coding version, need to modify later
        String active_requst = String.format("Rider name: %s  Destination: %s\nEstimate cost: %.2f\nPhone: %s",Rider.getUsername(),"sub",13.34,"587-234-1299");
        return active_requst;
    }
    public Date getAcceptTime(){
        return this.AcceptTime;
    }
    public Date getArriveTime(){
        return this.ArriveTime;
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
    public String ReadableAddress(LatLng location, Geocoder geocoder) throws IOException {
        double lat = location.latitude;
        double lnt = location.longitude;
        List<Address> addresses;
        addresses = geocoder.getFromLocation(lat, lnt, 1);
        return addresses.get(0).getAddressLine(0);
                //+addresses.get(0).getFeatureName();
    }
    //public LatLng getCurrentLocation(){
        //return this.Rider.getCurrentLocation();
    //}
}
