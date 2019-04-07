package com.example.messangergame.clientside;

import android.os.Build;
import android.support.annotation.RequiresApi;

public class ClientThread extends Thread{
    public ClientThread() {
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        super.run();

        Client client = new Client();
        client.start();
    }
}
