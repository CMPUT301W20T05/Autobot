package com.example.autobot;

import android.app.Activity;
import android.content.Context;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

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
        TextView start = view.findViewById(R.id.request_start);
        TextView fee = view.findViewById(R.id.request_avenue);
        TextView tips = view.findViewById(R.id.request_tips);
        //update info here
        rider_name.setText("Passenger: "+request.getRider().getUsername());
        destination.setText("Destination: "+convert_lat_to_addaress(request,request.getDestination()));
        start.setText("Start: "+convert_lat_to_addaress(request,request.getBeginningLocation()));
        fee.setText(String.format("%5.3f",request.getEstimateCost()));
        tips.setText(String.format("%3.2f",request.getTips()));
        return view;
    }

    public String convert_lat_to_addaress(Request request, LatLng latLng){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try{
            String oaddress = request.ReadableAddress(latLng,geocoder);
            return oaddress;
        }catch(IOException e){
            e.printStackTrace();
            return "";
        }
    }
}
