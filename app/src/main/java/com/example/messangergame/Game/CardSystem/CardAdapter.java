package com.example.messangergame.Game.CardSystem;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.messangergame.R;

import java.util.ArrayList;

public class CardAdapter extends ArrayAdapter<Card> {

    public CardAdapter(Context context, ArrayList<Card> arr) {
        super(context, R.layout.card_item, arr);

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Card card = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_item, null);
        }

        TextView name = convertView.findViewById(R.id.name);
        TextView price = convertView.findViewById(R.id.price);

        name.setText(card.getName());
        price.setText(card.getPrice() +"");

        return convertView;
    }
}

