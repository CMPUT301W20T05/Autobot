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
        return new HistoryRequest(10,"ready", date, 10);
    }

    @Test
    public void testGetter() throws ParseException {
        HistoryRequest historyRequest = mockHistoryRequest();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2020-03-22");
        assertEquals(10, (int)historyRequest.getRequestNumber());
        assertEquals("ready",(String)historyRequest.getStatus());
        assertEquals(date, historyRequest.getDate());
        assertEquals(10,historyRequest.getPhoto());
    }

    @Test
    public void testSetter() throws ParseException {
        HistoryRequest historyRequest = mockHistoryRequest();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2020-03-22");
        historyRequest.setDate(date);
        assertEquals(date,historyRequest.getDate());

        historyRequest.setPhoto(20);
        assertEquals(20,historyRequest.getPhoto());

        int request = 30;
        historyRequest.setRequestNumber(request);
        assertEquals(request, historyRequest.getRequestNumber());

        historyRequest.setStatus("processing");
        assertEquals("processing", historyRequest.getStatus());
    }
}