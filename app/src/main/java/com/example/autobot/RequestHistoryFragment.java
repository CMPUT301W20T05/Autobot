package com.example.autobot;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.android.volley.VolleyLog.TAG;

public class RequestHistoryFragment extends Fragment {

    private ListView requestList;
    private ArrayAdapter<HistoryRequest> mAdapter;
    private ArrayList<HistoryRequest> mDataList;
    private Date dateTemp;
    private String requestId;
    Database userBase = LoginActivity.db;
    User user = LoginActivity.user;

    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");  //format for the date

    public View onCreateView(LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.my_request_history_page, container, false);

        requestList = view.findViewById(R.id.requests_list);

        mDataList = new ArrayList<>();

        mAdapter = new HistoryList(getContext(), mDataList);// set adapter

        requestList.setAdapter(mAdapter);

        userBase.collectionReference_request.whereEqualTo("Rider",user.getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                String time1 = document.getData().get("ArriveTime").toString();
                                String userName = document.getData().get("Driver").toString();
                                requestId =  document.getData().get("ID").toString();
                                userName = "Driver: "+userName;
                                if (!time1.equals("30-11-002 12:00:00")){
                                    try {
                                        dateTemp = formatter.parse(time1);
                                    } catch (ParseException e) {
                                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    String time2 = document.getData().get("AcceptTime").toString();
                                    if (!time2.equals("30-11-002 12:00:00")){
                                        try {
                                            dateTemp = formatter.parse(time2);
                                        } catch (ParseException e) {
                                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        String time3 = document.getData().get("SendTime").toString();
                                        try {
                                            dateTemp = formatter.parse(time3);
                                        } catch (ParseException e) {
                                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                String str = document.getData().get("RequestStatus").toString();

                                if (dateTemp != null) {
                                    mDataList.add(new HistoryRequest(str,dateTemp,null,userName,requestId));
                                    mAdapter.notifyDataSetChanged();
                                }

                            }
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());

                        }
                    }
                });
        userBase.collectionReference_request.whereEqualTo("Driver",user.getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                String time1 = document.getData().get("ArriveTime").toString();
                                String userName = document.getData().get("Rider").toString();
                                userName = "Rider: "+userName;
                                if (!time1.equals("30-11-002 12:00:00")){
                                    try {
                                        dateTemp = formatter.parse(time1);
                                    } catch (ParseException e) {
                                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    String time2 = document.getData().get("AcceptTime").toString();
                                    if (!time2.equals("30-11-002 12:00:00")){
                                        try {
                                            dateTemp = formatter.parse(time2);
                                        } catch (ParseException e) {
                                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        String time3 = document.getData().get("SendTime").toString();
                                        try {
                                            dateTemp = formatter.parse(time3);
                                        } catch (ParseException e) {
                                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                String str = document.getData().get("RequestStatus").toString();

                                if (dateTemp != null) {
                                    mDataList.add(new HistoryRequest(str,dateTemp,null,userName,requestId));
                                    mAdapter.notifyDataSetChanged();
                                }

                            }
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());

                        }
                    }
                });
        requestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HistoryRequest tempo = mDataList.get(i);
                showDetail(tempo);
            }
        });

        return view;
    }

    private void showDetail(HistoryRequest historyRequest) {
        String rid = historyRequest.getRequestId();

        View view = LayoutInflater.from(getContext()).inflate(R.layout.history_detail, null);
        TextView driverName = view.findViewById(R.id.detail_name);
        TextView riderName = view.findViewById(R.id.detail_name2);
        TextView sendTime = view.findViewById(R.id.detail_send_time);
        TextView acceptTime = view.findViewById(R.id.detail_accept_time);
        TextView arriveTime = view.findViewById(R.id.detail_arrive_time);
        TextView status = view.findViewById(R.id.status);

        userBase.collectionReference_request
                .document(rid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {  // display username on navigation view
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String driver = "Driver:  " + document.getData().get("Driver").toString();
                                String rider = "Rider:  " + document.getData().get("Rider").toString();

                                String tempTime1 = document.getData().get("AcceptTime").toString();
                                String facceptTime = "Accept Time: ";
                                if (!tempTime1.equals("30-11-002 12:00:00")) {
                                    facceptTime = "Accept Time:  " + tempTime1;
                                }

                                String farriveTime = "Arrive Time: ";
                                String tempTime2 = document.getData().get("ArriveTime").toString();
                                if (!tempTime2.equals("30-11-002 12:00:00")) {
                                    farriveTime = "Arrive Time:  " + tempTime2;
                                }

                                String fsendTime = "Send Time:  " + document.getData().get("SendTime").toString();
                                String Status = "Status:  " + document.getData().get("RequestStatus").toString();;

                                driverName.setText(driver);
                                riderName.setText(rider);
                                sendTime.setText(fsendTime);
                                acceptTime.setText(facceptTime);
                                arriveTime.setText(farriveTime);
                                status.setText(Status);

                            }
                        }
                    }
                });
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(view)
                .setTitle("Details")
                .setNegativeButton("Close",null);
        alert.show();

    }
}
