package com.example.autobot;

import android.graphics.Bitmap;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class HistoryRequestTest {
    private HistoryRequest mockHistoryRequest() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2020-03-22");
        Bitmap bitmap = null;
        return new HistoryRequest("ready", date, bitmap, "user");
    }

    @Test
    public void testGetter() throws ParseException {
        HistoryRequest historyRequest = mockHistoryRequest();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2020-03-22");
        Bitmap bitmap = null;
        assertEquals("ready",(String)historyRequest.getStatus());
        assertEquals(date, historyRequest.getDate());
        assertEquals(bitmap, historyRequest.getBitmap());
        assertEquals("user",historyRequest.getUser());
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

        Bitmap b = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        historyRequest.setBitmap(b, historyRequest.getBitmap());

        historyRequest.setUser("Brian");
        assertEquals("Brian",historyRequest.getUser());
    }
}