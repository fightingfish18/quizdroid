package edu.washington.wsmay1.quizdroid;


import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnswerSummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnswerSummaryFragment extends Fragment {

    private int qNum;
    private int totalCorrect;
    private String selectedAnswer;
    private String correctAnswer;
    private ArrayList<Question> questions;

    public static AnswerSummaryFragment newInstance(String param1, String param2) {
        AnswerSummaryFragment fragment = new AnswerSummaryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AnswerSummaryFragment() {
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
        View v = inflater.inflate(R.layout.fragment_answer_summary, container, false);
        this.qNum = ((FragmentQuiz)getActivity()).getqNum();
        this.totalCorrect = ((FragmentQuiz)getActivity()).getTotalCorrect();
        this.selectedAnswer = ((FragmentQuiz)getActivity()).getSelected();
        this.correctAnswer = ((FragmentQuiz)getActivity()).getLastCorrect();
        this.questions = ((FragmentQuiz)getActivity()).getQuizQuestions();
        populateChoices(this.selectedAnswer, this.correctAnswer, this.qNum, this.totalCorrect, v);
        determineButton(qNum, questions, v);
        return v;
    }

    public void populateChoices(String provided, String correct, int totalQuestions, int totalCorrect, View v) {
        TextView userChoice = (TextView) v.findViewById(R.id.providedText);
        TextView correctText = (TextView) v.findViewById(R.id.correctText);
        TextView correctStats = (TextView) v.findViewById(R.id.correctStats);
        userChoice.setText(provided);
        correctText.setText(correct);
        correctStats.setText("You have answered " + totalCorrect + " out of " + totalQuestions + " questions correctly");
    }

    public void determineButton(int totalQuestions, ArrayList<Question> questions, View v) {
        Button next = (Button) v.findViewById(R.id.next);
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
        Fragment questionFragment = new QuizQuestionFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, questionFragment).commit();
    }

    public void finishQuiz() {
        Context context = ((FragmentQuiz)getActivity()).getApplicationContext();
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }


}
