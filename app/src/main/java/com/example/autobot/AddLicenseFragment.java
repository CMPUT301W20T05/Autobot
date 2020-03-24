package com.example.autobot;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddLicenseFragment extends DialogFragment {
    EditText input_driving_license;
    EditText input_car_license;
    AddLicenseFragment.OnFragmentInteractionListener listener;

    public AddLicenseFragment(){
        super();
    }

    public interface OnFragmentInteractionListener{
        public void get_license_input(String driving_license,String car_license);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener){
            listener = (AddLicenseFragment.OnFragmentInteractionListener) context;
        } else{
            throw new RuntimeException(context.toString());
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.license,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        input_driving_license = view.findViewById(R.id.driver_license);
        input_car_license = view.findViewById(R.id.car_license);
        AlertDialog.Builder sbuilder = new AlertDialog.Builder(getContext());
        return sbuilder.setView(view).setTitle("Input license").setNegativeButton("cancel",null).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                  String driver_license = input_driving_license.getText().toString();
                  String car_license = input_car_license.getText().toString();
                  listener.get_license_input(driver_license,car_license);
            }
        }).create();}

}
