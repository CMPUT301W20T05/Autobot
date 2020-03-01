package com.example.autobot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {
    ListView listView;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_page, container, false);

        String[] settingItems = {"Account and Security","Notification","Privacy","General","Help and Feedback","About Autobot","Switch Account","Log Out"};

        listView = view.findViewById(R.id.settings_page);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,settingItems);

        listView.setAdapter(listViewAdapter);

        return view;
    }
}
