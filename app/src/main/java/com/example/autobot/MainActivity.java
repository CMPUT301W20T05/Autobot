package com.example.autobot;


import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.content.Intent;
import android.os.Handler;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import java.text.ParseException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

/**
 * this is class for main activity(first page of our app)
 */
public class MainActivity extends AppCompatActivity {
    Button get_start;
    Handler handler;
    int DELAY = 3*1000;
    public static com.example.autobot.Database db;
    SharedPreferences preferences_user;
    SharedPreferences preferences_request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            db = new Database();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);

        //if(Offline.preference_is_empty(preferences_request)&&Offline.preference_is_empty(preferences_user)){
        //   log_in_page();
        //}else{
        //  jump_to_homepage();
        //}
        log_in_page();

    }
    /*change activity*/
    public void log_in_page() {
        final Intent intentLogin = new Intent(this,LoginActivity.class);
        Timer timer = new Timer();
        TimerTask task = new TimerTask()
        {
            @Override
            public void run(){
                startActivity(intentLogin);
            }
        };
        timer.schedule(task,DELAY);
    }
    public void jump_to_homepage(){
        final Intent intenthomepage = new Intent(this, HomePageActivity.class);
        startActivity(intenthomepage);
    }
}

