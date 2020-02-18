package com.example.autobot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button get_start;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //opening animation
        set_animation();
    }
    /*change activity*/
    public void log_out_page() {
        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);
    }

    @SuppressLint("HandlerLeak")
    public void set_animation(){
        //handle the transformation
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                log_out_page();
            }};
        //delay the transformation between home page and login page
        handler.sendEmptyMessageDelayed(0,4000);
    }
}
