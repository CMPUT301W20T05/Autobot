package com.example.autobot;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EditProfilePage extends Fragment {
    private EditText firstName;
    private EditText lastName;
    private EditText phoneNumber;
    private EditText emailAddress;
    private EditText homeAddress;
    private EditText eContact;
    private Button btn;
    private TextView UserName;
    private Button Cancel;
    private EditProfilePageListener listener;

    public interface EditProfilePageListener {
        void updateInformation(String FirstName, String LastName, String PhoneNumber, String EmailAddress, String HomeAddress, String emergencyContact);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EditProfilePageListener) {
            listener = (EditProfilePageListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EditProfilePageListener");
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String username = bundle.getString("username");
        View view = inflater.inflate(R.layout.edit_contact_infor_activity, container, false);

        firstName = view.findViewById(R.id.editTextFirstName);
        lastName = view.findViewById(R.id.editTextLastName);
        emailAddress = view.findViewById(R.id.editTextEmail);
        homeAddress = view.findViewById(R.id.editTextHomeAddress);
        eContact = view.findViewById(R.id.editTextEmergencyContact);
        UserName = view.findViewById(R.id.Username);
        Cancel = view.findViewById(R.id.button_cancel);
        UserName.setText(username);

        Database db = new Database();

        btn = view.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName = firstName.getText().toString();
                String lName = lastName.getText().toString();
                String pNumber = phoneNumber.getText().toString();
                String eAddress = emailAddress.getText().toString();
                String hAddress = homeAddress.getText().toString();
                String econtact = eContact.getText().toString();

                listener.updateInformation(fName,lName,pNumber,eAddress,hAddress,econtact);
                //getFragmentManager().beginTransaction().remove(EditProfilePage.this).commit();
                getActivity().onBackPressed();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }
}