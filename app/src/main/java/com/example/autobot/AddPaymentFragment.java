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
    private CardTypeItemAdapter cAdapter;
    private Spinner cardType;
    private EditText cardNumber;
    private EditText holdName;
    private EditText date;
    private EditText billingAddress;
    private EditText postalCode;
    private OnFragmentInteractionListener listener;
    private CardTypeItem mCardTypeItem;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public interface OnFragmentInteractionListener {
        void onOkPressed(PaymentCard newPayment);

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
        mCardList.add(new CardTypeItem("None",R.drawable.empty));
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
                        final int icon = (int) mCardTypeItem.getCardTypelogo();// get card type icon

                        String temp = cardNumber.getText().toString();
                        long cardn = 0;
                        boolean check;
                        if (temp.replace(" ", "").length() != 0){
                            cardn = Long.parseLong(temp); // get card number
                            check = true;
                        }else{
                            check = false;
                        }
                        final String holdn = holdName.getText().toString();  // get hold name
                        temp = date.getText().toString();
                        Date edate = new Date();
                        formatter.setLenient(false);
                        try {
                            edate = formatter.parse(temp);  // make it date format
                        } catch (ParseException e) {
                            int p = 0;
                        }
                        final Date finalEdate = edate; // get expire date

                        final String baddress = billingAddress.getText().toString();
                        final String pcode = postalCode.getText().toString();
                        if (check == true) listener.onOkPressed(new PaymentCard(cardn,holdn,finalEdate,icon,baddress,pcode));
                        else Toast.makeText(getContext(), "Card number could not be empty", Toast.LENGTH_SHORT).show();

                    }
                }).create();
    }
}