package com.example.messangergame.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
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
    public Border border;
    public SurfaceHolder holder;
    private CastleGame cg;
    public static MySurfaceView msf;


    public Point getSize() {
        return new Point(w, h);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        Log.e("MSGI","2");
        msf = this;
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
        Log.e("MSGI","CREATED");

        this.holder = holder;
        if (background == null) background = new Background(getContext(), w, h);
        if (castles == null) castles = Castle.Forge.create2Teams(w, h, getContext());
        if (border == null) border = new Border(castles);

        Canvas canvas = null;
        try{
            canvas = holder.lockCanvas();
            onDraws(canvas);
        } finally {
            holder.unlockCanvasAndPost(canvas);
        }


        if (cg.won() == 0) {
            Async a = new Async();
            //a.execute(castles);
            cg.surfaceReady(this, true);
        }
        else cg.onWin(cg.won());

    }

    public void onDraws(Canvas canvas) {
        Log.e("MSGD","ONDRAW");
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);

        background.onDraw(canvas);
        canvas.drawCircle(w / 2f, h / 2f, 100, paint);
        Castle.Forge.onDraw(castles, canvas);
        border.onDraw(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e("MSGI","CHANGED");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e("MSGI","DESTROYED");
        cg.surfaceReady(this, false);
    }
}

class Border {
    Castle[] castles;

    public Border(Castle[] castles) {
        this.castles = castles;
    }

    public void onDraw(Canvas canvas) {
        int h = canvas.getHeight();
        int w = canvas.getWidth();
        double border = w*castles[0].getHP()/(castles[0].getHP()+castles[1].getHP());

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#20"+castles[1].getColor()));
        canvas.drawRect((float)(border+5),0, (float)canvas.getWidth(), h, paint);
        paint.setColor(Color.parseColor("#20"+castles[0].getColor()));
        canvas.drawRect(0,0, (float)(border + 5), h, paint);
        for (int i = 0; i < h; i++) {
            w = (int)(Math.random()*12-6);
            canvas.drawLine((float)(border+w),i,(float)(border+5), i, paint);
        }
    }
}




