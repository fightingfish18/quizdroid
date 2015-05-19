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
        if (downloadTimer != null) {
            downloadTimer.cancel();
        } else {
            downloadTimer = new Timer();
            checkPreferences();
            downloadTimer.scheduleAtFixedRate(new DownloadTask(), 0, interval * 60000);
        }

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Preferences Changed", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        downloadTimer.cancel();
    }

    public void checkPreferences() {
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Log.e("prefs", mySharedPreferences.toString());
        String url = mySharedPreferences.getString("urlPref", "blank");
        int duration = Integer.parseInt(mySharedPreferences.getString("delayPref", "5"));
        source = url;
        interval = duration;
    }


    class DownloadTask extends TimerTask {
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
    }
}
