package com.example.autobot;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = getSharedPreferences("User",MODE_PRIVATE);

        createNotificationChannels();
    }

    /**
     * create a notification channel
     */
    public void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Notification Channel 1");

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription("This is Notification Channel 2");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel1);
                manager.createNotificationChannel(channel2);
            }
        }
    }
}
