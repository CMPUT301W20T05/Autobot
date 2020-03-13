package com.example.autobot;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RequestHistoryFragment extends Fragment {

    private ListView requestList;
    private ArrayAdapter<HistoryRequest> mAdapter;
    private ArrayList<HistoryRequest> mDataList;
    private RequestHistoryListener listener;
    private Database db;

    public interface RequestHistoryListener {
        String getUsername();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RequestHistoryListener) {
            listener = (RequestHistoryListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RequestHistoryListener");
        }
    }
    // use temp data first, later use database's data

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  //format for the date

    Date date1, date2, date3;
    {try {
        date1 = formatter.parse("2020-02-02");
    } catch (ParseException e) {
        int a = 1;
    }}
    {try {
        date2 = formatter.parse("2020-02-22");
    } catch (ParseException e) {
        int b = 1;
    }}
    {try {
        date3 = formatter.parse("2020-01-06");
    } catch (ParseException e) {
        int c = 1;
    }}


    Date[] dates ={date1,date2,date3};
    int[] imgs = {R.drawable.default_avatar, R.drawable.default_avatar,R.drawable.default_avatar};
    Integer[] request_numbers = {123,222,111};
    String[] status = {"Completed", "Accepted", "Canceled"};


    public View onCreateView(LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.my_request_history_page, container, false);

        db = HomePageActivity.db;
        User user = db.rebuildUser(listener.getUsername());


        requestList = (ListView) view.findViewById(R.id.requests_list);

        mDataList = new ArrayList<>();

        for (int i=0;i<dates.length;i++){
            mDataList.add(new HistoryRequest(request_numbers[i],status[i],dates[i],imgs[i]));
        }
        mAdapter = new HistoryList(getContext(), mDataList);// set adapter

        requestList.setAdapter(mAdapter);

        return view;
    }
}
