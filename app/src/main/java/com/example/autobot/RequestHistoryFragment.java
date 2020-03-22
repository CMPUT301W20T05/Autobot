package com.example.autobot;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  //format for the date

    public View onCreateView(LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.my_request_history_page, container, false);

        Database userBase = LoginActivity.db;
        User user = LoginActivity.user;

        ArrayList<Date> newDates = new ArrayList<>();
        ArrayList<String> newStatus = new ArrayList<>();
        ArrayList<Bitmap> newBitmap = new ArrayList<>();

        userBase.collectionReference_request.whereEqualTo("Rider",user.getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                String str1 = document.getData().get("ArriveTime").toString();
                                try {
                                    dateTemp = formatter.parse(str1);
                                } catch (ParseException e) {
                                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                }
                                if (dateTemp != null) {
                                    newDates.add(dateTemp);
                                }
                                String str2 = document.getData().get("RequestStatus").toString();
                                newStatus.add(str2);
                                newBitmap.add(null);
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
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String str1 = document.getData().get("ArriveTime").toString();
                                try {
                                    dateTemp = formatter.parse(str1);
                                } catch (ParseException e) {
                                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                }
                                if (dateTemp != null) {
                                    newDates.add(dateTemp);
                                }
                                String str2 = document.getData().get("RequestStatus").toString();
                                newStatus.add(str2);
                                newBitmap.add(null);

                            }
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());

                        }
                    }
                });

        requestList = view.findViewById(R.id.requests_list);

        mDataList = new ArrayList<>();

        for (int i=0;i<newDates.size();i++){
            mDataList.add(new HistoryRequest(newStatus.get(i),newDates.get(i),newBitmap.get(i)));
        }
        mAdapter = new HistoryList(getContext(), mDataList);// set adapter

        requestList.setAdapter(mAdapter);

        return view;
    }
}
