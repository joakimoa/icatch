package com.absolutapp.icatch;

/**
 * Created by Joakim on 2018-05-09.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import static android.os.SystemClock.sleep;
import static android.os.VibrationEffect.DEFAULT_AMPLITUDE;

public class MainAccelerometer extends Activity implements AccelerometerListener{
    public int [] imageArray = {R.drawable.pill0, R.drawable.pill05,
            R.drawable.pill1, R.drawable.pill15,
            R.drawable.pill2, R.drawable.pill25,
            R.drawable.pill3, R.drawable.pill35,
            R.drawable.pill4, R.drawable.pill45,
            R.drawable.pill5};
    public int pillcount = 9;
    public ImageView imageview2;
    public Boolean cleared = false;
    public Vibrator v;



    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.accelerometer_example_main);
        imageview2 = findViewById(R.id.imageView2);
        imageview2.setImageResource(imageArray[10]);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    public void onAccelerationChanged(float x, float y, float z) {

    }

    public void onShake(float force) {
        if(pillcount > 0){
            Intent soundServiceIntent = new Intent(getApplicationContext(), SoundService.class);
            startService(soundServiceIntent);
            imageview2.setImageResource(imageArray[pillcount]);
            pillcount--;
            v.vibrate(50);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    v.cancel();
                    imageview2.setImageResource(imageArray[pillcount]);
                    pillcount--;
                    if (pillcount <= 0){
                        cleared = true;
                        Toast.makeText(getBaseContext(), "Grattis! Zon botad",
                                Toast.LENGTH_LONG).show();
                        exitApp();
                    }
                }
            }, 50);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isSupported(this)) {

            //Start Accelerometer Listening
            AccelerometerManager.startListening(this);
        }

        stopSounds();
    }

    @Override
    public void onStop() {
        super.onStop();

        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isListening()) {

            //Start Accelerometer Listening
            AccelerometerManager.stopListening();
            
        }

        stopSounds();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Sensor", "Service  distroy");

        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isListening()) {

            //Start Accelerometer Listening
            AccelerometerManager.stopListening();

            Toast.makeText(getBaseContext(), "onDestroy Accelerometer Stoped",
                    Toast.LENGTH_SHORT).show();
        }

        stopSounds();
    }

    private void stopSounds() {
        stopService(new Intent(MainAccelerometer.this, SoundService.class));
    }


    private void exitApp() {
        setResult(Activity.RESULT_OK);
        GameActivity.GAME_WON = true;
        finishAfterTransition();
    }
}