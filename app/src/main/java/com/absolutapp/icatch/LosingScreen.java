package com.absolutapp.icatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/**
 * This class is started whenever you lose the game
 */
public class LosingScreen extends Activity implements View.OnClickListener {

    private Button menuButton;
    private Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        setContentView(R.layout.activity_losing_screen);

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(2000);

        menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(this);
        startMusic();
    }

    /**
     * Starts the losing music
     */
    public void startMusic(){
        startService(new Intent(getApplicationContext(), FailureService.class));
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
