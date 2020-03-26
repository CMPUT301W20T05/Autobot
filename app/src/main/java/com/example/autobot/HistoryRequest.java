package com.example.autobot;

import android.graphics.Bitmap;

import java.util.Date;

public class HistoryRequest {
    // set variables
    private String status;
    private Date date;
    private String user;
    private String requestId;
    private Double cost;

    HistoryRequest(String status, Date date, String user, String requestId, Double cost) { // constructor
        this.status = status;
        this.date = date;
        this.user = user;
        this.requestId = requestId;
        this.cost = cost;
    }

    // setter that let set value
    void setStatus(String s) {this.status = s;}
    void setDate(Date d) {this.date = d;}
//    void setBitmap(Bitmap b, Bitmap bitmap) {this.bitmap = bitmap;}

    void setUser(String user) {this.user = user;}
    void setRequestId(String requestId) {this.requestId = requestId;}
    void setCost(Double cost) {this.cost = cost;}


    // can get the value using these getter
    String getStatus() { return this.status; }
    Date getDate() { return this.date; }
    String getUser(){return this.user;}
    String getRequestId(){return this.requestId;}
    Double getCost(){return this.cost;}

}
