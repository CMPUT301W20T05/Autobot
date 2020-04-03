package com.example.autobot.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.autobot.R;
import com.example.autobot.WalletInformation;

import java.util.ArrayList;

public class WalletAdapter extends ArrayAdapter<WalletInformation> {
    private ArrayList<WalletInformation> informations;
    private Context context;

    /**
     * This is custom adapter of payment history
     * @param context
     * @param informations
     */
    public WalletAdapter(Context context, ArrayList<WalletInformation> informations){
        super(context, 0,informations);
        this.informations = informations;
        this.context = context;
    }

    /**
     * to set wallet history view
     * @param position
     * @param convertView
     * @param parent
     * @return
     */

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.wallet_history, parent,false);
        }
        WalletInformation information = informations.get(position);
        TextView Destination_wallet = view.findViewById(R.id.Destination_wallet);
        TextView cost = view.findViewById(R.id.cost);
        TextView wallet_date = view.findViewById(R.id.wallet_date);

        Destination_wallet.setText(information.getDestination());
        cost.setText(information.getCost());
        wallet_date.setText(information.getDate());
        return view;
    }
}
