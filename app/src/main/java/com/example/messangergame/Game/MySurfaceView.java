package com.example.messangergame.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.messangergame.CastleGame;


public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private int w, h;
    public Castle[] castles;
    public Background background;
    public SurfaceHolder holder;
    private CastleGame cg;
    public static MySurfaceView msf;



    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        Log.e("MSG","2");
        msf = this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("MSG","ONDRAWORIGINAL");
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);

        background.onDraw(canvas);
        canvas.drawCircle(w / 2f, h / 2f, 100, paint);
        Castle.Forge.onDraw(castles, canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
    }

    public void setGame(CastleGame cg) {
        this.cg = cg;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e("MSG","CREATED");

        this.holder = holder;
        if (background == null) background = new Background(getContext(), w, h);
        if (castles == null) castles = Castle.Forge.create2Teams(w, h, getContext());

        Canvas canvas = null;
        try{
            canvas = holder.lockCanvas();
            onDraws(canvas);
        } finally {
            holder.unlockCanvasAndPost(canvas);
        }


        if (cg.won() == 0) {
            Async a = new Async();
            a.execute(castles);
            cg.surfaceReady(this, true);
        }
        else cg.onWin(cg.won());

    }

    public void onDraws(Canvas canvas) {
        Log.e("MSG","ONDRAW");
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);

        background.onDraw(canvas);
        canvas.drawCircle(w / 2f, h / 2f, 100, paint);
        Castle.Forge.onDraw(castles, canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e("MSG","CHANGED");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e("MSG","DESTROYED");
        cg.surfaceReady(this, false);
    }
}




