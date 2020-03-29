package com.example.autobot;

import org.junit.Test;

import static org.junit.Assert.*;

public class PayInfoTest {
    private PayInfo mockPayInfo(){
        return new PayInfo();
    }

    @Test
    public void testSetter(){
        PayInfo payInfo = mockPayInfo();
        payInfo.setBillingAddress("Edmonton");
        assertEquals("Edmonton", payInfo.getBillingAddress());

        payInfo.setCardNumber("Z1123");
        assertEquals("Z1123", payInfo.getCardNumber());

        payInfo.setCountry("China");
        assertEquals("China", payInfo.getCountry());

        payInfo.setHolderNmae("David");
        assertEquals("David", payInfo.getHolderNmae());

        payInfo.setrPostalCode("T6J 2H4");
        assertEquals("T6J 2H4", payInfo.getPostalCode());
    }
}