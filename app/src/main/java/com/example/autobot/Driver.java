package com.example.autobot;

import android.media.Image;

public interface Driver {
    void AcceptRequest(Request request);
    void ScanQRcode(Image QRcode);
}
