package com.example.autobot;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class HistoryRequestTest {
    private HistoryRequest mockHistoryRequest() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2020-03-22");
        Float cost = (float)50.0;
        return new HistoryRequest("ready", date, "David", "A123", cost, 1);
    }

    @Test
    public void testGetter() throws ParseException {
        HistoryRequest historyRequest = mockHistoryRequest();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2020-03-22");
        Float cost = (float)50.0;
        assertEquals("ready",(String)historyRequest.getStatus());
        assertEquals(date, historyRequest.getDate());
        assertEquals(java.util.Optional.of(cost), java.util.Optional.of(historyRequest.getCost()));
        assertEquals("David",historyRequest.getUser());
        assertEquals("A123", historyRequest.getRequestId());
    }

    @Test
    public void testSetter() throws ParseException {
        HistoryRequest historyRequest = mockHistoryRequest();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2020-03-23");
        historyRequest.setDate(date);
        assertEquals(date,historyRequest.getDate());

        historyRequest.setStatus("processing");
        assertEquals("processing", historyRequest.getStatus());

        Float cost = (float)60.0;
        historyRequest.setCost(cost);
        assertEquals(java.util.Optional.of(cost), java.util.Optional.of(historyRequest.getCost()));

        historyRequest.setUser("Brian");
        assertEquals("Brian",historyRequest.getUser());

        historyRequest.setRequestId("B123");
        assertEquals("B123", historyRequest.getRequestId());
    }
}