package com.example.autobot;

public class CardTypeItem {
    private String cardType;
    private int cardTypelogo;

    CardTypeItem(String cardType, int cardTypelogo){
        this.cardType = cardType;
        this.cardTypelogo = cardTypelogo;
    }
    public String getCardType() { return this.cardType;}
    public int getCardTypelogo() {return this.cardTypelogo;}


}