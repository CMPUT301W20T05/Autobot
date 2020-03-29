package com.example.autobot;
import android.content.Context;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.autobot.Adapter.HistoryRequestAdapter;
import com.example.autobot.Adapter.WalletAdapter;
import com.example.autobot.Common.Common;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;


public class Wallet_fragment extends Fragment {
    Database userBase = LoginActivity.db;
    User user = LoginActivity.user;


    ListView informationList;
    WalletAdapter informationAdapter;
    ArrayList<WalletInformation> informationDataList;


    public View onCreateView(LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wallet, container, false);
        informationList = view.findViewById(R.id.wallet_listView);
        informationDataList = new ArrayList<>();

        userBase.collectionReference_request
                .whereEqualTo(user.getUserType(), user.getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String status = document.getData().get("RequestStatus").toString();
                                if (status.equals("Trip Completed")) {
                                    String time = document.getData().get("SendTime").toString();
                                    String cost = document.getData().get("Cost").toString();
                                    LatLng latLng = new LatLng(Double.valueOf((String) document.getString("DestinationLat")), Double.valueOf((String) document.getString("DestinationLnt")));
                                    String destination = null;
                                    try {
                                        destination = convert_lat_to_addaress(latLng);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    informationDataList.add(new WalletInformation(destination, cost, time));
                                }
                            }

                            informationAdapter = new WalletAdapter(getContext(), informationDataList);
                            informationList.setAdapter(informationAdapter);

                        }
                    }
                });





        TextView user_name = view.findViewById(R.id.username_wallet);
        TextView balance = view.findViewById(R.id.balance);
        user_name.setText(user.getUsername());
        balance.setText(user.getBalance());

        return view;
    }

    public String convert_lat_to_addaress(LatLng latLng) throws ParseException {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        Request request = new Request();
        try{
            String oaddress = request.ReadableAddress(latLng,geocoder);
            return oaddress;
        }catch(IOException e){
            e.printStackTrace();
            return "";
        }
    }
}

