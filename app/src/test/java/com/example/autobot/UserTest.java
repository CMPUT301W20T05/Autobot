package com.example.autobot;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {
    private User mockUser(){
        return new User("David");
    }

    @Test
    public void testGetter(){
        User user = mockUser();
        assertEquals("", user.getEmergencyContact());
        assertEquals(String.valueOf(R.id.icon), user.getUri());
        assertEquals("0",user.getGoodRate());
        assertEquals("0", user.getBadRate());
        assertEquals("", user.getHomeAddress());
        assertEquals("David",user.getUsername());
        assertEquals("",user.getEmailAddress());
        assertEquals("",user.getPhoneNumber());
        assertEquals(new LatLng(0.0,0.0), user.getCurrentLocation());
        assertEquals("",user.getPassword());
        assertEquals(new Double(0.0), user.getStars());
        assertEquals("",user.getUserType());
    }

    @Test
    public void testSetter(){
        User user = mockUser();
        user.setEmergencyContact("123456");
        assertEquals("123456",user.getEmergencyContact());
        user.setUri("abc");
        assertEquals("abc",user.getUri());
        user.setGoodRate("1");
        assertEquals("1",user.getGoodRate());
        user.setBadRate("2");
        assertEquals("2",user.getBadRate());
        user.setHomeAddress("edmonton");
        assertEquals("edmonton",user.getHomeAddress());
        user.setUsername("Brian");
        assertEquals("Brian",user.getUsername());
        user.setEmailAddress("123456@qq.com");
        assertEquals("123456@qq.com",user.getEmailAddress());
        user.setPhoneNumber("654321");
        assertEquals("654321",user.getPhoneNumber());
        LatLng location = new LatLng(1.0, 2.0);
        user.updateCurrentLocation(location);
        assertEquals(location, user.getCurrentLocation());
        user.setPassword("zxc1233");
        assertEquals("zxc1233",user.getPassword());
        Double rate = 2.0;
        user.setStars(rate);
        assertEquals(rate,user.getStars());
        user.setUserType("Driver");
        assertEquals("Driver",user.getUserType());
    }
}