package edu.washington.wsmay1.quizdroid;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

public class MyContextWrapper extends ContextWrapper {

    public MyContextWrapper(Context base) {
        super(base);
    }

    public boolean isAirplaneModeOn() {
        return Settings.System.getInt(getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null);
    }
}