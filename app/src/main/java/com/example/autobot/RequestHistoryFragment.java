package com.example.autobot;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autobot.Adapter.HistoryRequestAdapter;
import com.example.autobot.Common.Common;
import com.example.autobot.Common.LinearLayoutManagerWithSmoothScroller;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.android.volley.VolleyLog.TAG;

public class RequestHistoryFragment extends Fragment {

    private Date dateTemp;
    private String requestId;
    Database userBase = LoginActivity.db;
    User user = LoginActivity.user;
    private Bitmap bitmap = null;
    private String temp;
    private String userName;

    private ArrayList<HistoryRequest> requestArrayList;
    private HistoryRequestAdapter adapter;
    LinearLayoutManager layoutManager;

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  //format for the date

    public View onCreateView(LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_request_history_page, container, false);

        RecyclerView recyclerView_request = (RecyclerView) view.findViewById(R.id.recycler_request);

        layoutManager = new LinearLayoutManagerWithSmoothScroller(getContext());
        recyclerView_request.setLayoutManager(layoutManager);

        requestArrayList = new ArrayList<>();

        userBase.collectionReference_request
                .whereEqualTo(user.getUserType(), user.getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                String time1 = document.getData().get("ArriveTime").toString();
                                userName = document.getData().get("Driver").toString();
                                requestId = document.getData().get("ID").toString();
                                String userName1 = "Driver: " + userName;
                                if (!time1.equals("0002-11-30 00:00:00")) {
                                    try {
                                        dateTemp = formatter.parse(time1);
                                    } catch (ParseException e) {
                                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    String time2 = document.getData().get("AcceptTime").toString();
                                    if (!time2.equals("0002-11-30 00:00:00")) {
                                        try {
                                            dateTemp = formatter.parse(time2);
                                        } catch (ParseException e) {
                                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        String time3 = document.getData().get("SendTime").toString();
                                        try {
                                            dateTemp = formatter.parse(time3);
                                        } catch (ParseException e) {
                                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                String str = document.getData().get("RequestStatus").toString();
                                float cost = Float.parseFloat(document.getData().get("Cost").toString());

                                requestArrayList.add(new HistoryRequest(str, dateTemp, userName1, requestId, cost, 1));

                            }
                            if (requestArrayList.size() != 0){
                                //requestArrayList = Common.sortListByDate(requestArrayList); // sort
                                requestArrayList = Common.sortListByStatus(requestArrayList); // same
                                requestArrayList = Common.addHeader(requestArrayList); // add header
                                adapter = new HistoryRequestAdapter(getContext(), requestArrayList);
                                recyclerView_request.setAdapter(adapter);}

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());

                        }
                    }
                });

        return view;
    }
}
