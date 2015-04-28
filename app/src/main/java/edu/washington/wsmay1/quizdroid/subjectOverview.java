package edu.washington.wsmay1.quizdroid;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.content.Intent;
import java.util.*;


public class subjectOverview extends ActionBarActivity {
    private String selected;
    private ArrayList<Question> quizQuestions = new ArrayList<Question>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_overview);
        Intent selection = getIntent();
        int activityId = selection.getIntExtra("subject", 0);
        populateView(activityId);
        Button begin = (Button) findViewById(R.id.start);
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginQuiz();
            }
        });
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
                selected = "Math";
                break;
            case(R.id.physics):
                title.setText("Physics Quiz");
                overview.setText("Physics, closely related to math, involves studying how matter interacts");
                selected = "Physics";
                break;
            case(R.id.marvel):
                title.setText("Marvel Superheroes Quiz");
                overview.setText("Marvel is one of the classic comic companies with iconic characters such as Spiderman, the X-Men, and Captain America");
                selected = "Marvel";
                break;
        }
        createQuiz(selected);
    }

    public void createQuiz(String selected) {
        ArrayList<String> choices = new ArrayList<String>();
        choices.add("1");
        choices.add("3");
        choices.add("5");
        choices.add("2");
        Question one = new Question("What is 1 + 1", choices, 3);
        quizQuestions.add(one);
    }
    public void beginQuiz() {
        Intent intent = new Intent(this, Quiz.class);
        intent.putExtra("Subject", selected);
        Bundle bundle = new Bundle();
        bundle.putSerializable("questions", quizQuestions);
        intent.putExtras(bundle);
        intent.putExtra("current", 0);
        startActivity(intent);
    }
}
