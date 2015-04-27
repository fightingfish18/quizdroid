package edu.washington.wsmay1.quizdroid;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import android.content.Intent;


public class subjectOverview extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_overview);
        Intent selection = getIntent();
        int activityId = selection.getIntExtra("subject", 0);
        populateView(activityId);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subject_overview, menu);
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

    public void populateView(int activityId) {
        TextView title = (TextView) findViewById(R.id.title);
        TextView overview = (TextView) findViewById(R.id.overview);
        switch(activityId) {
            case(R.id.math):
                title.setText("Math Quiz");
                overview.setText("Math is an ancient field of study involving addition, subtraction, multiplication, geometry, trigonometry, and more!");
                break;
            case(R.id.physics):
                title.setText("Physics Quiz");
                overview.setText("Physics, closely related to math, involves studying how matter interacts");
                break;
            case(R.id.marvel):
                title.setText("Marvel Superheroes Quiz");
                overview.setText("Marvel is one of the classic comic companies with iconic characters such as Spiderman, the X-Men, and Captain America");
                break;
        }
    }
}
