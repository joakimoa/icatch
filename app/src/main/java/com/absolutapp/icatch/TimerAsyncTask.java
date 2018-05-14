package com.absolutapp.icatch;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.ProgressBar;

public class TimerAsyncTask extends AsyncTask<Void, Integer, String> {
    private int count = 0;
    private int seconds;
    ProgressBar pb;

    public TimerAsyncTask(ProgressBar pb, int seconds) {
        this.pb = pb;
        this.seconds = seconds;
    }

    @Override
    protected void onPreExecute() {
        pb.setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    protected String doInBackground(Void... params) {
//        while (count < 5) {
//            SystemClock.sleep(1000);
//            count++;
//            publishProgress(count * 20);
//        }
//        return "Complete";

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
}