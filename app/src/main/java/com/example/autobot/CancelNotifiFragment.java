package com.example.autobot;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CancelNotifiFragment extends Fragment {
    long TimeLeftInMillis = 4000;
    TextView notification;
    TextView timecouter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View rootView = inflater.inflate(R.layout.canel_notification,container,false);
        notification = rootView.findViewById(R.id.cancel_notification_text);
        notification.setText("Oops....... ...\nYour order has been canceled");
        timecouter = rootView.findViewById(R.id.time_downcounter);

        //downtimer for returning to homepage
        CountDownTimer CountDownTimer = new CountDownTimer(TimeLeftInMillis,1000){
            @Override
            //while still on counting time, update the remaining time data of the view 
            public void onTick(long millisUntilFinished) {
                //update the remaining time
                TimeLeftInMillis = millisUntilFinished;
                //update view
                updateCountDownText();
            }
            @Override
            public void onFinish() {
            }
            }.start();
        return rootView;}


    public void updateCountDownText(){
             int seconds = (int) (TimeLeftInMillis / 1000) % 60;
             timecouter.setText(String.format("%d s will return to home page",seconds));
    }
}
