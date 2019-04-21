package com.example.messangergame;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {





    private DB db;
    private ArrayList<Server> servers;
    private static ListActivity la;
    private ServerAdapter sa;
    protected AlertDialog ad;
    private String name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent i = getIntent();
        if (i.hasExtra("name")) name = i.getStringExtra("name");




        la = this;

        db = new DB(this);

        servers = loadData();





        ListView lv = (ListView) findViewById(R.id.list);
        sa = new ServerAdapter(this, servers);
        lv.setAdapter(sa);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(ListActivity.la).inflate(R.layout.prompt, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.la);
                builder.setView(view);

                final EditText NAME = (EditText) view.findViewById(R.id.input_text);
                final EditText IP = (EditText) view.findViewById(R.id.input_text2);
                final EditText PORT = (EditText) view.findViewById(R.id.input_text3);
                final ImageButton btn = (ImageButton) view.findViewById(R.id.imageButton);

                builder.setCancelable(true);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = NAME.getText().toString();
                        if (name.length() <= 0) {
                            NAME.setHint("Обязательное поле!");
                            NAME.setHintTextColor(Color.RED);
                            NAME.requestFocus();
                            return;
                        }
                        String ip = IP.getText().toString();;
                        if (ip.length()>=7 || ip.toLowerCase().equals("marvel")) {
                            IP.setTextColor(Color.BLACK);
                        }
                        else {
                            IP.setHint("Обязательное поле!");
                            IP.setHintTextColor(Color.RED);
                            IP.setTextColor(Color.RED);
                            IP.requestFocus();
                            return;
                        }
                        String port = PORT.getText().toString();
                        if (port.length() <= 0) {
                            PORT.setHint("Обязательное поле!");
                            PORT.setHintTextColor(Color.RED);
                            PORT.requestFocus();
                            return;
                        }

                        Server server = new Server(name, ip, Integer.parseInt(port));
                        addServer(server);

                        ad.dismiss();
                    }
                });

                ad = builder.create();
                ad.show();
            }
        });


    }


    public static void startClient(String ip, int port) {
        la.startClientInside(ip, port);
    }
    public void startClientInside(String ip, int port) {
        Intent i = new Intent(ListActivity.this, MainActivity.class);
        i.putExtra("name", la.name);
        i.putExtra("ip", ip);
        i.putExtra("port", port);
        la.startActivity(i);
    }






    public static void send(final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable(){
            @Override
            public void run() {
                Toast.makeText(la, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void deleteServer(Server server) {
        server.stop();
        if (la.servers.contains(server)) {
            la.servers.remove(server);
            la.sa.notifyDataSetChanged();
            la.db.delete(server.getId());
        }
    }

    private ArrayList<Server> loadData() {
        return db.selectAll();
    }


    public static void addServer(Server server) {
        if(!la.servers.contains(server)) {
            la.servers.add(server);
            la.sa.notifyDataSetChanged();
            long id = la.db.insert(server.getName(), server.getIp(), server.getPort());
            server.setId(id);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        for (Server server : servers) {
            server.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        sa.notifyDataSetChanged();
    }
}
