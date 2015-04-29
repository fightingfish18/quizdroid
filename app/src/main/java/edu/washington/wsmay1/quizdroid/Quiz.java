package edu.washington.wsmay1.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import java.io.Serializable;
import java.util.*;


public class Quiz extends ActionBarActivity {
    private int answer;
    private int qNum;
    private int totalCorrect;
    private String selectedAnswer;
    private String correctAnswer;
    private ArrayList<Question> questions;
    ArrayList<RadioButton> buttons = new ArrayList<RadioButton>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Intent data = getIntent();
        Bundle questionData = data.getExtras();
        this.questions = (ArrayList<Question>) questionData.getSerializable("questions");
        qNum = data.getIntExtra("current", 0);
        totalCorrect = data.getIntExtra("correct", 0);

        makeQuestion(questions.get(qNum));
        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitQuestion();
            }
        });
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
        buttons.add((RadioButton) findViewById(R.id.a));
        buttons.add((RadioButton) findViewById(R.id.b));
        buttons.add((RadioButton) findViewById(R.id.c));
        buttons.add((RadioButton) findViewById(R.id.d));
        TextView questionText = (TextView) findViewById(R.id.qText);
        questionText.setText(current.getQuestionText());
        Map<String, Boolean> questionChoices = current.getChoices();
        Set<String> questionSet = questionChoices.keySet();
        int button = 0;
        for (String question : questionSet) {
            buttons.get(button).setText(question);
            if (questionChoices.get(question)) {
                this.answer = button;
            }
            button++;
        }
    }

    public void submitQuestion() {
        boolean selected = false;
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).isChecked()) {
                selected = true;
                this.selectedAnswer = buttons.get(i).getText().toString();
                if (i == this.answer) {
                    this.totalCorrect++;
                }
            }
            if (i == this.answer) {
                this.correctAnswer = buttons.get(i).getText().toString();
            }
        }
        if (selected) {
            this.qNum++;
            Intent intent = new Intent(this, Summary.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("questions", this.questions);
            intent.putExtra("current", this.qNum);
            intent.putExtra("correct", this.totalCorrect);
            intent.putExtra("selectedOption", this.selectedAnswer);
            intent.putExtra("correctOption", this.correctAnswer);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
