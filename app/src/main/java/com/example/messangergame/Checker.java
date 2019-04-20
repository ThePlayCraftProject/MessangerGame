package com.example.messangergame;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Checker extends Thread{
    private View view;
    private LinearLayout layout;
    private ImageView status;
    private ProgressBar bar;
    private TextView details;
    private Server server;
    private volatile Socket socket;
    private volatile Boolean running = false;

    public Checker(View view, Server server) {
        this.view = view;
        this.server = server;
        socket = null;

        layout = (LinearLayout) view.findViewById(R.id.detailLayout);
        status = (ImageView) view.findViewById(R.id.imageView);
        bar = (ProgressBar) view.findViewById(R.id.progressBar);
        details = (TextView) view.findViewById(R.id.details);

        server.setChecker(this);

        running = true;
    }

    public void setSocket(Socket socket) {
        if (this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.socket = null;
        }
        this.socket = socket;
        if (socket != null) {
            check();
        }
    }

    @Override
    public void run() {
        while (running) {
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
                ListActivity.send("BADrun");
            }
        }
        ListActivity.send("NOTrun");
    }

    private void check() {
        try {
            Scanner dis = new Scanner(socket.getInputStream());
            String msg = dis.nextLine();
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
        } catch (IOException | NoSuchElementException e) {
            ListActivity.send("BADcheck");
            ServerAdapter.setImageResource(status, R.drawable.presence_offline);
            ServerAdapter.setVisibility(layout, View.INVISIBLE);
            ListActivity.send("not working");
        }
    }

    public void askToStop() {
        running = false;
        interrupt();
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
        if (!c.isInterrupted()) c.setSocket(socket);
    }
}
