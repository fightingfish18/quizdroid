package edu.washington.wsmay1.quizdroid;


import android.app.DownloadManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.util.concurrent.ScheduledExecutorService;


public class DownloadService extends Service {
    private static int interval;
    private static String source;
    private Timer downloadTimer = null;
    private ScheduledExecutorService downloadService = null;
    private Handler downloadHandler = new Handler();
    private DownloadManager dmManager;
    private long enqueue;
    boolean isAir;
    private MyContextWrapper wrapper;

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
            dmManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            checkPreferences();
            downloadTimer.scheduleAtFixedRate(new DownloadTask(), 0, interval * 6000);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        downloadTimer.cancel();
    }

    public void checkPreferences() {
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String url = mySharedPreferences.getString("urlPref", "blank");
        int duration = Integer.parseInt(mySharedPreferences.getString("delayPref", "5"));
        source = url;
        interval = duration;
        wrapper = new MyContextWrapper(this);
        isAir = wrapper.isAirplaneModeOn();
    }


    class DownloadTask extends TimerTask {
        @Override
        public void run() {
            downloadHandler.post(new Runnable() {
                @Override
                public void run() {
                    checkPreferences();
                    try {
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(source));
                        enqueue = dmManager.enqueue(request);
                        Toast.makeText(getApplicationContext(), source, Toast.LENGTH_SHORT).show();
                    } catch (IllegalArgumentException e) {
                        downloadTimer.cancel();
                        Toast.makeText(getApplicationContext(), "Illegaly formed URL... please enter another", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
