package com.example.autobot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class DriverhomeActivity extends HomePageActivity {
    private User user;
    String phone_num;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        load_user();
        setTitle(phone_num);
    }

    public void load_user(){
        Intent intent = getIntent();
        if(intent != null){
            Log.d("check","exist");
        }else{
            Log.d("check","fail");
        }
        phone_num = intent.getExtras().getString("phone number");
    }

}
