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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EditProfilePage extends Fragment {
    private EditText firstName;
    private EditText lastName;
    private EditText emailAddress;
    private EditText homeAddress;
    private EditText eContact;
    private Button btn;
    private EditProfilePageListener listener;
    private Database db;

    public interface EditProfilePageListener {
        void updateInformation(String FirstName, String LastName, String EmailAddress, String HomeAddress, String emergencyContact);
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
        View view = inflater.inflate(R.layout.edit_contact_infor_activity, container, false);

        firstName = view.findViewById(R.id.editTextFirstName);
        lastName = view.findViewById(R.id.editTextLastName);
        emailAddress = view.findViewById(R.id.editTextEmail);
        homeAddress = view.findViewById(R.id.editTextHomeAddress);
        eContact = view.findViewById(R.id.editTextEmergencyContact);

        db = HomePageActivity.db;

        btn = view.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName = firstName.getText().toString();
                String lName = lastName.getText().toString();
                String eAddress = emailAddress.getText().toString();
                String hAddress = homeAddress.getText().toString();
                String econtact = eContact.getText().toString();

                listener.updateInformation(fName,lName,eAddress,hAddress,econtact);
                getActivity().onBackPressed();
            }
        });

        return view;
    }
}