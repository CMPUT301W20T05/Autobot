package com.example.autobot;

import java.util.Date;
/**
 * this is a class of Payment Card item, we can get information and set information here
 */
public class PaymentCard {
    // set variables
    private Long cardNumber;
    private String holdName;
    private Date expireDate;
    private int cardLogo;
    private String billingAddress;
    private String postalCode;
    private String userName;


    PaymentCard(Long cardNumber, String userName, String holdName, Date expireDate, int cardLogo, String billingAddress, String postalCode){
        this.cardNumber = cardNumber;
        this.holdName = holdName;
        this.expireDate = expireDate;
        this.cardLogo = cardLogo;
        this.billingAddress = billingAddress;
        this.postalCode = postalCode;
        this.userName = userName;

    }
    void setCardNumber(Long cn) {this.cardNumber = cn;} // setter that let set value
    void setHoldName(String n) {this.holdName = n;}
    void setExpireDate(Date d) {this.expireDate = d;}
    void setCardLogo(int l) {this.cardLogo = l;}
    void setBillingAddress(String b) {this.billingAddress = b;}
    void setPostalCode(String p) {this.postalCode = p;}
    void setUserName(String u) {this.userName = u;}

    public Long getCardNumber() { return this.cardNumber; }  // can get the value using these getter
    public String getHoldName() { return this.holdName; }
    public Date getExpireDate() { return this.expireDate; }
    public int getCardLogo() { return this.cardLogo; }
    public String getBillingAddress() { return this.billingAddress;}
    public String getPostalCode() {return this.postalCode;}
    public String getUserName(){return this.userName;}
}
