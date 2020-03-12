package com.example.autobot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class OrderInfo extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("Rider Mode");
        View rootView = getLayoutInflater().inflate(R.layout.order_info, frameLayout);

        Button viewDetail = findViewById(R.id.View_Detail);
        ImageButton emailButton = findViewById(R.id.emailButton);
        ImageButton phoneButton = findViewById(R.id.phoneButton);
        ImageButton backButton = findViewById(R.id.button_back);
        Button cancel = findViewById(R.id.cancel_order);
        TextView originLoc = findViewById(R.id.origin_loc);
        TextView Destination = findViewById(R.id.Destination);
        TextView ApproDist = findViewById(R.id.Appro_distance);
        TextView ApproPrice = findViewById(R.id.Appro_price);

        
    }
}
