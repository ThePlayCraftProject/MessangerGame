package com.example.messangergame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {
    private EditText IP, PORT, NAME;
    private String ip, port, name;
    private Button btn;
    private SharedPreferences sp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        IP = (EditText) findViewById(R.id.ip);
        PORT = (EditText) findViewById(R.id.port);
        NAME = (EditText) findViewById(R.id.name);

        sp = getPreferences(MODE_PRIVATE);
        if (sp.contains("ip")) IP.setText(sp.getString("ip", ""));
        if (sp.contains("port")) PORT.setText(sp.getInt("port", 25565));
        if (sp.contains("name")) NAME.setText(sp.getString("name", ""));







        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                ip = IP.getText().toString();;
                if (ip.length()>=7 || ip.toLowerCase().equals("marvel")) {}
                else {
                    IP.setHint("Обязательное поле!");
                    IP.setHintTextColor(Color.RED);
                    return;
                }
                i.putExtra("ip", ip);
                port = PORT.getText().toString();
                if (port.length() > 0) {
                    i.putExtra("port", Integer.parseInt(port));
                }
                name = NAME.getText().toString();
                if (name.length() > 0) {
                    i.putExtra("name", name);
                }




                startActivity(i);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("ip", ip);
        if (port.length() > 0) editor.putInt("port", Integer.parseInt(port));
        editor.putString("name", name);
        editor.apply();
    }
}
