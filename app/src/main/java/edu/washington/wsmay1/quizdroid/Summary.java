package edu.washington.wsmay1.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;


public class Summary extends ActionBarActivity {

    private int qNum;
    private int totalCorrect;
    private String selectedAnswer;
    private String correctAnswer;
    private ArrayList<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Intent quizData = getIntent();
        Bundle bundle = quizData.getExtras();
        this.questions = (ArrayList<Question>) bundle.getSerializable("questions");
        this.selectedAnswer = quizData.getStringExtra("selectedOption");
        this.correctAnswer = quizData.getStringExtra("correctOption");
        this.qNum = quizData.getIntExtra("current", 0);
        this.totalCorrect = quizData.getIntExtra("correct", 0);
        populateChoices(this.selectedAnswer, this.correctAnswer, qNum, totalCorrect);
        determineButton(qNum, questions);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_summary, menu);
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

    public void populateChoices(String provided, String correct, int totalQuestions, int totalCorrect) {
        TextView userChoice = (TextView) findViewById(R.id.providedText);
        TextView correctText = (TextView) findViewById(R.id.correctText);
        TextView correctStats = (TextView) findViewById(R.id.correctStats);
        userChoice.setText(provided);
        correctText.setText(correct);
        correctStats.setText("You have answered " + totalCorrect + " out of " + totalQuestions + " questions correctly");
    }

    public void determineButton(int totalQuestions, ArrayList<Question> questions) {
        Button next = (Button) findViewById(R.id.next);
        if (totalQuestions == questions.size()) {
            next.setText("Finish");
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishQuiz();
                }
            });
        } else {
            next.setText("Next Question");
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextQuestion();
                }
            });
        }
    }

    public void nextQuestion() {
        Intent intent = new Intent(this, Quiz.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("questions", this.questions);
        intent.putExtra("current", this.qNum);
        intent.putExtra("correct", this.totalCorrect);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void finishQuiz() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
