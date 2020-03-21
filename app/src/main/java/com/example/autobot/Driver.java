package com.example.autobot;

import android.media.Image;

public interface Driver {
    public void AcceptRequest(Request request);
    public void ScanQRcode(int QRcode);
}
