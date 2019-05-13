package com.example.messangergame.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.example.messangergame.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Sprite {

    public static final int ANIM_WALK = 0;
    public static final int ANIM_ATTACK = 1;
    public static final int ANIM_DIE = 2;


    /**Картинка*/
    private ArrayList<Bitmap> walks = new ArrayList<>();
    private ArrayList<Bitmap> attacks = new ArrayList<>();
    private ArrayList<Bitmap> dyings = new ArrayList<>();
    private ArrayList<ArrayList<Bitmap>> anims = new ArrayList<>(Arrays.asList(walks, attacks, dyings));
    private int w;

    /**Позиция по Х=0*/
    private double x = 0;
    private double y = 0;

    /**Скорость по Х=5*/
    private double vx = 0;
    private int padding = 0;

    private double vy = 0;

    /**Текущий кадр = 0*/
    private int currentFrame = 0;
    private ArrayList<Bitmap> currentFrameGroup = null;

    /**Ширина*/
    private int width;

    /**Ввыоста*/
    private int height;
    private long lastupdate = new Date().getTime();
    private int team;

    /**Конструктор*/
    public Sprite(Context context, int w, String source, double vx, int team, double heightS)
    {
        this.w = w;
        try {
            int i = 0;
            while(true) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.class.getField(source+"0"+i).getInt(null));
                double q = bitmap.getHeight()/heightS;
                bitmap = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()/q), (int)(bitmap.getHeight()/q), false);
                walks.add(bitmap);
                i+=1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            int i = 0;
            while(true) {
                dyings.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.class.getField(source+"1"+i).getInt(null)));
                i+=1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.vx = vx;
    }

    public void spawn(int x, int y) {
        this.x = x;
        this.y = y;
        currentFrameGroup = anims.get(ANIM_WALK);
    }

    /**Перемещение объекта, его направление*/
    private void update()
    {
        long update = new Date().getTime();
        double seconds = (update - lastupdate)/1000d;
        double s = vx*seconds;
        lastupdate = update;
        x += s;
    }

    /**Рисуем наши спрайты*/
    public void onDraw(Canvas canvas)
    {
        update();
        if (currentFrameGroup == null) return;
        if (currentFrameGroup.get(currentFrame) == null) return;
        canvas.drawBitmap(currentFrameGroup.get(currentFrame), (int)x , (int)y, null);
    }

    public boolean intersect (Sprite s) {
        return getBoundingBoxRect().intersect(s.getBoundingBoxRect());
    }

    public Rect getBoundingBoxRect () {
        int frameWidth = currentFrameGroup.get(currentFrame).getWidth();
        int frameHeight = currentFrameGroup.get(currentFrame).getHeight();
        return new Rect((int)x+padding, (int)y+padding, (int)(x + frameWidth - 2 *padding),
                (int)(y + frameHeight - 2* padding));
    }
}