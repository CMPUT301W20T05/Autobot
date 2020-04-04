package com.example.autobot;

import org.junit.Test;

import static org.junit.Assert.*;
/**
 This is the Unit test for testing CardTypeItem Class
 */
public class CardTypeItemTest {
    private CardTypeItem mockCardType(){
        return new CardTypeItem("Visa",10);
    }

    @Test
    public void testGetter(){
        CardTypeItem cardTypeItem = mockCardType();
        assertEquals("Visa", cardTypeItem.getCardType());
        assertEquals(10,cardTypeItem.getCardTypelogo());
    }
}