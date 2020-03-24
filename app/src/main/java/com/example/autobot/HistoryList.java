package com.example.autobot;

import android.content.Context;
import android.graphics.Bitmap;
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

        //get each textView
        TextView status = view.findViewById(R.id.status);
        TextView dateTime = view.findViewById(R.id.date_time);
        TextView userName = view.findViewById(R.id.usersname);
        TextView cost = view.findViewById(R.id.cost);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // format for the date
        String dateString = formatter.format(historyRequest.getDate());        //transform the date

        //the strings to show in the lines
        String s = "Status: " + historyRequest.getStatus();
        String d = "Date: " + dateString;
        String u = historyRequest.getUser();
        String c = "Cost " + historyRequest.getCost();

        //set the above information on the line(textView)
        status.setText(s);
        dateTime.setText(d);
        userName.setText(u);
        cost.setText(c);

        return view;
    }


}