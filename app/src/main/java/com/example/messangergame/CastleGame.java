package com.example.messangergame;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.messangergame.Game.CardSystem.Card;
import com.example.messangergame.Game.CardSystem.CardAdapter;
import com.example.messangergame.Game.GameTimer;
import com.example.messangergame.Game.MySurfaceView;

import java.util.ArrayList;
import java.util.Arrays;

public class CastleGame extends AppCompatActivity {
    public static CastleGame cg;
    private volatile boolean surfaceReady = false;
    public GameTimer gt;
    private Dialog dialog = null;
    private MySurfaceView msf;
    private CardAdapter ca;
    private volatile int winner = 0;
    private int coins = 100;


    public int won() {
        return winner;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cg = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_castle_game);
        getSupportActionBar().hide();
        msf = findViewById(R.id.surfaceView);
        msf.setGame(this);

        ca = new CardAdapter(this, new ArrayList<>(Arrays.asList(new Card(0, "Mr.Hello", 10))));
        ListView lv = findViewById(R.id.listView);
        lv.setAdapter(ca);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Card item = (Card) parent.getSelectedItem();
                if (coins > item.getPrice()) return false;

                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder();
                view.startDragAndDrop(data, myShadowBuilder, view, 0);

                return true;
            }
        });
    }

    public void surfaceReady(MySurfaceView msf, boolean ready) {
        if (ready) {
            if (msf != null && !surfaceReady) {
                surfaceReady = true;
                if (gt != null) {
                    gt.interrupt();
                    gt = null;
                }
                gt = new GameTimer(msf);
                gt.setRunning(true);
                gt.start();
            }
        }
        else {
            surfaceReady = false;
            gt.setRunning(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onWin(int player) {
        Log.e("MSG", "WIN!");
        winner = player;
        new Handler(Looper.getMainLooper()).post(new Runnable(){
            @Override
            public void run() {
                if (dialog != null) {
                    dialog.cancel();
                    dialog = null;
                }
                surfaceReady(msf, false);
                dialog = new Dialog(CastleGame.this);
                dialog.setContentView(R.layout.win_dialog);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button tv = dialog.findViewById(R.id.button);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(CastleGame.this, ListActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


    }
}

