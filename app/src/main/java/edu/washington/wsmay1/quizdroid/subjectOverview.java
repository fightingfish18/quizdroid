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
        if (selected.equals("Math")) {
            choices.add("1");
            choices.add("3");
            choices.add("5");
            choices.add("2");
            Question one = new Question("What is 1 + 1?", choices, 3);
            choices.clear();
            choices.add("10");
            choices.add("20");
            choices.add("100");
            choices.add("45");
            Question two = new Question("What is 10 x 10?", choices, 2);
            choices.clear();
            choices.add("25");
            choices.add("30");
            choices.add("67");
            choices.add("10");
            Question three = new Question("Find x.  X + 5 = 30", choices, 0);
            quizQuestions.add(one);
            quizQuestions.add(two);
            quizQuestions.add(three);
        } else if (selected.equals("Physics")) {
            choices.add("e=mc^2");
            choices.add("9.8ms/s");
            choices.add("y = mx + b");
            choices.add("F = ma");
            Question one = new Question("What is the equation for calculating Force?", choices, 3);
            choices.clear();
            choices.add("Nikola Tesla");
            choices.add("Galileo");
            choices.add("Sir. Isaac Newton");
            choices.add("Albert Einstein");
            Question two = new Question("Who pioneered the theory of gravity?", choices, 1);
            quizQuestions.add(one);
            quizQuestions.add(two);
        } else {
            choices.add("The Brotherhood");
            choices.add("The X-Men");
            choices.add("The Disciples");
            choices.add("New Age");
            Question one = new Question("What do the mutants who follow Magneto call themselves?", choices, 0);
            choices.clear();
            choices.add("Clark Kent");
            choices.add("Lex Luthor");
            choices.add("Peter Parker");
            choices.add("Xavier Kent");
            Question two = new Question("What is Spider Man's real name?", choices, 2);
            choices.clear();
            choices.add("The Last Stand");
            choices.add("Age of Ultron");
            choices.add("Apocalypse");
            choices.add("The Winter Soldier");
            Question three = new Question("What is the title of the second Avengers movie?", choices, 1);
            quizQuestions.add(one);
            quizQuestions.add(two);
            quizQuestions.add(three);
        }
        numQuestions(quizQuestions.size());
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

    public void numQuestions(int num) {
        TextView totalNum = (TextView) findViewById(R.id.textView);
        String text = totalNum.getText().toString();
        totalNum.setText(text + " " + num);
    }
}
