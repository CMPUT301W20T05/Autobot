package com.example.autobot;

import android.graphics.Bitmap;

import java.util.Date;

public class HistoryRequest {
    // set variables
    private String status;
    private Date date;
    private Bitmap bitmap;
    private String user;
    private String requestId;

    HistoryRequest(String status, Date date, Bitmap bitmap, String user, String requestId) { // constructor
        this.status = status;
        this.date = date;
        this.bitmap = bitmap;
        this.user = user;
        this.requestId = requestId;
    }

    // setter that let set value
    void setStatus(String s) {this.status = s;}
    void setDate(Date d) {this.date = d;}
    void setBitmap(Bitmap bitmap) {this.bitmap = bitmap;}
    void setUser(String user) {this.user = user;}
    void setRequestId(String requestId) {this.requestId = requestId;}

    // can get the value using these getter
    String getStatus() { return this.status; }
    Date getDate() { return this.date; }
    Bitmap getBitmap() { return this.bitmap; }
    String getUser(){return this.user;}
    String getRequestId(){return this.requestId;}

}
