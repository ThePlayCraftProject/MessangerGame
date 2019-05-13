package com.example.messangergame.Game;

import android.graphics.Canvas;
import android.util.Log;

public class GameTimer extends Thread {

    /**Объект класса*/
    private MySurfaceView msf;

    /**Переменная задавания состояния потока рисования*/
    private volatile boolean running = false;

    /**Конструктор класса*/
    public GameTimer(MySurfaceView msf)
    {
        this.msf = msf;
    }

    /**Задание состояния потока*/
    public void setRunning(boolean run)
    {
        running = run;
    }

    /** Действия, выполняемые в потоке */
    public void run() {
        Log.e("MSGI","GameTimer>Start");
        while (running) {
            Canvas c = null;
            try {
                c = msf.getHolder().lockCanvas();
                synchronized (msf.getHolder()) {
                    if (running) msf.onDraws(c);
                }
            } finally {
                if (c != null) {
                    msf.getHolder().unlockCanvasAndPost(c);
                }
            }
        }
    }
}
