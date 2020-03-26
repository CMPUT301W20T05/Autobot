package com.example.autobot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

public class SuccessfulNotification extends CancelNotifiFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = super.onCreateView(inflater,container,savedInstanceState);
        notification.setText("Passenger already accepted the order");
        return view;}
}
