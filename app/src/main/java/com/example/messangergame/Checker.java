package com.example.messangergame;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Checker extends Thread{
    private View view;
    private LinearLayout layout;
    private ImageView status;
    private ProgressBar bar;
    private TextView details;
    private Server server;
    private volatile Socket socket;
    private boolean running = false;

    public Checker(View view, Server server) {
        this.view = view;
        this.server = server;
        this.socket = null;

        layout = (LinearLayout) view.findViewById(R.id.detailLayout);
        status = (ImageView) view.findViewById(R.id.imageView);
        bar = (ProgressBar) view.findViewById(R.id.progressBar);
        details = (TextView) view.findViewById(R.id.details);

        server.setChecker(this);

        running = true;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
        if (socket != null) {
            check();
        }
    }

    @Override
    public void run() {
        while (running) {
            socket = null;
            Connect con = new Connect(server, this);
            con.start();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            con.interrupt();
            if (socket == null) {
                ServerAdapter.setVisibility(layout, View.INVISIBLE);
                ServerAdapter.setImageResource(status, R.drawable.presence_offline);
            }
        }
    }

    private void check() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String msg = dis.readUTF();
            if (msg != null) {
                ServerAdapter.setImageResource(status, R.drawable.presence_online);
                if (msg.length() >= 3) {
                    String[] args = msg.split("/");
                    int players = 0;
                    if (args[0].length() > 0) {
                        players = Integer.parseInt(args[0]);
                    }
                    int max = 1;
                    if (args[1].length() > 0) {
                        max = Integer.parseInt(args[1]);
                    }
                    bar.setMax(max);
                    bar.setProgress(players);
                    ServerAdapter.setText(details, players + "/" + max);

                    ServerAdapter.setVisibility(layout, View.VISIBLE);
                } else ServerAdapter.setVisibility(layout, View.INVISIBLE);
            }
            socket.close();
        } catch (IOException e) {
            ServerAdapter.setImageResource(status, R.drawable.presence_offline);
            ServerAdapter.setVisibility(layout, View.INVISIBLE);
            ListActivity.send("not working");
        }
    }

    public void askToStop() {
        running = false;
    }
}

class Connect extends Thread {
    private Server server;
    private Socket socket;
    private Checker c;
    public Connect(Server server, Checker c) {
        this.server = server;
        this.c = c;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(server.getIp(), server.getPort()+1);
        } catch (IOException e) {}
        c.setSocket(socket);
    }
}
