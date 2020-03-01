package com.example.autobot;

import android.content.Context;
import android.media.Image;
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

public class CardTypeItemAdapter extends ArrayAdapter<CardTypeItem> {

    public CardTypeItemAdapter(Context context, ArrayList<CardTypeItem> CardTypeItemList) {
        super(context,0, CardTypeItemList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.payment_spinner_item,parent,false);
        }
        ImageView imageViewIcon = view.findViewById(R.id.card_type_imageView);
        TextView imageViewText = view.findViewById(R.id.card_type_TextView);

        CardTypeItem cardTypeItem = getItem(position);

        if (cardTypeItem != null) {
            imageViewIcon.setImageResource(cardTypeItem.getCardTypelogo());
            imageViewText.setText(cardTypeItem.getCardType());
        }
        return view;
    }

}
