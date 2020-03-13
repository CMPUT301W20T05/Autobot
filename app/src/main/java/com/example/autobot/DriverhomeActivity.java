package com.example.autobot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.internal.$Gson$Preconditions;

public class DriverhomeActivity extends BaseActivity {
    private User user;
    String phone_num;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //View rootView = getLayoutInflater().inflate(..., frameLayout);
        setTitle(phone_num);
        load_user();
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
