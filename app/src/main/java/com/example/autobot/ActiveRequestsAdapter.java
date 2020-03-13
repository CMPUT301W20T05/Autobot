package com.example.autobot;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ActiveRequestsAdapter extends ArrayAdapter<Request> {
    private ArrayList<Request> active_requests;
    private Context context;
    //constructor
    public ActiveRequestsAdapter(Context context, int resource, ArrayList<Request> active_requests){
        super(context,resource,active_requests);
        //set up attributes
        this.active_requests = active_requests;
        this.context = context;
    }
    //this for setting up view for the list view
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //request ont to one plot in listview
        Request request = active_requests.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        //get the sample layout of the listview
        View view = inflater.inflate(R.layout.single_request,null);
        //set up info
        TextView example_layout = view.findViewById(R.id.active_request_example);
        example_layout.setText(request.get_active_requset_tostring());
        return view;
    }
}
