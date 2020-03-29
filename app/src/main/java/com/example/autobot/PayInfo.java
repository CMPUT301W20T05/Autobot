package com.example.autobot;

import java.io.Serializable;

class PayInfo implements Serializable {
    private String CardNumber;
    private String HolderNmae;
    private String BillingAddress;
    private String Country;
    private String PostalCode;
    public PayInfo(){

    }
    public String getCardNumber(){return this.CardNumber;}
    public String getHolderNmae(){return this.HolderNmae;}
    public String getBillingAddress(){return this.BillingAddress;}
    public String getCountry(){return this.Country;}
    public String getPostalCode(){return this.PostalCode;}

    public void setCardNumber(String CardNumber){
        this.CardNumber = CardNumber;
    }
    public void setHolderNmae(String HolderName){
        this.HolderNmae = HolderName;
    }
    public void setBillingAddress(String BillingAddress){
        this.BillingAddress = BillingAddress;
    }
    public void setCountry(String country){
        this.Country = country;
    }
    public void setrPostalCode(String PostalCode){
        this.PostalCode = PostalCode;
    }
}
