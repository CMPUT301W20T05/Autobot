package com.example.autobot;

import android.media.Image;

public interface Rider {
    void SendRequest(Request request);
    Image GanerateQRcode();
}
