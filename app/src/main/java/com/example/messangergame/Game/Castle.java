package com.example.messangergame.Game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.messangergame.CastleGame;
import com.example.messangergame.R;

import java.util.ArrayList;
import java.util.Arrays;

public class Castle extends Static {
    public static final int TEAM_BLUE = 0;
    private ArrayList<Sprite> soldiers1 = new ArrayList<>();
    private ArrayList<Sprite> soldiers2 = new ArrayList<>();
    private ArrayList<Sprite> soldiers3 = new ArrayList<>();
    private ArrayList<ArrayList<Sprite>> soldiers = new ArrayList<>(Arrays.asList(soldiers1, soldiers2, soldiers3));
    private String color;
    private static final int[] TEAM_BLUE_STATES = {R.drawable.a0, R.drawable.a1, R.drawable.a2};
    private static final int[] TEAM_GREEN_STATES = {R.drawable.a3, R.drawable.a4, R.drawable.a5};
    private static final int[] TEAM_RED_STATES = {R.drawable.a6, R.drawable.a7, R.drawable.a8};
    private static final int[][] TEAMS_STATES = {TEAM_BLUE_STATES, TEAM_GREEN_STATES, TEAM_RED_STATES};
    public static final int TEAM_GREEN = 1;
    public static final int TEAM_RED = 2;
    private int team;
    int w,h;
    private int state = 0;
    private double HP = 120;
    private static int players = 1;
    private int player;

    public String getColor() {
        return color;
    }

    public void summonSoldier(Sprite s, int layer) {
        if (s == null) return;
        soldiers.get(layer).add(s);
        s.spawn((int)(w / (players == 1 ? 1.35 : 3.85)),h - 10);
    }

    public double getHP() {
        return HP;
    }

    private void gotDamage(Castle who, double hit) {
        if (HP <= 0) return;
        HP -= hit;
        int newstate = state;
        if (HP < 80 && state < 2) {
            Resources r = context.getResources();
            double difference;
            if (HP < 30) {
                newstate = 2;
            }
            else {
                newstate = 1;
            }
            if (state != newstate && state < newstate) {
                difference = r.getDrawable(TEAMS_STATES[team][state]).getMinimumHeight()*1d/r.getDrawable(TEAMS_STATES[team][newstate]).getMinimumHeight();
                drawableID = TEAMS_STATES[team][newstate];
                offY += height - height/difference;
                height /= difference;
                super.reFactory();
                state = newstate;
            }
        }
        if (HP <= 0) {
            CastleGame.cg.onWin(player == 1 ? 2 : 1);
        }
    }

    public void damage(Castle target, double hit) {
        HP += hit;
        target.gotDamage(this, hit);
    }

    public Castle(int w, int h, Context context, int team) {
        super(context, getStartID(team), (int)(w / (players == 1 ? 3.85 : 1.35)), (int)(h/6));
        scale(w/2, h);
        this.w = w;
        this.h = h;
        this.team = team;
        if (players == 1) super.flipX();
        player = players;
        players+=1;

        switch (team) {
            case TEAM_BLUE:
                color = "0000FF";
                break;
            case TEAM_GREEN:
                color = "00FF00";
                break;
            case TEAM_RED:
                color = "FF0000";
                break;
        }
    }

    private static int getStartID(int team) {
        int drawableID = 0;
        switch (team){
            case TEAM_BLUE: drawableID = R.drawable.a0; break;
            case TEAM_GREEN: drawableID = R.drawable.a3; break;
            case TEAM_RED: drawableID = R.drawable.a6; break;
        }
        return drawableID;
    }



    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, offX, offY, null);
        for (ArrayList<Sprite> level : soldiers) {
            for (Sprite soldier : level) {
                soldier.onDraw(canvas);
            }
        }

    }

    public static class Forge {
        public static Castle[] create2Teams(int w, int h, Context context) {
            int team = (int) (Math.random() * 3);
            int team2 = (int) (Math.random() * 2);
            if (team2 == team) team2+=1;
            return new Castle[] {new Castle(w, h, context, team), new Castle(w, h, context, team2)};
        }
        public static void onDraw(Castle[] c, Canvas canvas) {
            c[0].onDraw(canvas);
            c[1].onDraw(canvas);
        }
    }
}