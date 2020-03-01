package com.example.autobot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PaymentCardList extends ArrayAdapter<PaymentCard> { // this is a adapter
    private ArrayList<PaymentCard> paymentCards;
    private Context context;

    public PaymentCardList(Context context, ArrayList<PaymentCard> paymentCards) {
        super(context,0,paymentCards);
        this.paymentCards = paymentCards;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.payment_content,parent,false);
        }
        PaymentCard paymentCard = paymentCards.get(position);

        TextView cardNumber = view.findViewById(R.id.card_number_textView);
        TextView holdName = view.findViewById(R.id.hold_name_textView);
        TextView expireDate = view.findViewById(R.id.expire_date_textView);
        ImageView cardLogo = view.findViewById(R.id.payment_logo_ImgView);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // format for the date
        String dateString = formatter.format(paymentCard.getExpireDate());        //transform the date

        String cn = "Card Number: " + paymentCard.getCardNumber();                       //the strings to show in the lines
        String n = "Name: " + paymentCard.getHoldName();
        String d = "Expire Date: " + dateString;
        int l = paymentCard.getCardLogo();

        cardNumber.setText(cn);        //set the above information on the line(textView)
        holdName.setText(n);
        expireDate.setText(d);
        cardLogo.setImageResource(l);

        return view;
    }
}
