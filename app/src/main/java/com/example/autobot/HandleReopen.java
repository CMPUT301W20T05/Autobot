package com.example.autobot;

import android.content.Context;

public abstract class HandleReopen {
    Context context;
    Request request;
    public HandleReopen(Context context, Request request){
        this.context = context;
        this.request = request;
    }

    public void handle_activities(){
        switch (request.getStatus()){
            case "Rider Accepted":
                start_driverontheway();
                break;
            case "Rider picked":
                start_driverontheway();
                break;
            case "Trip Completed":
                start_orderfinish();
            default:
                start_homepage();
        }
    }

    public abstract void start_driverontheway();
    public abstract void start_orderfinish();
    public abstract void start_homepage();
}