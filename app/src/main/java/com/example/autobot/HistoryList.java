package com.example.autobot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HistoryList extends ArrayAdapter<HistoryRequest> {

    private ArrayList<HistoryRequest> historyRequests;
    private Context context;

    public HistoryList(Context context, ArrayList<HistoryRequest> historyRequests) {
        super(context,0,historyRequests);
        this.historyRequests = historyRequests;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.request_content,parent,false);
        }

        HistoryRequest historyRequest = historyRequests.get(position); //get the measurement by using its position

        TextView requestNumber = view.findViewById(R.id.request_number); //get each textview
        TextView status = view.findViewById(R.id.status);
        TextView dateTime = view.findViewById(R.id.date_time);
        ImageView  profilePhoto = view.findViewById(R.id.profile_photo);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // format for the date
        String dateString = formatter.format(historyRequest.getDate());        //transform the date


        String n = "Request Number: " + historyRequest.getRequestNumber();                       //the strings to show in the lines
        String s = "Status: " + historyRequest.getStatus();
        String d = "Date: " + dateString;
        int p = historyRequest.getPhoto();

        requestNumber.setText(n);        //set the above information on the line(textview)
        status.setText(s);
        dateTime.setText(d);
        profilePhoto.setImageResource(p);

        return view;
    }


}