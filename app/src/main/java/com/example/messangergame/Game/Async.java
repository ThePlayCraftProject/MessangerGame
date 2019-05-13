package com.example.messangergame.Game;

import android.os.AsyncTask;

public class Async extends AsyncTask<Castle,Void,Void> {


    @Override
    protected Void doInBackground(Castle... castles) {
        if (castles.length >= 2) {
            for (double i = 0; i < 144; i+=0.1) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                castles[0].damage(castles[1], 0.1);
            }
        }
        return null;
    }
}
