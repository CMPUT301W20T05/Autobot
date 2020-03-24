package com.example.autobot;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class ShowSelectedActiveRequestFragment extends Fragment {
    private Request request;
    ButtonPress listener;

    public ShowSelectedActiveRequestFragment(Request request) {
        this.request = request;
    }

    public interface ButtonPress {
        public void confirm_request(Request request);
        public void back_press();
    }

    //attach listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ButtonPress) {
            listener = (ButtonPress) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.order_info, container, false);
        TextView drive_name = rootView.findViewById(R.id.Driver_name);
        drive_name.setText(request.getRider().getUsername());
        TextView cost = rootView.findViewById(R.id.Appro_price);
        TextView distance = rootView.findViewById(R.id.Appro_distance);
        TextView origin = rootView.findViewById(R.id.origin_loc);
        TextView destination = rootView.findViewById(R.id.Destination);
        distance.setText(String.format("%4.3f",get_distance(request.getBeginningLocation(),request.getDestination()))+"km");
        origin.setText(convert_latng_to_address(request.getBeginningLocation()));
        destination.setText(convert_latng_to_address(request.getDestination()));
        cost.setText(String.format("%4.3f",request.getEstimateCost()));
        Button confirm = rootView.findViewById(R.id.cancel_order);
        confirm.setText("CONFIRM");
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.confirm_request(request);
            }
        });
//        ImageButton back_button = rootView.findViewById(R.id.button_back);
//        back_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.back_press();
//            }
//        });


        //view profilo
        ImageView see_profile_button = rootView.findViewById(R.id.imageViewAvatar); // 需改button id
        see_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.profile_viewer, null);


                TextView fname = view.findViewById(R.id.FirstName);
                TextView lname = view.findViewById(R.id.LastName);
                TextView pnumber = view.findViewById(R.id.PhoneNumber);
                TextView email = view.findViewById(R.id.EmailAddress);

                User rider = request.getRider();
                fname.setText(rider.getFirstName());
                lname.setText(rider.getLastName());
                pnumber.setText(rider.getPhoneNumber());
                email.setText(rider.getEmailAddress());

                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setView(view)
                        .setTitle("Details")
                        .setNegativeButton("Close",null);
                alert.show();
            }
        });


        return rootView;
    }

    public String convert_latng_to_address(LatLng location){
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            String address = request.ReadableAddress(location, geocoder);
            return address;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public double get_distance(LatLng start,LatLng end){
        return Math.round(SphericalUtil.computeDistanceBetween(start,end))/1000;
    }
}
