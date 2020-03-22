package com.example.autobot;

import android.graphics.Bitmap;

import java.util.Date;

public class HistoryRequest {
    // set variables
    private String status;
    private Date date;
    private Bitmap bitmap;

    HistoryRequest(String status, Date date, Bitmap bitmap) { // constructor
        this.status = status;
        this.date = date;
        this.bitmap = bitmap;
    }

    // setter that let set value
    void setStatus(String s) {this.status = s;}
    void setDate(Date d) {this.date = d;}
    void setBitmap(Bitmap bitmap) {this.bitmap = bitmap;}

    // can get the value using these getter
    String getStatus() { return this.status; }
    Date getDate() { return this.date; }
    Bitmap getBitmap() { return this.bitmap; }

}
