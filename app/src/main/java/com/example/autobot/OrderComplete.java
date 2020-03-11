package com.example.autobot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OrderComplete extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("Rider Mode");
        View rootView = getLayoutInflater().inflate(R.layout.accurate_fair, frameLayout);

        EditText Destination = findViewById(R.id.textView3);
        EditText OriginalLoc = findViewById(R.id.textView4);
        EditText Price = findViewById(R.id.textView6);
        EditText Tips = findViewById(R.id.textView7);
        Button Confirm = findViewById(R.id.button2);

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRateDriver = new Intent(OrderComplete.this, RateDriver.class);
                startActivity(intentRateDriver);
            }
        });
    }
}
