package edu.washington.wsmay1.quizdroid;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class SetPreferencesActivity extends ActionBarActivity {
    private QuizApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApp = (QuizApp) getApplication();
        setContentView(R.layout.activity_set_preferences);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment prefFrag = new UserPreferenceFragment();
        transaction.add(R.id.prefContainer, prefFrag);
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment prefFrag = new UserPreferenceFragment();
        transaction.add(R.id.prefContainer, prefFrag);
        transaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_preferences, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public QuizApp getApp() {
        return myApp;
    }
}
