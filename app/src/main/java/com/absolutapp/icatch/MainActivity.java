package com.absolutapp.icatch;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView h2p, playBtn;
    private Switch diffSwitch;
    public static boolean HARD_DIFFICULTY;
    private Intent musicServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 19)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else
        {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_main);
        HARD_DIFFICULTY = false;
        h2p = findViewById(R.id.h2p);
        playBtn = findViewById(R.id.play);
        h2p.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        diffSwitch = findViewById(R.id.difficultySwitch);
        musicServiceIntent = new Intent(getApplicationContext(), MusicService.class);
        startService(musicServiceIntent);

        /**
         * Decides the difficulty for the game
         */
        diffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                HARD_DIFFICULTY = isChecked;
            }
        });

    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(MainActivity.this, MusicService.class));
        super.onDestroy();
    }

    /**
     * Reruns the how-to-play
     * @param view
     */
    public void loadSlides(View view)
    {
        new PreferenceManager(this).clearPreference();
        startActivity(new Intent(this, InstructionsActivity.class));
        finish();
    }

    /**
     * Starts the actual game
     * @param v
     */
    public void startGame(View v)
    {
        stopService(new Intent(MainActivity.this, MusicService.class));
        startActivity(new Intent(this, GameActivity.class));
    }

    /**
     * Decides what view to change to after a button click
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.h2p:
                loadSlides(v);
                break;

            case R.id.play:
                startGame(v);
                break;
        }
    }
}

