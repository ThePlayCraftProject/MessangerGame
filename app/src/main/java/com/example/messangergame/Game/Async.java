package com.example.messangergame.Game;

import android.os.AsyncTask;

public class Async extends AsyncTask<Castle,Void,Void> {


    @Override
    protected Void doInBackground(Castle... castles) {
        if (castles.length >= 2) {
            for (int i = 0; i < 144; i++) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                castles[0].damage(castles[1], 1);
            }
        }
        return null;
    }
}
