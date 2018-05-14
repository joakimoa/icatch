package com.absolutapp.icatch;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;

// from https://stackoverflow.com/questions/18357641/is-it-possible-to-run-multiple-asynctask-in-same-time
public class AsyncTaskTools {
    public static <P, T extends AsyncTask<P, ?, ?>> void execute(T task) {
        execute(task, (P[]) null);
    }

    @SuppressLint("NewApi")
    public static <P, T extends AsyncTask<P, ?, ?>> void execute(T task, P... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }
}