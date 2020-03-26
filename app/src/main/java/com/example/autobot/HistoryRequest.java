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
    private int viewType;

    public HistoryRequest(String status, Date date, String user, String requestId, Double cost, int viewType) { // constructor
        this.status = status;
        this.date = date;
        this.user = user;
        this.requestId = requestId;
        this.cost = cost;
        this.viewType = viewType;
    }

    // setter that let set value
    void setStatus(String s) {this.status = s;}
    void setDate(Date d) {this.date = d;}
//    void setBitmap(Bitmap b, Bitmap bitmap) {this.bitmap = bitmap;}

    void setUser(String user) {this.user = user;}
    void setRequestId(String requestId) {this.requestId = requestId;}
    void setCost(Double cost) {this.cost = cost;}
    void setViewType(int viewType) {this.viewType = viewType;}


    // can get the value using these getter
    public String getStatus() { return this.status; }
    public Date getDate() { return this.date; }
    public String getUser(){return this.user;}
    public String getRequestId(){return this.requestId;}
    public Double getCost(){return this.cost;}
    public int getViewType(){return this.viewType;}

}
