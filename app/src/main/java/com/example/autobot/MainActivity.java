package com.example.autobot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Button get_start;
    Handler handler;
    int DELAY = 3*1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        setContentView(R.layout.current_request_of_user);
=======
        setContentView(R.layout.activity_main);

        log_in_page();

    }
    /*change activity*/
    public void log_in_page() {
        final Intent intentLogin = new Intent(this,SignUpActivity.class);
        Timer timer = new Timer();
        TimerTask task = new TimerTask()
        {
            @Override
            public void run(){
                startActivity(intentLogin);
            }
        };
        timer.schedule(task,DELAY);
>>>>>>> 9df9d515f518a1cfcc8f84011688cf631f19a421
    }
}
