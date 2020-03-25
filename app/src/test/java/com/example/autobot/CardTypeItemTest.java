package com.example.autobot;

import org.junit.Test;

import static org.junit.Assert.*;

public class CardTypeItemTest {
    private CardTypeItem mockCardType(){
        return new CardTypeItem("Visa",10);
    }

    @Test
    public void testGetter(){
        CardTypeItem cardTypeItem = mockCardType();
        assertEquals("visa", cardTypeItem.getCardType());
        assertEquals(10,cardTypeItem.getCardTypelogo());
    }
}