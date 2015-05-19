package edu.washington.wsmay1.quizdroid;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DownloadService extends Service {
    private static int interval;
    private static String source;
    private Timer downloadTimer = null;
    private ScheduledExecutorService downloadService = null;
    private Handler downloadHandler = new Handler();
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        /*if (downloadTimer != null) {
            downloadTimer.cancel();
        } else {
            downloadTimer = new Timer();
            checkPreferences();
            downloadTimer.scheduleAtFixedRate(new DownloadTask(), 0, interval * 1000);
        }*/
        if (downloadService != null) {
            downloadService.shutdown();
        } else {
            downloadService = Executors.newScheduledThreadPool(2);
            checkPreferences();
            if (downloadService.isShutdown()) {
                downloadService.scheduleAtFixedRate(makeRunnable(), 0, interval, TimeUnit.SECONDS);
            }
        }
    }

    public void checkPreferences() {
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Log.e("prefs", mySharedPreferences.toString());
        String url = mySharedPreferences.getString("urlPref", "blank");
        int duration = Integer.parseInt(mySharedPreferences.getString("delayPref", "5"));
        boolean changed = false;
        if (!url.equals(source)) {
            source = url;
            changed = true;
        }
        if (duration != interval) {
            interval = duration;
            changed = true;
        }
        if (changed) {
            downloadService.shutdown();
            downloadService.scheduleAtFixedRate(makeRunnable(), 0, interval, TimeUnit.SECONDS);
        }
    }

    private Runnable makeRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                checkPreferences();
                Toast.makeText(getApplicationContext(), source, Toast.LENGTH_SHORT).show();
            }
        };
    }

    /*
    class DownloadTask {
        @Override
        public void run() {
            downloadHandler.post(new Runnable() {
                @Override
                public void run() {
                    checkPreferences();
                    Toast.makeText(getApplicationContext(), source, Toast.LENGTH_SHORT).show();
                }
            });
        }
    } */
}
