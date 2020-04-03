package com.example.autobot;

import android.graphics.Bitmap;

import java.util.Date;
/**
 * this is a class of History Request item, we can get information and set information here
 */
public class HistoryRequest {
    // set variables
    private String status;
    private Date date;
    private String user;
    private String requestId;
    private float cost;
    private int viewType;

    public HistoryRequest(String status, Date date, String user, String requestId, float cost, int viewType) { // constructor
        this.status = status;
        this.date = date;
        this.user = user;
        this.requestId = requestId;
        this.cost = cost;
        this.viewType = viewType;
    }

    // setter that let set value
    public void setStatus(String s) {this.status = s;}
    public void setDate(Date d) {this.date = d;}
//    void setBitmap(Bitmap b, Bitmap bitmap) {this.bitmap = bitmap;}

    public void setUser(String user) {this.user = user;}
    public void setRequestId(String requestId) {this.requestId = requestId;}
    public void setCost(float cost) {this.cost = cost;}
    public void setViewType(int viewType) {this.viewType = viewType;}


    // can get the value using these getter
    public String getStatus() { return this.status; }
    public Date getDate() { return this.date; }
    public String getUser(){return this.user;}
    public String getRequestId(){return this.requestId;}
    public float getCost(){return this.cost;}
    public int getViewType(){return this.viewType;}

}
