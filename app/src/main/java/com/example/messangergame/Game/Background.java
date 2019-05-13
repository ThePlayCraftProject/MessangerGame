package com.example.messangergame.Game;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.messangergame.R;

public class Background extends Static {
    public Background(Context context, int w, int h) {
        super(context, R.drawable.valley, 0, 0);
        scale(w, h);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, offX, offY, null);
    }
}