package edu.washington.wsmay1.quizdroid;


import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizQuestionFragment extends Fragment {
    private int answer;
    private int qNum;
    private String selectedAnswer;
    private String correctAnswer;
    private ArrayList<Question> questions;
    ArrayList<RadioButton> buttons = new ArrayList<RadioButton>();

    public static QuizQuestionFragment newInstance(String param1, String param2) {
        QuizQuestionFragment fragment = new QuizQuestionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public QuizQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quiz_question, container, false);
        this.qNum = ((FragmentQuiz)getActivity()).getqNum();
        this.questions = ((FragmentQuiz)getActivity()).getQuizQuestions();
        makeQuestion(questions.get(this.qNum), v);
        Button submit = (Button) v.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitQuestion();
            }
        });
        return v;
    }

    public void makeQuestion(Question current, View v) {
        buttons.add((RadioButton) v.findViewById(R.id.a));
        buttons.add((RadioButton) v.findViewById(R.id.b));
        buttons.add((RadioButton) v.findViewById(R.id.c));
        buttons.add((RadioButton) v.findViewById(R.id.d));
        TextView questionText = (TextView) v.findViewById(R.id.qText);
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
                ((FragmentQuiz)getActivity()).setSelected(buttons.get(i).getText().toString());
                if (i == this.answer) {
                    int correctAnswers = ((FragmentQuiz)getActivity()).getTotalCorrect();
                    correctAnswers += 1;
                    ((FragmentQuiz)getActivity()).setTotalCorrect(correctAnswers);
                }
            }
            if (i == this.answer) {
                ((FragmentQuiz)getActivity()).setLastCorrect(buttons.get(i).getText().toString());
            }
        }
        if (selected) {
            this.qNum++;
            ((FragmentQuiz)getActivity()).setqNum(this.qNum);
            Fragment summaryFragment = new AnswerSummaryFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, summaryFragment).commit();
        }
    }


}
