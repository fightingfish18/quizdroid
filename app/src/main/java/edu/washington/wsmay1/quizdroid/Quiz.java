package edu.washington.wsmay1.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import java.io.Serializable;
import java.util.ArrayList;


public class Quiz extends ActionBarActivity {
    private int answer;
    private int qNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Intent data = getIntent();
        Bundle questionData = data.getExtras();
        ArrayList<Question> questionList = (ArrayList<Question>) questionData.getSerializable("questions");
        qNum = data.getIntExtra("current", 0);
        makeQuestion(questionList.get(qNum));
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

    public void makeQuestion(Question current) {
        TextView questionText = (TextView) findViewById(R.id.qText);
        questionText.setText(current.getQuestionText());
    }
}
