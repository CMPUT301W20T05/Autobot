package com.example.autobot.Common;


import android.view.ViewStructure;

import com.example.autobot.HistoryRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
/**
 * this is a class of Common functions, which could be used for other classes
 */
public class Common {
    public static final int VIEWTYPE_GROUP = 0;
    public static final int VIEWTYPE_REQUEST = 1;

    /**
     * This function adds headers to the Request History Fragment
     * @param list An ArrayList contains HistoryRequests
     */
    public static ArrayList<HistoryRequest> addHeader(ArrayList<HistoryRequest> list){
        int i = 0;
        ArrayList<HistoryRequest> customList = new ArrayList<>();

        HistoryRequest firstPosition = new HistoryRequest(list.get(i).getStatus(), new Date(), "gg", "gg", 1, VIEWTYPE_GROUP);
        customList.add(firstPosition);
        for (i = 0;i < list.size()-1;i++){
            HistoryRequest historyRequest = new HistoryRequest(list.get(i).getStatus(), new Date(), "gg", "gg", 1, VIEWTYPE_GROUP);
            String string1 = list.get(i).getStatus();
            String string2 = list.get(i+1).getStatus();
            if (string1.equals(string2)){
                list.get(i).setViewType(VIEWTYPE_REQUEST);
                customList.add(list.get(i));
            }else{
                list.get(i).setViewType(VIEWTYPE_REQUEST);
                customList.add(list.get(i));
                historyRequest.setStatus(string2);
                historyRequest.setViewType(VIEWTYPE_GROUP);
                customList.add(historyRequest);
            }
        }
        list.get(i).setViewType(VIEWTYPE_REQUEST);
        customList.add(list.get(i));
        return customList;
    }
    /**
     * This function sorts the request by date inside the ArrayList of Request Histories
     * @param historyRequests an ArrayList of HistoryRequest
     */
    public static ArrayList<HistoryRequest> sortListByDate(ArrayList<HistoryRequest> historyRequests){
        Collections.sort(historyRequests, new Comparator<HistoryRequest>() {
            @Override
            public int compare(HistoryRequest historyRequest, HistoryRequest t1) {
                return historyRequest.getDate().compareTo(t1.getDate());
            }
        });
        //Collections.sort(historyRequests, Collections.reverseOrder());
        return historyRequests;
    }
    /**
     * This function sorts the request by Status inside the ArrayList of Request Histories
     * @param historyRequests an ArrayList of HistoryRequest
     */
    public static ArrayList<HistoryRequest> sortListByStatus(ArrayList<HistoryRequest> historyRequests){
        Collections.sort(historyRequests, new Comparator<HistoryRequest>() {
            @Override
            public int compare(HistoryRequest historyRequest, HistoryRequest t1) {
                return historyRequest.getStatus().compareTo(t1.getStatus());
            }
        });
        return historyRequests;
    }


}