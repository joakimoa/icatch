package com.absolutapp.icatch;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.ProgressBar;

public class TimerAsyncTask extends AsyncTask<Void, Integer, String> {
    private int count = 0;
    private int seconds;
    ProgressBar pb;
    private GameActivity gameActivity;
    private int zoneNo;

    public TimerAsyncTask(ProgressBar pb, int seconds, GameActivity gameActivity, int zoneNo) {
        this.pb = pb;
        this.seconds = seconds;
        this.gameActivity = gameActivity;
        this.zoneNo = zoneNo;
    }

    @Override
    protected void onPreExecute() {
        pb.setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    protected String doInBackground(Void... params) {

        while (count < seconds) {
            SystemClock.sleep(1000);
            count++;
            publishProgress(count);
        }
        return "Complete";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        pb.setProgress(values[0]);
        System.out.println("TimerAsyncTask update");
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        gameActivity.gameTimerUpdate(zoneNo);
    }
}