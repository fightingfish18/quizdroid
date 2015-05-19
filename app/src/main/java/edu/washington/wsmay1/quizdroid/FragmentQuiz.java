package edu.washington.wsmay1.quizdroid;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class FragmentQuiz extends ActionBarActivity  {
    private ArrayList<Question> quizQuestions;
    private Topic selectedTopic;
    private String selected;
    //private int activityId;
    private View.OnClickListener beginQuizListener;
    private int qNum;
    private int totalCorrect;
    private String lastCorrect;
    private QuizApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_quiz);
        Intent selection = getIntent();
        myApp = (QuizApp) getApplication();
        HashMap<String, Topic> topicMap = myApp.getTopicMap();
        selected = selection.getStringExtra("subject");
        selectedTopic = topicMap.get(selected);
        this.qNum = 0;
        this.totalCorrect = 0;
        //activityId = selection.getIntExtra("subject", 0);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment overviewFrag = new subjectOverviewFragment();
        transaction.add(R.id.fragmentContainer, overviewFrag);
        transaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public Topic getSelectedTopic() {
        return this.selectedTopic;
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

    public void setQuestionList(ArrayList<Question> questions) {
        this.quizQuestions = questions;
    }

    public ArrayList<Question> getQuizQuestions() {
        return this.quizQuestions;
    }

    public int getqNum() {
        return this.qNum;
    }

    public void setqNum(int num) {
        this.qNum = num;
    }

    public void setTotalCorrect(int correct) {
        this.totalCorrect = correct;
    }

    public int getTotalCorrect() {
        return this.totalCorrect;
    }

    public String getSelected() {
        return this.selected;
    }

    public void setSelected(String answer) {
        this.selected = answer;
    }

    public String getLastCorrect() {
        return this.lastCorrect;
    }

    public void setLastCorrect(String correct) {
        this.lastCorrect = correct;
    }

    public void onDestroy() {
        super.onDestroy();
        myApp.stop();
    }
}
