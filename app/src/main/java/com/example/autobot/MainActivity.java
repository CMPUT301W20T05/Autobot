package com.example.autobot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    Button get_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //click get start to change activitiy
        get_start = findViewById(R.id.start);
        get_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log_out_page(v);
            }
        });
    }
    /** Called when the user taps the Send button */
    public void log_out_page(View view) {
        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);
    }
}
