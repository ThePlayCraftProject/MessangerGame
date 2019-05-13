package com.example.messangergame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {
    private EditText NAME;
    private String name;
    private Button btn;
    private SharedPreferences sp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        NAME = (EditText) findViewById(R.id.name);

        sp = getPreferences(MODE_PRIVATE);
        if (sp.contains("name")) NAME.setText(sp.getString("name", ""));







        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, ListActivity.class);
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
        editor.putString("name", name);
        editor.apply();
    }
}
