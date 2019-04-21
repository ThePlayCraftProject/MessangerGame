package com.example.messangergame;

import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Checker extends Thread{
    private ServerAdapter sa;
    private Server server;
    private volatile Socket socket;
    private volatile Boolean running = false;

    public Checker(ServerAdapter sa, Server server) {
        this.sa = sa;
        this.server = server;
        socket = null;

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
                if (server.online != Server.ONLINE) {
                    server.online = Server.OFFLINE;
                    sa.notifyDataSetChanged();
                }
            }
        }
    }

    private void check() {
        try {
            Scanner dis = new Scanner(socket.getInputStream());
            String msg = dis.nextLine();
            if (msg != null) {
                server.online = Server.ONLINE;
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
                    server.max = max;
                    server.players = players;
                }
            }
            socket.close();
        } catch (IOException | NoSuchElementException e) {
            server.online = Server.OFFLINE;
            ListActivity.send("not working");
        }
        sa.notifyDataSetChanged();
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
