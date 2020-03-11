package com.example.autobot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class RateDriver extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Rider Mode");
        View rootView = getLayoutInflater().inflate(R.layout.rate_driver, frameLayout);
        findViewById(R.id.myMap).setVisibility(View.GONE);

        ImageView Selfie = findViewById(R.id.imageView);
        EditText Driver = findViewById(R.id.Driver_name);
        Button Profile = findViewById(R.id.see_profile);
        EditText Comment = findViewById(R.id.comment);
        Button Skip = findViewById(R.id.skip);
        Button Confirm = findViewById(R.id.button2);

        Selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //see profile
            }
        });

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //see profile
            }
        });

        String comment = Comment.getText().toString();
        Comment.setText(comment);

        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //next activity
                finish();
            }
        });

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //next activity
                finish();
            }
        });
    }
}
