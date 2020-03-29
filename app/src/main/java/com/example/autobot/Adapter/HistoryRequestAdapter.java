package com.example.autobot.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.autobot.Common.Common;
import com.example.autobot.Database;
import com.example.autobot.HistoryRequest;
import com.example.autobot.LoginActivity;
import com.example.autobot.R;
import com.example.autobot.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater layoutInflater;
    private ArrayList<HistoryRequest> requestList;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // format for the date
    public Database userBase = LoginActivity.db;
    public User user = LoginActivity.user;
    private Bitmap bitmap = null;
    private String temp;
    private String userName;
    private Context context;

    public HistoryRequestAdapter(Context context, ArrayList<HistoryRequest> requestList) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = layoutInflater;
        if (viewType == Common.VIEWTYPE_GROUP){
            ViewGroup group = (ViewGroup) inflater.inflate(R.layout.group_layout,parent,false);
            GroupViewHolder groupViewHolder = new GroupViewHolder(group);
            return groupViewHolder;
        } else if (viewType == Common.VIEWTYPE_REQUEST) {
            ViewGroup group = (ViewGroup) inflater.inflate(R.layout.request_content,parent,false);
            RequestViewHolder requestViewHolder = new RequestViewHolder(group);
            return requestViewHolder;
        } else {
            ViewGroup group = (ViewGroup) inflater.inflate(R.layout.group_layout,parent,false);
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
            requestViewHolder.date_time.setText("Date: " + formatter.format(requestList.get(position).getDate()));

            requestViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HistoryRequest tempo = requestList.get(position);
                    showDetail(tempo);
                    temp = null;
                    bitmap = null;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    private static class GroupViewHolder extends RecyclerView.ViewHolder{
        TextView txt_group_title;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_group_title = itemView.findViewById(R.id.txt_group_title);
        }
    }

    private static class RequestViewHolder extends RecyclerView.ViewHolder{
        TextView usersname, status, date_time, cost;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            usersname = itemView.findViewById(R.id.usersname);
            status = itemView.findViewById(R.id.status);
            date_time = itemView.findViewById(R.id.date_time);
            cost = itemView.findViewById(R.id.cost);

        }
    }
    public void showDetail(@NonNull HistoryRequest historyRequest) {
        LayoutInflater inflater = layoutInflater;
        String rid = historyRequest.getRequestId();

        View view = inflater.inflate(R.layout.history_detail, null);

        TextView driverName = view.findViewById(R.id.detail_name);
        TextView riderName = view.findViewById(R.id.detail_name2);
        TextView sendTime = view.findViewById(R.id.detail_send_time);
        TextView acceptTime = view.findViewById(R.id.detail_accept_time);
        TextView arriveTime = view.findViewById(R.id.detail_arrive_time);
        TextView status = view.findViewById(R.id.status);
        CircleImageView photo = view.findViewById(R.id.detail_photo);

        if (rid != null) {
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
                                    String Status = "Status:  " + document.getData().get("RequestStatus").toString();

                                    String test = document.getData().get("Driver").toString();
//                                    Toast.makeText(context, test, Toast.LENGTH_SHORT).show(); // print driver's name
                                    if (test.equals("")) {
                                        temp = null;
                                    } else {
                                        temp = userBase.rebuildUser(test).getUri();
                                    }

                                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                    StrictMode.setThreadPolicy(policy);
                                    try {
                                        if (temp != null) {
                                            bitmap = BitmapFactory.decodeStream((InputStream) new URL(temp).getContent());
                                            photo.setImageBitmap(bitmap);
                                        }
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

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
            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setView(view)
                    .setTitle("Details")
                    .setNegativeButton("Close", null);
            alert.show();
        }

    }
}
