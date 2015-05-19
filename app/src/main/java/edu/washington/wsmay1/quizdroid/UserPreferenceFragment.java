package edu.washington.wsmay1.quizdroid;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import java.util.prefs.Preferences;

public class UserPreferenceFragment extends PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences,
                false);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        SharedPreferences.OnSharedPreferenceChangeListener spChanged = new
                SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                        if (key.equals("urlPref")) {
                            ((SetPreferencesActivity) getActivity()).getApp().setDownloadUrl(sharedPreferences.getString(key, "blank"));
                        } else {
                            ((SetPreferencesActivity) getActivity()).getApp().setDownloadInterval(sharedPreferences.getInt(key, 5));
                        }
                    }
                };
    }
}