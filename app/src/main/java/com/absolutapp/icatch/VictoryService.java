package com.absolutapp.icatch;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

/**
 * Plays the victory music
 */
public class VictoryService extends Service {
    MediaPlayer mp;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCreate() {
        mp = MediaPlayer.create(this, R.raw.victory);
        mp.setLooping(false);
    }

    public void onDestroy() {
        mp.stop();
    }
    public void onStart(Intent intent, int startid){
        mp.start();
    }
}