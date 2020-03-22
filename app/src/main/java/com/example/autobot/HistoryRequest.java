package com.example.autobot;

import java.util.Date;

public class HistoryRequest {
    // set variables
    private Integer requestNumber;
    private String status;
    private Date date;
    private int photo;


    HistoryRequest(Integer requestNumber, String status, Date date, int photo) { // constructor
        this.requestNumber = requestNumber;
        this.status = status;
        this.date = date;
        this.photo = photo;
    }

    void setRequestNumber(Integer n) {this.requestNumber = n;} // setter that let set value
    void setStatus(String s) {this.status = s;}
    void setDate(Date d) {this.date = d;}
    void setPhoto(int p) {this.photo = p;}

    int getRequestNumber() { return this.requestNumber; }  // can get the value using these getter
    String getStatus() { return this.status; }
    Date getDate() { return this.date; }
    int getPhoto() { return this.photo; }

}
