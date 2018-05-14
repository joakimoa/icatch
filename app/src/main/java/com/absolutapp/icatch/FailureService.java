package com.absolutapp.icatch;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class FailureService extends Service {
    MediaPlayer mp;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCreate() {
        mp = MediaPlayer.create(this, R.raw.failure);
        mp.setLooping(false);
    }

    public void onDestroy() {
        mp.stop();
    }
    public void onStart(Intent intent, int startid){
        mp.start();
    }
}