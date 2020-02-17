package com.example.autobot;

class PayInfo {
    private String CardNumber;
    private String HolderNmae;
    private String BillingAddress;
    private String Country;
    private String PostalCode;
    public PayInfo(){

    }
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
