package com.example.messangergame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.messangergame.clientside.Client;
import com.example.messangergame.clientside.ClientThread;

public class MainActivity extends AppCompatActivity {
    private TextView outText;
    private EditText inText;
    private ScrollView scroll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();

        String ip = null;
        int port = 25565;
        String name = null;

        if (i != null) {
            ip = i.getStringExtra("ip");
            port = i.getIntExtra("port", 25565);
            name = i.getStringExtra("name");
        }
        if (name == null) {
            AppClient.setPlayer(ip, port);
        }
        else {
            AppClient.setPlayer(ip, port, name);
        }







        AppClient.setMainActivity(this);

        outText = (TextView) findViewById(R.id.outText);
        inText = (EditText) findViewById(R.id.inText);
        scroll = findViewById(R.id.scroll);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(){
                    @Override
                    public void run() {
                        AppClient.readInput();
                    }
                }.start();
            }
        });

        inText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    AppClient.readInput();
                }

                return false;
            }
        });

        ClientThread thread = new ClientThread();
        thread.start();
    }

    public void sendToAppMessage(final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable(){
            @Override
            public void run() {
                outText.setText(outText.getText()+msg);
            }
        });
    }
    public void setBottomScroll() {
        new Handler(Looper.getMainLooper()).post(new Runnable(){
            @Override
            public void run() {
                scroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
    public void clearInText() {
        new Handler(Looper.getMainLooper()).post(new Runnable(){
            @Override
            public void run() {
                inText.setText("");
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        Client.stop();
    }
}
