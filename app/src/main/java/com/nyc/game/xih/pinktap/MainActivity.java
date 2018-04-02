package com.nyc.game.xih.pinktap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView tvScore, tvHigh;
    private int score = 0;
    private int high = 0;

    private GridView gvMain;
    private GVAdapter adapter;

    private int[] blocks = new int[9];

    private Handler handler;
    private Runnable runnable;
    private boolean isRunning = false;
    private int delay = 1000;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        play();
    }

    private void init() {
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = 0;
        }

        tvScore = findViewById(R.id.tvScore);
        tvHigh = findViewById(R.id.tvHigh);
        gvMain = findViewById(R.id.gvMain);

        adapter = new GVAdapter(this, blocks);
        gvMain.setAdapter(adapter);
        gvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (blocks[i] == 0) {
                    //failure
                    defeat();
                } else if (blocks[i] == 1) {
                    //score
                    blocks[i] = 2;

                    score++;
                    tvScore.setText("Score: " + score);

                    if (score > high) {
                        high = score;
                        tvHigh.setText("High: " + high);
                    }
                } else if (blocks[i] == 2) {
                    //clicked
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void play() {
        isRunning = true;
        count = 0;
        score = 0;
        tvScore.setText("Score: " + score);
        getHigh();
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = 0;
        }

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    build();
                    handler.postDelayed(this, delay);
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    private void build() {
        if (blocks[0] == 1 || blocks[1] == 1 || blocks[2] == 1) {
            defeat();
        }

        for (int i = 0; i < blocks.length - 3; i++) {
            blocks[i] = blocks[i + 3];
        }

        blocks[blocks.length - 3] = 0;
        blocks[blocks.length - 2] = 0;
        blocks[blocks.length - 1] = 0;

        count++;
        if (count%5==0 && delay > 100) {
            delay = delay - 50;
        }

        int goal = new Random().nextInt(3);
        if (goal == 0) {
            blocks[blocks.length - 3] = 1;
        } else if (goal == 1) {
            blocks[blocks.length - 2] = 1;
        } else {
            blocks[blocks.length - 1] = 1;
        }

        adapter.notifyDataSetChanged();
    }

    private void defeat() {
        isRunning = false;
        saveHigh();
        try {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this);
            builder.setTitle("DEFEAT")
                    .setMessage("Your score is: " + score)
                    .setPositiveButton("Replay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            play();
                        }
                    })
                    .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            MainActivity.this.finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getHigh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveHigh();
    }

    private void saveHigh() {
        SharedPreferences preferences = this.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("HIGH", high);
        editor.apply();
    }

    private void getHigh() {
        SharedPreferences preferences = this.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        high = preferences.getInt("HIGH", 0);
        tvHigh.setText("High: " + high);
    }
}
