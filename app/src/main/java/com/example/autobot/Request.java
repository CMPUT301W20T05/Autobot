package com.example.autobot;

import android.location.Location;


import java.util.Date;


public class Request{
    private User Rider;
    private User Driver;
    private Location Destination;
    private Location BeginningLocation;
    private Date SendTime;
    private Date AcceptTime;
    private Float EstimateCost;
    private Float CurrentLocation;
    private String RequestStatus;
    private Date ArriveTime;

    public Request(User rider, User driver, Location destination, Location beginningLocation){
        this.Rider = rider;
        this.Driver = driver;
        this.Destination = destination;
        this.BeginningLocation = beginningLocation;
        this.SendTime = new Date();
        this.RequestStatus = "Request Sending";

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
    public void setDestination(Location destination){
        this.Destination = destination;
    }
    public void setBeginningLocation(Location beginningLocation){
        this.BeginningLocation = beginningLocation;
    }
    public void ganerateAcceptTime(){
        this.AcceptTime = new Date();
    }
    public void ganerateArriveTime(){
        this.ArriveTime = new Date();
    }
    public void getCurrentLocation(){

    }
    public void EstimateCost(Location destination, Location beginningLocation){

    }
    public void UpdateCurrentCost(Location CurrentLocation, Location beginningLocation){

    }
    public void UpdateStatus(String status){

    }
}
