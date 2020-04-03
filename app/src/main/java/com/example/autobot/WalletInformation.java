package com.example.autobot;

public class WalletInformation {
    String Destination;
    String Cost;
    String date;

    /**
     * it is to set a custom list
     * @param destination
     * @param cost
     * @param date
     */
    public WalletInformation(String destination, String cost, String date) {
        Destination = destination;
        Cost = cost;
        this.date = date;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
