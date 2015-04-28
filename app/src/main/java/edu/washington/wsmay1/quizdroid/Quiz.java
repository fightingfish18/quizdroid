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
    ArrayList<RadioButton> buttons = new ArrayList<RadioButton>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Intent data = getIntent();
        Bundle questionData = data.getExtras();
        final ArrayList<Question> questionList = (ArrayList<Question>) questionData.getSerializable("questions");
        qNum = data.getIntExtra("current", 0);
        totalCorrect = data.getIntExtra("correct", 0);

        makeQuestion(questionList.get(qNum));
        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitQuestion(questionList);
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

    public void submitQuestion(ArrayList<Question> questions) {
        boolean selected = false;
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).isSelected()) {
                selected = true;
                if (i == this.answer) {
                    this.totalCorrect++;
                    break;
                }
            }
        }
        this.qNum++;
        Intent intent = new Intent(this, Summary.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("questions", questions);
        intent.putExtra("current", this.qNum);
        intent.putExtra("correct", this.totalCorrect);
        startActivity(intent);
    }
}
