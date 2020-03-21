package com.example.autobot;

import android.app.Activity;
import android.content.Context;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.type.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

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
        TextView rider_name = view.findViewById(R.id.request_rider);
        TextView destination = view.findViewById(R.id.request_destination);
        TextView fee = view.findViewById(R.id.request_avenue);
        TextView tips = view.findViewById(R.id.request_tips);
        //update info here
        rider_name.setText("Passenager: "+request.getRider().getUsername());
        destination.setText("Destinatiob: "+ convert_lat_to_addaress(request));
        fee.setText("Avenue: "+13.34);
        tips.setText("Tips: "+String.format("%d%%",10));
        return view;
    }

    public String convert_lat_to_addaress(Request request){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try{
            String oaddress = request.ReadableAddress(request.getBeginningLocation(),geocoder);
            return oaddress;
        }catch(IOException e){
            e.printStackTrace();
            return "";
        }
    }
}
