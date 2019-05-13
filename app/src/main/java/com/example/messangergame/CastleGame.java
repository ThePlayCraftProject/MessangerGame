package com.example.messangergame;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messangergame.Game.CardSystem.Card;
import com.example.messangergame.Game.CardSystem.CardAdapter;
import com.example.messangergame.Game.GameTimer;
import com.example.messangergame.Game.MySurfaceView;
import com.example.messangergame.Game.Sprite;

import java.util.ArrayList;
import java.util.Arrays;

public class CastleGame extends AppCompatActivity implements View.OnDragListener {
    public static CastleGame cg;
    private volatile boolean surfaceReady = false;
    public GameTimer gt;
    private Dialog dialog = null;
    private MySurfaceView msf;
    private CardAdapter ca;
    private volatile int winner = 0;
    private int coins = 100;
    private Card dragging = null;


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

        ca = new CardAdapter(this, new ArrayList<>(Arrays.asList(new Card(0, "Mr.Hello", 10), new Card(1, "Mrs.Bye", 25))));
        ListView lv = findViewById(R.id.listView);
        lv.setAdapter(ca);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Card card = (Card) parent.getItemAtPosition(position);
                if (coins < card.getPrice()) return false;



                // Create a new ClipData.Item from the View object's tag
                ClipData.Item item = new ClipData.Item((CharSequence) card.getName());

                // Create a new ClipData using the tag as a label, the plain text MIME type, and
                // the already-created item. This will create a new ClipDescription object within the
                // ClipData, and set its MIME type entry to "text/plain"
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                ClipData data = new ClipData(card.getName().toString(), mimeTypes, item);

                // Instantiates the drag shadow builder.
                View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(view);

                // Starts the drag
                view.startDrag(data       // data to be dragged
                        , dragshadow  // drag shadow
                        , view          // local data about the drag and drop operation
                        , 0          // flags set to 0 because not using currently
                );
                dragging = card;
                return true;
            }
        });
        findViewById(R.id.textView).setOnDragListener(this);
        findViewById(R.id.textView2).setOnDragListener(this);
        findViewById(R.id.textView3).setOnDragListener(this);


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
        Log.e("MSGI", "WIN!");
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

    @Override
    public boolean onDrag(View v, DragEvent event) {
        // Defines a variable to store the action type for the incoming event
        int action = event.getAction();
        // Handles each of the expected events
        switch (action) {

            case DragEvent.ACTION_DRAG_STARTED:
                Log.e("MSGI","ACTION_DRAG_STARTED");
                // Determines if this View can accept the dragged data
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    // if you want to apply color when drag started to your view you can uncomment below lines
                    // to give any color tint to the View to indicate that it can accept data.
                    v.getBackground().setColorFilter(Color.parseColor("#0FFFFFFF"), PorterDuff.Mode.XOR);
                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate();
                    // returns true to indicate that the View can accept the dragged data.
                    return true;
                }
                // Returns false. During the current drag and drop operation, this View will
                // not receive events again until ACTION_DRAG_ENDED is sent.
                return false;

            case DragEvent.ACTION_DRAG_ENTERED:
                Log.e("MSGI","ACTION_DRAG_ENTERED");
                // Applies a GRAY or any color tint to the View. Return true; the return value is ignored.
                v.getBackground().setColorFilter(Color.parseColor("#1FFFFFFF"), PorterDuff.Mode.XOR);
                // Invalidate the view to force a redraw in the new tint
                v.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                // Ignore the event
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                Log.e("MSGI","ACTION_DRAG_EXITED");
                // Re-sets the color tint to blue. Returns true; the return value is ignored.
                v.getBackground().setColorFilter(Color.parseColor("#0FFFFFFF"), PorterDuff.Mode.XOR);
                //It will clear a color filter .
                //v.getBackground().clearColorFilter();
                // Invalidate the view to force a redraw in the new tint
                v.invalidate();
                return true;

            case DragEvent.ACTION_DROP:
                Log.e("MSGI","ACTION_DROP");
                // Gets the item containing the dragged data
                ClipData.Item item = event.getClipData().getItemAt(0);
                // Gets the text data from the item.
                String dragData = item.getText().toString();
                // Displays a message containing the dragged data.
                Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();
                // Turns off any color tints
                v.getBackground().clearColorFilter();
                // Invalidates the view to force a redraw
                v.invalidate();
                if (dragging != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            summon(dragging, 0);
                        }
                    }).start();
                }
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                Log.e("MSGI","ACTION_DRAG_ENDED");
                // Turns off any color tinting
                v.getBackground().clearColorFilter();
                // Invalidates the view to force a redraw
                v.invalidate();
                // Does a getResult(), and displays what happened.
                if (event.getResult()) {
                    Toast.makeText(this, "The drop was handled.", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(this, "The drop didn't work.", Toast.LENGTH_SHORT).show();
                // returns true; the value is ignored.
                dragging = null;
                return true;
            // An unknown action type was received.
            default:
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }

    public void summon(Card card, int player) {
        int id = card.getId();
        try {
            switch(id) {
                case 0:
                    Sprite s = new Sprite(this, msf.getSize().x, "walk_0", 3, player, 100);
                    msf.castles[player].summonSoldier(s);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

