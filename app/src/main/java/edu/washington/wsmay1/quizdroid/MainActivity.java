package edu.washington.wsmay1.quizdroid;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class MainActivity extends ActionBarActivity {
    private QuizApp myApp;
    private String topicName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myApp = (QuizApp) getApplication();
        HashMap<String, Topic> topics = myApp.getTopicMap();
        Set<String> topicNames = topics.keySet();
        ArrayList<Button> buttons = new ArrayList<Button>();
        Button math = (Button) findViewById(R.id.math);
        buttons.add(math);
        Button physics = (Button) findViewById(R.id.physics);
        buttons.add(physics);
        Button marvel = (Button) findViewById(R.id.marvel);
        buttons.add(marvel);
        int i = 0;
        for (Iterator<String> iter = topicNames.iterator(); iter.hasNext();) {
            String topic = iter.next();
            buttons.get(i).setText(topic);
            i++;
        }

        View.OnClickListener buttonPush = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button)v;
                handleButton(b.getText().toString());
            }
        };
        math.setOnClickListener(buttonPush);
        physics.setOnClickListener(buttonPush);
        marvel.setOnClickListener(buttonPush);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(this, SetPreferencesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void handleButton(String title) {
        Intent intent = new Intent(this, FragmentQuiz.class);
        intent.putExtra("subject", title);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myApp.stop();
    }
}
