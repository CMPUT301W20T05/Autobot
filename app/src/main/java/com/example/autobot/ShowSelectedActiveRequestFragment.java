package com.example.autobot;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class ShowSelectedActiveRequestFragment extends Fragment {
    private Request request;
    ButtonPress listener;
    private static final int REQUEST_PHONE_CALL = 101;

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
        set_profile_picture(rootView);
        confirm.setText("CONFIRM");
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.confirm_request(request);
            }
        });

        ImageButton phone = rootView.findViewById(R.id.phoneButton);
        ImageButton email = rootView.findViewById(R.id.emailButton);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = false;
                String phoneNumber = request.getRider().getPhoneNumber();
                if (!phoneNumber.equals("")) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + phoneNumber));//change the number.
                    while (!success) {
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getContext(), "No permission for calling", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                        } else {
                            success = true;
                            startActivity(callIntent);
                        }
                    }
                }
                else {
                    Toast.makeText(getContext(), "No phone number provided", Toast.LENGTH_LONG).show();
                }
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = request.getRider().getEmailAddress();
                if (!email.equals("")) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), "No email address provided", Toast.LENGTH_LONG).show();
                }
            }
        });


        //view profilo
        drive_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.profile_viewer, null);
                ImageView avatar = view.findViewById(R.id.profileAvatar);
                TextView fname = view.findViewById(R.id.FirstName);
                TextView lname = view.findViewById(R.id.LastName);
                TextView pnumber = view.findViewById(R.id.PhoneNumber);
                TextView email = view.findViewById(R.id.EmailAddress);

                User rider = request.getRider();
                try {
                    Bitmap temp = BitmapFactory.decodeStream((InputStream)new URL(rider.getUri()).getContent());
                    avatar.setImageBitmap(temp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fname.setText(rider.getFirstName());
                lname.setText(rider.getLastName());
                pnumber.setText(rider.getPhoneNumber());
                email.setText(rider.getEmailAddress());
                Log.d("checkuser",rider.getUserType()+"hi");
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void set_profile_picture(View view){
        ImageView profile = view.findViewById(R.id.imageViewAvatar);
        //if user dose have profile,get the url of pic resource
        String url = request.getRider().getUri();
        try {
            Bitmap profile_pic= BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
            //profile.setAdjustViewBounds(true);
            profile.setImageBitmap(profile_pic);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e){
        }
    }
}
