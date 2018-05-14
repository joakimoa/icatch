package com.absolutapp.icatch;

/**
 * Created by Joakim on 2018-05-09.
 */
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import static android.os.SystemClock.sleep;

public class MainAccelerometer extends Activity implements AccelerometerListener{
    public int [] imageArray = {R.drawable.pill0, R.drawable.pill05,
            R.drawable.pill1, R.drawable.pill15,
            R.drawable.pill2, R.drawable.pill25,
            R.drawable.pill3, R.drawable.pill35,
            R.drawable.pill4, R.drawable.pill45,
            R.drawable.pill5};
    public int pillcount = 9;
    public ImageView imageview2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accelerometer_example_main);
        imageview2 = findViewById(R.id.imageView2);
        imageview2.setImageResource(imageArray[10]);

        // Check onResume Method to start accelerometer listener
    }

    public void onAccelerationChanged(float x, float y, float z) {
        // TODO Auto-generated method stub

    }

    public void onShake(float force) {
        if(pillcount > 0){
            Intent soundServiceIntent = new Intent(getApplicationContext(), SoundService.class);
            startService(soundServiceIntent);
            imageview2.setImageResource(imageArray[pillcount]);
            pillcount--;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    imageview2.setImageResource(imageArray[pillcount]);
                    pillcount--;
                }
            }, 50);


        } else {
            // Called when Motion Detected
            Toast.makeText(getBaseContext(), "Grattis! Zon botad",
                    Toast.LENGTH_SHORT).show();
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

        stopService(new Intent(MainAccelerometer.this, SoundService.class));
    }

    @Override
    public void onStop() {
        super.onStop();

        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isListening()) {

            //Start Accelerometer Listening
            AccelerometerManager.stopListening();
            
        }

        stopService(new Intent(MainAccelerometer.this, SoundService.class));
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

        stopService(new Intent(MainAccelerometer.this, SoundService.class));
    }

}