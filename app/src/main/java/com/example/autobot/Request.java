package com.example.autobot;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

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
    private double Cost;
    private String RequestStatus;
    private Date ArriveTime;
    private String RequestID;
    private ArrayList<String> requestStatusList;
    private double tips;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Request(User user) throws ParseException {
        this.Rider = user;
        this.Driver = null;
        this.Destination = null;
        this.BeginningLocation = null;
        this.requestStatusList = new ArrayList<>();
        this.requestStatusList.add("Request Sending");
        this.requestStatusList.add("Driver Accepted");
        this.requestStatusList.add("Rider Accepted");
        this.requestStatusList.add("Rider picked");
        this.requestStatusList.add("Trip Completed");
        this.requestStatusList.add("Cancel");
        this.RequestStatus = requestStatusList.get(0);
        this.SendTime = new Date(System.currentTimeMillis());
        String defaultTimeString = "0000-00-00 00:00:00";
        this.AcceptTime = formatter.parse(defaultTimeString);
        this.ArriveTime = formatter.parse(defaultTimeString);
        this.EstimateCost = 0.0;
        this.Cost = 0.0;
        this.RequestID = generateRequestID();
        this.tips = 0.0;

    }
    public Request() throws ParseException {
        this.Rider = null;
        this.Driver = null;
        this.Destination = null;
        this.BeginningLocation = null;
        this.requestStatusList = new ArrayList<>();
        this.requestStatusList.add("Request Sending");
        this.requestStatusList.add("Driver Accepted");
        this.requestStatusList.add("Rider Accepted");
        this.requestStatusList.add("Rider picked");
        this.requestStatusList.add("Trip Completed");
        this.requestStatusList.add("Cancel");
        this.RequestStatus = requestStatusList.get(0);
        this.SendTime = new Date(System.currentTimeMillis());
        String defaultTimeString = "0000-00-00 00:00:00";
        this.AcceptTime = formatter.parse(defaultTimeString);
        this.ArriveTime = formatter.parse(defaultTimeString);
        this.EstimateCost = 0.0;
        this.Cost = 0.0;
        this.RequestID = null;
        this.tips = 0.0;

    }

    public Request(User user, LatLng origin, LatLng destination) throws ParseException {
        this.Rider = user;
        this.Driver = null;
        this.Destination = destination;
        this.BeginningLocation = origin;
        this.requestStatusList = new ArrayList<>();
        this.requestStatusList.add("Request Sending");
        this.requestStatusList.add("Driver Accepted");
        this.requestStatusList.add("Rider Accepted");
        this.requestStatusList.add("Rider picked");
        this.requestStatusList.add("Trip Completed");
        this.requestStatusList.add("Cancel");
        this.RequestStatus = requestStatusList.get(0);
        this.SendTime = new Date(System.currentTimeMillis());
        String defaultTimeString = "0000-00-00 00:00:00";
        this.AcceptTime = formatter.parse(defaultTimeString);
        this.ArriveTime = formatter.parse(defaultTimeString);
        this.EstimateCost = 0.0;
        this.Cost = 0.0;
        this.RequestID = generateRequestID();
        this.tips = 0.0;

    }
    public double getTips(){
        return this.tips;
    }
    public void setTips(double tips){
        this.tips = tips;
    }

    /**
     * reset tips in database
     * @param tips new tips
     * @param db Database
     */
    public void resetTips(double tips,Database db){
        this.tips = tips;
        HashMap<String, Object> update = new HashMap<>();
        update.put("Tips", String.valueOf(this.tips));
        db.collectionReference_request.document(RequestID)
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
        BigDecimal bg = new BigDecimal(estimateCost);
        this.EstimateCost = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    //add a function to directly set estcost
    public void direct_setEstimateCost(double Estcost){
        this.EstimateCost = Estcost;
    }

    public double getEstimateCost() {
        return this.EstimateCost;
    }
    public double EstimateAddModelFee(double addPrice) {
        this.EstimateCost += addPrice;
        return this.EstimateCost;
    }

    public void setCost(double cost) {
        this.Cost = cost;
    }

    public double getCost() {
        return this.Cost;
    }

    /**
     * reset cost in database
     * @param cost new cost
     * @param db database
     */

    public void resetCost(double cost,Database db){
        this.Cost = cost;
        HashMap<String, Object> update = new HashMap<>();
        update.put("Cost", String.valueOf(this.Cost));
        db.collectionReference_request.document(RequestID)
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

    public void setAcceptTime(Date d){
        this.AcceptTime = d;
    }

    /**
     * reset accept time in database
     * @param d new time
     * @param db Database
     */
    public void resetAcceptTime(Date d, Database db){
        this.AcceptTime = d;
        HashMap<String, Object> update = new HashMap<>();
        update.put("AcceptTime", String.valueOf(this.AcceptTime));
        db.collectionReference_request.document(RequestID)
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
     * reset arrive time in database
     * @param d new time
     */
    public void setArriveTime(Date d){
        this.ArriveTime = d;
    }
    /**
     * reset arrive time in database
     * @param d new time
     * @param db Database
     */
    public void resetArriveTime(Date d ,Database db){
        this.ArriveTime = d;
        HashMap<String, Object> update = new HashMap<>();
        update.put("ArriveTime", String.valueOf(this.ArriveTime));
        db.collectionReference_request.document(RequestID)
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
     * reset status in database
     * @param status new status
     * @param db Database
     */
    public void resetRequestStatus(String status,Database db){
        this.RequestStatus = status;
        HashMap<String, Object> update = new HashMap<>();
        update.put("RequestStatus", this.getStatus());
        db.collectionReference_request.document(RequestID)
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
    public void reset_Request_Status(String status) {
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
    //calculate the distance between start and destination
    public String calculate_distance(){
        double distance = Math.round(SphericalUtil.computeDistanceBetween(getBeginningLocation(), getDestination()));
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(distance);
    }

    //calculate how many minutes drive to destination
    public String calculate_time(){
        double distance = Math.round(SphericalUtil.computeDistanceBetween(getBeginningLocation(), getDestination()));
        double time = distance / 1008.00 + 5.00;
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(time);
    }
    //just for testing
    public String testing_rebuild_request(){
        return String.format("ID: %s\nRider name: %s\nDriver name: %s\nRequest Status: %s",this.getRequestID(),Rider.getUsername(),Driver.getUsername(),this.getStatus());
    }
}
