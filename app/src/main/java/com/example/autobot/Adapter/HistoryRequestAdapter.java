package com.example.autobot.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.autobot.Common.Common;
import com.example.autobot.HistoryRequest;
import com.example.autobot.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<HistoryRequest> requestList;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == Common.VIEWTYPE_GROUP){
            ViewGroup group = (ViewGroup) inflater.inflate(R.layout.group_layout,parent,false);
            GroupViewHolder groupViewHolder = new GroupViewHolder(group);
            return groupViewHolder;
        } else if (viewType == Common.VIEWTYPE_REQUEST) {
            ViewGroup group = (ViewGroup) inflater.inflate(R.layout.request_content,parent,false);
            RequestViewHolder requestViewHolder = new RequestViewHolder(group);
            return requestViewHolder;
        } else { ViewGroup group = (ViewGroup) inflater.inflate(R.layout.group_layout,parent,false);
            GroupViewHolder groupViewHolder = new GroupViewHolder(group);
            return groupViewHolder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return requestList.get(position).getViewType();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GroupViewHolder){
            GroupViewHolder groupViewHolder = (GroupViewHolder) holder;
            groupViewHolder.txt_group_title.setText(requestList.get(position).getStatus());
        } else if (holder instanceof RequestViewHolder) {
            RequestViewHolder requestViewHolder = (RequestViewHolder) holder;
            requestViewHolder.usersname.setText(requestList.get(position).getUser());
            requestViewHolder.status.setText("Status: " + requestList.get(position).getStatus());
            requestViewHolder.cost.setText("Cost " + requestList.get(position).getCost());
            requestViewHolder.date_time.setText("Date: " + requestList.get(position).getDate());
        }
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    private class GroupViewHolder extends RecyclerView.ViewHolder{
        TextView txt_group_title;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_group_title = (TextView) itemView.findViewById(R.id.txt_group_title);
        }
    }

    private class RequestViewHolder extends RecyclerView.ViewHolder{
        TextView usersname, status, date_time, cost;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            usersname = (TextView) itemView.findViewById(R.id.usersname);
            status = (TextView) itemView.findViewById(R.id.status);
            date_time = (TextView) itemView.findViewById(R.id.date_time);
            cost = (TextView) itemView.findViewById(R.id.cost);
        }
    }
}
