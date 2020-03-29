package com.example.autobot;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autobot.Adapter.HistoryRequestAdapter;
import com.example.autobot.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class PaymentInformationFragment extends Fragment {
    ListView paymentList;
    ArrayAdapter<PaymentCard> mAdapter;
    ArrayList<PaymentCard> mDataList;
    Database userBase = LoginActivity.db;
    User user = LoginActivity.user;
    String holdName;
    long cardNumber;
    Date dateTemp;
    String time1;
    String billingAddress;
    String postalCode;
    int cardLogo;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  //format for the date

    public void updateList(PaymentCard paymentCard){
        mDataList.add(paymentCard);
        mAdapter.notifyDataSetChanged();
    }

    public View onCreateView(LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.payment_list_page, container, false);

        paymentList = (ListView) view.findViewById(R.id.payment_listView);

        mDataList = new ArrayList<>();

        userBase.collectionReference_payment
                .whereEqualTo("UserName", user.getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                time1 = document.getData().get("ExpireDate").toString();
                                holdName = document.getData().get("HoldName").toString();
                                cardNumber = Long.parseLong(document.getData().get("CardNumber").toString());
                                billingAddress = document.getData().get("BillingAddress").toString();
                                postalCode = document.getData().get("PostalCode").toString();
                                cardLogo = Integer.parseInt(document.getData().get("CardType").toString());
                                try {
                                    dateTemp = formatter.parse(time1);
                                    mDataList.add(new PaymentCard(cardNumber,user.getUsername(),holdName,dateTemp,cardLogo,billingAddress,postalCode));
                                } catch (ParseException e) {
                                    Toast.makeText(getContext(), "Error loading", Toast.LENGTH_SHORT).show();
                                }
                            }

                            mAdapter = new PaymentCardList(getContext(), mDataList);// set adapter

                            paymentList.setAdapter(mAdapter);

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());

                        }
                    }
                });

        FloatingActionButton btn = view.findViewById(R.id.add_payment_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddPaymentFragment().show(getParentFragmentManager(), "ADD_CITY");
                //Toast.makeText(getContext(), "jump", Toast.LENGTH_SHORT).show();
            }
        });

        paymentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PaymentCard paymentCard = mDataList.get(i);
                showDetail(paymentCard,i);
            }
        });

        // Wallet
        LinearLayout wallet = view.findViewById(R.id.wallet);
        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Wallet_fragment wallet_fragment = new Wallet_fragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,wallet_fragment,"WALLET_FRAGMENT");
                fragmentTransaction.commit();
                getActivity().setTitle("Wallet");
            }
        });

        return view;
    }

    private void showDetail(@NonNull PaymentCard paymentCard,int position) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.payment_detail,null);

        TextView cnumber = view.findViewById(R.id.card_number);
        TextView cname = view.findViewById(R.id.card_name);
        TextView cdate = view.findViewById(R.id.card_date);
        TextView caddress = view.findViewById(R.id.card_address);
        TextView cpcode = view.findViewById(R.id.card_postal_code);

        cnumber.setText("Card number:   "+paymentCard.getCardNumber());
        cname.setText("Holder name:   "+paymentCard.getHoldName());
        cdate.setText("Expire date:   "+formatter.format(paymentCard.getExpireDate()));
        caddress.setText("Billing address:   "+paymentCard.getBillingAddress());
        cpcode.setText("Postal code:   "+paymentCard.getPostalCode());

        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(view)
                .setTitle("Card Detail")
                .setNegativeButton("Close", null)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDataList.remove(position);
                        mAdapter.notifyDataSetChanged();
                        userBase.collectionReference_payment.document(paymentCard.getCardNumber().toString()).delete();
                    }
                }).create();
        alert.show();
    }
}
