package com.example.autobot;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class ShowSelectedActiveRequestFragment extends Fragment {
    private Request request;
    ButtonPress listener;

    public ShowSelectedActiveRequestFragment(Request request) {
        this.request = request;
    }

    public interface ButtonPress {
        public void confirm_request(Request request);
        public void back_press();
    }

    //attach listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ButtonPress) {
            listener = (ButtonPress) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.order_info, container, false);
        TextView drive_name = rootView.findViewById(R.id.Driver_name);
        drive_name.setText(request.getRider().getLastName());
        Button confirm = rootView.findViewById(R.id.cancel_order);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.confirm_request(request);
            }
        });
        ImageButton back_button = rootView.findViewById(R.id.button_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.back_press();
            }
        });
        return rootView;
    }
}
