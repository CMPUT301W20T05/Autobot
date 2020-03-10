package com.example.autobot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class PaymentInformationFragment extends Fragment implements AddPaymentFragment.OnFragmentInteractionListener {
    ListView paymentList;
    ArrayAdapter<PaymentCard> mAdapter;
    ArrayList<PaymentCard> mDataList;


    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  //format for the date

    Date date1, date2, date3;
    {try {
        date1 = formatter.parse("2020-02-02");
    } catch (ParseException e) {
        int a = 1;
    }}
    {try {
        date2 = formatter.parse("2020-02-22");
    } catch (ParseException e) {
        int b = 1;
    }}
    {try {
        date3 = formatter.parse("2020-01-06");
    } catch (ParseException e) {
        int c = 1;
    }}


    Date[] expire_dates ={date1,date2,date3};
    int[] logos = {R.drawable.master, R.drawable.master,R.drawable.master};
    Long[] card_numbers = {2222111133334444L,2222111133335555L,1111666644443333L};
    String[] hold_names = {"Tim James", "Allen Jones", "James Lord"};
    String[] billing_address = {"8510 111St", "8888 201St", "8231 102St"};
    String[] postal_code = {"T6G 1H7", "T3B 2U8", "E2G 0I1"};


    public View onCreateView(LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.payment_list_page, container, false);

        paymentList = (ListView) view.findViewById(R.id.payment_listView);
        mDataList = new ArrayList<>();

        for (int i=0;i<expire_dates.length;i++){
            mDataList.add(new PaymentCard(card_numbers[i],hold_names[i],expire_dates[i],logos[i],billing_address[i],postal_code[i]));
        }

        mAdapter = new PaymentCardList(getContext(), mDataList);// set adapter

        paymentList.setAdapter(mAdapter);

        FloatingActionButton btn = view.findViewById(R.id.add_payment_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddPaymentFragment().show(getParentFragmentManager(), "ADD_CITY");

                //Toast.makeText(getContext(), "jump", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    public void onOkPressed(PaymentCard newPayment) {
        mDataList.add(newPayment);
        mAdapter.notifyDataSetChanged();
    }

}
