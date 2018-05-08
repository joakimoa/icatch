package com.absolutapp.icatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent musicServiceIntent = new Intent(getApplicationContext(), MusicService.class);
        startService(musicServiceIntent);

    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(MainActivity.this, MusicService.class));
        super.onDestroy();
    }

    //reruns the how-to-play
    public void loadSlides(View view)
    {
        new PreferenceManager(this).clearPreference();
        startActivity(new Intent(this, InstructionsActivity.class));
        finish();
    }
}

