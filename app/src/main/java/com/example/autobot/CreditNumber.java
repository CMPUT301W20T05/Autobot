package com.example.autobot;

public class CreditNumber {
    private String money;
    private int cardTypelogo;

    CreditNumber(String money){
        this.money = money;
    }

    public String getCardType() { return this.money;}

}
