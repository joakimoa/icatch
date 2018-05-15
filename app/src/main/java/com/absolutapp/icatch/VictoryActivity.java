package com.absolutapp.icatch;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/**
 * This class is started when you win the game
 */
public class VictoryActivity extends AppCompatActivity implements View.OnClickListener {

    private Vibrator v;
    private Button menuButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_victory);

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(2000);

        menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(this);
        startMusic();
    }

    /**
     * Starts the winning music
     */
    public void startMusic(){
    startService(new Intent(getApplicationContext(), VictoryService.class));
    }

    /**
     * Returns to the main menu
     * @param v
     */
    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
