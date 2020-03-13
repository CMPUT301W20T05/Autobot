package com.example.autobot;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.firestore.util.Assert;

import java.util.ArrayList;

public class ActiverequestsFragment extends Fragment{
    ListView requests_view;
    ArrayList<Request> requests_list;
    //set the interface as listener
    OnBackPressed listener;
    FragmentManager fm;
    public interface OnBackPressed {
        void hide();
        void show_detail(ShowSelectedActiveRequestFragment showSelectedActiveRequestFragment);
    }

    public ActiverequestsFragment(ArrayList<Request> requests_list){
        this.requests_list = requests_list;
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof OnBackPressed){
            listener = (OnBackPressed) context;
        } else{
            throw new RuntimeException(context.toString());
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View rootView = inflater.inflate(R.layout.show_requirement_fragment,container,false);
        //initial all attributes
        requests_view = rootView.findViewById(R.id.active_requests);

        //those code is for testing----------------------------
        User rider;
        rider = new User();
        rider.setLastName("jc");
        //Location location = new Location("");
        Request request1 = new Request();

        request1.setRider(rider);
        //request1.setDestination();
        requests_list.add(request1);
        //----------------------------------------------------

        //bound data and adapter to list view
        requests_view.setAdapter(new ActiveRequestsAdapter(getActivity(),0,requests_list));

        //set up list view listener
        rootView.setOnClickListener(new View.OnClickListener() {
            //if touch the outside area of the fragment, the active requests list fragment will disappear
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.active_requests:
                        break;
                    default:
                        //call this function to popback the previous fragment
                        listener.hide();
                        Toast.makeText(getActivity(),"touch out of area",Toast.LENGTH_LONG).show();
                }
            }
        });

        //set up listview listener
        requests_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),requests_list.get(position).get_active_requset_tostring(),Toast.LENGTH_SHORT).show();
                //show the detail of the clicked request
                listener.show_detail(new ShowSelectedActiveRequestFragment(requests_list.get(position))); }
        });
        return rootView;
    }

}
