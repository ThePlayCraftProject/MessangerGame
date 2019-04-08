package com.example.messangergame;

import android.widget.EditText;
import android.widget.TextView;

import com.example.messangergame.clientside.Client;

public class AppClient {
    private static MainActivity ma;
    private static TextView outText;
    private static EditText inText;
    public static String ip = null;
    public static int port = 25565;
    public static String name = null;

    public static void setMainActivity(MainActivity ma) {
        AppClient.ma = ma;
        outText = (TextView) ma.findViewById(R.id.outText);
        inText = (EditText) ma.findViewById(R.id.inText);
    }

    public static void setPlayer(String ip, int port) {
        setPlayer(ip, port, null);
    }

    public static void setPlayer(String ip, int port, String name) {
        AppClient.ip = ip;
        AppClient.port = port;
        AppClient.name = name;
    }

    public static void sendToAppMessage(String msg) {
        ma.sendToAppMessage(msg+"\n");
        ma.setBottomScroll();
    }

    public static void clearInText() {
        ma.clearInText();
    }
    public static void readInput() {
        String msg = inText.getText().toString();
        if (msg.length() == 0) return;
        synchronized (Client.msgs) {
            Client.msgs.offer(msg);
            Client.msgs.notifyAll();
        }

        clearInText();
    }
}
