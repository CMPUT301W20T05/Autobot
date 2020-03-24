package com.example.autobot;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class PaymentCardTest {
    private PaymentCard mockPaymentCard() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2020-03-22");
        return new PaymentCard((long) 123456,"David", date, 10, "edmonton", "T6G 3M3");
    }

    @Test
    public void testGetter() throws ParseException{
        PaymentCard paymentCard = mockPaymentCard();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2020-03-22");
        assertEquals(date,paymentCard.getExpireDate());
        Long l = new Long(123456);
        assertEquals(l, paymentCard.getCardNumber());
        assertEquals("David",paymentCard.getHoldName());
        assertEquals(10,paymentCard.getCardLogo());
        assertEquals("edmonton", paymentCard.getBillingAddress());
        assertEquals("T6G 3M3", paymentCard.getPostalCode());
    }

    @Test
    public void testSetter() throws ParseException{
        PaymentCard paymentCard = mockPaymentCard();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2020-03-23");
        paymentCard.setExpireDate(date);
        assertEquals(date,paymentCard.getExpireDate());

        paymentCard.setBillingAddress("vancouver");
        assertEquals("vancouver",paymentCard.getBillingAddress());

        paymentCard.setCardLogo(20);
        assertEquals(20,paymentCard.getCardLogo());

        Long l = new Long(654321);
        paymentCard.setCardNumber(l);
        assertEquals(l,paymentCard.getCardNumber());

        paymentCard.setHoldName("Brian");
        assertEquals("Brian",paymentCard.getHoldName());

        paymentCard.setPostalCode("T5J 3N4");
        assertEquals("T5J 3N4", paymentCard.getPostalCode());
    }
}