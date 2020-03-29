package com.example.autobot;

import org.junit.Test;

import static org.junit.Assert.*;

public class WalletInformationTest {
    private WalletInformation mockWalletInformation(){
        return new WalletInformation("Southgate", "50", "2020-03-28");
    }

    @Test
    public void testGetter(){
        WalletInformation walletInformation = mockWalletInformation();
        assertEquals("Southgate", walletInformation.getDestination());
        assertEquals("50", walletInformation.getCost());
        assertEquals("2020-03-28", walletInformation.getDate());
    }

    @Test
    public void testSetter(){
        WalletInformation walletInformation = mockWalletInformation();
        walletInformation.setDestination("Capital");
        assertEquals("Capital", walletInformation.getDestination());
        walletInformation.setCost("60");
        assertEquals("60", walletInformation.getCost());
        walletInformation.setDate("2020-04-01");
        assertEquals("2020-04-01", walletInformation.getDate());
    }
}