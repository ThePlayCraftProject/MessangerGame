package com.example.messangergame.Game;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Build;

public abstract class Static{
    protected Context context;
    protected Bitmap bitmap;
    protected int width, height;
    protected int offX, offY;
    protected int drawableID;

    public Static(Context context, int drawableID, int offX, int offY) {
        this.context = context;
        this.drawableID = drawableID;
        this.offX = offX;
        this.offY = offY;
        bitmap = BitmapFactory.decodeResource(context.getResources(), drawableID);
    }

    protected void scale(int width, int height) {
        this.width = width;
        this.height = height;
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    protected void reFactory() {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableID);
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        this.bitmap = bitmap;
    }

    public abstract void onDraw(Canvas canvas);

    protected void flipX() {
        Matrix m = new Matrix();
        m.postScale(-1, 1, bitmap.getWidth()/2f, bitmap.getWidth()/2f);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        offX -= width;
    }
}