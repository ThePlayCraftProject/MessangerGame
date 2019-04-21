package com.example.messangergame;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ServerAdapter extends ArrayAdapter<Server> {
    private ListView lv;
    private ListActivity la;

    public ServerAdapter(Context context, ArrayList<Server> arr) {
        super(context, R.layout.item_list, arr);
    }

    @Override
    public void notifyDataSetChanged() {
        new Handler(Looper.getMainLooper()).post(new Runnable(){
            @Override
            public void run() {
                ServerAdapter.super.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Server server = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, null);
        }

        ((TextView) convertView.findViewById(R.id.name)).setText(server.getName());
        ((TextView) convertView.findViewById(R.id.ip)).setText(server.getIp());
        ((TextView) convertView.findViewById(R.id.port)).setText(String.valueOf(server.getPort()));

        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.detailLayout);


        ImageView status = (ImageView) convertView.findViewById(R.id.imageView);
        ProgressBar bar = (ProgressBar) convertView.findViewById(R.id.progressBar);
        TextView details = (TextView) convertView.findViewById(R.id.details);
        if (server.online != Server.ONLINE) {
            layout.setVisibility(View.INVISIBLE);
            if (server.online == Server.OFFLINE) {
                status.setImageResource(R.drawable.presence_offline);
            }
            else {
                status.setImageResource(R.drawable.presence_invisible);
            }
        }
        else {
            layout.setVisibility(View.VISIBLE);
            status.setImageResource(R.drawable.presence_online);
            details.setText(server.players+"/"+server.max);
            bar.setMax(server.max);
            bar.setProgress(server.players);
        }
        if (!server.isHavingChecker()) {
            Checker ch = new Checker(this, server);
            ch.start();
        }

        ImageButton btn = (ImageButton) convertView.findViewById(R.id.imageButton);

        class MyListener implements View.OnClickListener {
            private Server server;

            public MyListener(Server server) {
                this.server = server;
            }

            @Override
            public void onClick(View v) {
                ListActivity.deleteServer(server);
            }
        }
        btn.setOnClickListener(new MyListener(server));

        ImageButton btn2 = (ImageButton) convertView.findViewById(R.id.imageButton2);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = ((TextView)v.getRootView().findViewById(R.id.ip)).getText().toString();
                String port = ((TextView)v.getRootView().findViewById(R.id.port)).getText().toString();
                if (port.length() <= 0) return;
                ListActivity.startClient(ip, Integer.parseInt(port));
            }
        });



        return convertView;
    }
}