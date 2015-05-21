package edu.washington.wsmay1.quizdroid;


import android.app.DownloadManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class Download extends Service {
    private DownloadManager dmManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
