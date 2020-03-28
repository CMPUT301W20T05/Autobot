package com.example.autobot;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class AddPaymentFragment extends DialogFragment {
    private ArrayList<CardTypeItem> mCardList;
    private ArrayList<String > tempList;
    private CardTypeItemAdapter cAdapter;
    private Spinner cardType;
    private EditText cardNumber;
    private EditText holdName;
    private EditText date;
    private EditText billingAddress;
    private EditText postalCode;
    private OnFragmentInteractionListener listener;
    private CardTypeItem mCardTypeItem;
    public Database userBase = LoginActivity.db;
    public User user = LoginActivity.user;
    String defaultTimeString = "0000-00-00";
    Date edate = new Date();
    public Fragment fragment;

    public interface OnFragmentInteractionListener {
        void onOkPressed(PaymentCard newPayment, int i);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_payment_frg, null);

        TextView title = new TextView(getContext()); // set title
        title.setText("Add Payment Information");
        title.setPadding(10, 80, 10, 10);
        title.setGravity(Gravity.CENTER); // set gravity to center
        title.setTextSize(23);  // set title text size
        title.setTextColor(getResources().getColor(R.color.black));

        cardType = view.findViewById(R.id.cardTypeSpinner);
        cardNumber = view.findViewById(R.id.add_cardNumber_editText);
        holdName = view.findViewById(R.id.add_holdName);
        date = view.findViewById(R.id.add_expireDate);
        billingAddress = view.findViewById(R.id.add_billingAddress);
        postalCode = view.findViewById(R.id.add_postalCode);

        mCardList = new ArrayList<CardTypeItem>();
        mCardList.add(new CardTypeItem("Visa",R.drawable.visa));
        mCardList.add(new CardTypeItem("Master Card",R.drawable.master));
        mCardList.add(new CardTypeItem("Others",R.drawable.empty));
        cAdapter = new CardTypeItemAdapter(getContext(),mCardList);
        cardType.setAdapter(cAdapter);

        cardType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCardTypeItem = (CardTypeItem) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()); // build a AlertDialog

        return builder.setView(view)
                .setCustomTitle(title)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int testInt1 = 1;
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                        final int icon = (int) mCardTypeItem.getCardTypelogo();// get card type icon

                        // get card number
                        String temp = cardNumber.getText().toString();
                        long cardn = 0;
                        boolean check;
                        if (temp.replace(" ", "").length() != 0){
                            if (temp.replace(" ", "").length() != 16){
                                Toast.makeText(getContext(), "Please enter a 16 digit card number", Toast.LENGTH_SHORT).show();
                            } else {
                                cardn = Long.parseLong(temp); // get card number
                                testInt1 = 0;
                            }
                        }else{
                            check = false;
                        }

                        //get holder's name
                        int testInt2 = 1;
                        String holdn = "";
                        if (testInt1 == 0 ){
                            holdn = holdName.getText().toString();
                            if (holdn.replace(" ","").equals("")) {
                                Toast.makeText(getContext(), "Holder name could not be empty", Toast.LENGTH_SHORT).show();
                            }
                            else testInt2 = 0;
                        }

                        // get expire date
                        int testInt3 = 1;
                        if (testInt2 == 0) {
                            String temp1 = date.getText().toString();
                            formatter.setLenient(false);
                            if (temp1.replace(" ", "").length() != 0) { //check if it is empty
                                if(isNumeric(temp1.replace("-", ""))) { //check if it is integer out of "-"
                                    try {
                                        edate = formatter.parse(temp1);  // make it date format
                                        testInt3 = 0;
                                    } catch (ParseException e) {
                                        Toast.makeText(getContext(), "Please enter a correct format date.", Toast.LENGTH_SHORT).show();
                                    }
                                } else Toast.makeText(getContext(), "Please enter a correct format date.", Toast.LENGTH_SHORT).show();
                            } else Toast.makeText(getContext(), "Expire date could not be empty", Toast.LENGTH_SHORT).show();
                        }
                        //get billing address
                        int testInt4 = 1;
                        String baddress = "";
                        if (testInt3 == 0){
                            baddress = billingAddress.getText().toString();
                            if (baddress.replace(" ","").equals("")) {
                                Toast.makeText(getContext(), "Billing address could not be empty", Toast.LENGTH_SHORT).show();
                            } else testInt4 = 0;
                        }

                        //get postal code
                        int testInt5 = 1;
                        String pcode = "";
                        if (testInt4 == 0){
                            pcode = postalCode.getText().toString();
                            if (pcode.replace(" ","").equals("")) {
                                Toast.makeText(getContext(), "Postal code could not be empty", Toast.LENGTH_SHORT).show();
                            }else testInt5=0;
                        }

                        long finalCardn = cardn;
                        String finalHoldn = holdn;
                        String finalBaddress = baddress;
                        String finalPcode = pcode;
                        if (testInt1 == 0 && testInt2 == 0 && testInt3 == 0 && testInt4 == 0 && testInt5 == 0){
                            userBase.collectionReference_payment
                                    .whereEqualTo("CardNumber",cardn+"")
                                    .whereEqualTo("UserName", user.getUsername())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                int i = 0;
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    i += 1;
                                                }
                                                listener.onOkPressed(new PaymentCard(finalCardn,user.getUsername(), finalHoldn,edate,icon, finalBaddress, finalPcode), i);
                                                if (i == 0) userBase.add_new_Payment(new PaymentCard(finalCardn,user.getUsername(), finalHoldn,edate,icon, finalBaddress, finalPcode));
                                            }
                                            else {
                                            }
                                        }
                                    });
                        }
                    }
                }).create();
    }
    public static boolean isNumeric(String str) { //function to check if it is integer
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}