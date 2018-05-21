package com.absolutapp.icatch;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.Arrays;

public class TimerAsyncTask extends AsyncTask<Void, Integer, String> {
    private int count = 0;
    private int[] seconds;
    ProgressBar[] pb;
    private GameActivity gameActivity;
   // private int[] zoneNo;
    private  int[] initSeconds;


    private int max;
    private int maxNr;

    public TimerAsyncTask(ProgressBar[] pb, int[] seconds, GameActivity gameActivity) {
        this.pb = pb;
        this.seconds = seconds;
        this.initSeconds = Arrays.copyOf(seconds,seconds.length);
        this.gameActivity = gameActivity;
     //   this.zoneNo = zoneNo;
        this.max = maxf(seconds);
    }

    private int maxf(int[] intArray){
        int max = Integer.MIN_VALUE;
        for(int i = 0; i <intArray.length; i++){
            if(intArray[i] > max){
                max = intArray[i];
                maxNr = i;
            }
        }
        return max;
    }


    @Override
    protected void onPreExecute() {
        for(int i = 0; i<3;i++) {
            pb[i].setVisibility(ProgressBar.VISIBLE);
        }
    }

    @Override
    protected String doInBackground(Void... params) {

        while (count <= max) {
            SystemClock.sleep(1000);
            count++;
            this.gameActivity.tick();
            publishProgress(count);

        }
        return "Complete";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

        Log.d("Values", "onProgressUpdate: " + values[0] + "/" + initSeconds[0]);
    for(int i = 0; i<3; i++) {
        if (count < seconds[i]) {
            pb[i].setProgress(values[0]%initSeconds[i]);
        }
        else{
            gameActivity.gameTimerUpdate(i);
        }
    }
        System.out.println("TimerAsyncTask update");
    }


    protected void reset(int timerNr){
        seconds[timerNr] += initSeconds[timerNr];
        pb[timerNr].setProgress(0);
        if(timerNr == maxNr){
            max+=initSeconds[timerNr];
        }
      //  pb[timerNr].setProgress(0);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
      //  gameActivity.gameTimerUpdate(zoneNo);
    }
}