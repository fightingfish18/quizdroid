package edu.washington.wsmay1.quizdroid;

import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
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
 * Activities that contain this fragment must implement the
 * {@link subjectOverviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link subjectOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class subjectOverviewFragment extends Fragment {

    //private int activityId;
    private Topic selectedTopic;
    private OnFragmentInteractionListener mListener;
    private String selected;
    private ArrayList<Question> quizQuestions = new ArrayList<Question>();


    public static subjectOverviewFragment newInstance(String param1, String param2) {
        subjectOverviewFragment fragment = new subjectOverviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public subjectOverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.selectedTopic = ((FragmentQuiz)getActivity()).getSelectedTopic();
        View v = inflater.inflate(R.layout.fragment_subject_overview, container, false);
        Button begin = (Button) v.findViewById(R.id.start);
        v = populateView(v);
        createQuiz();
        numQuestions(this.quizQuestions.size(), (TextView) v.findViewById(R.id.textView));
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginQuiz();
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
       /* try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        } */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public View populateView(View v) {
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView overview = (TextView) v.findViewById(R.id.overview);
        /*
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
        */
        title.setText(this.selectedTopic.getTitle());
        overview.setText(this.selectedTopic.getDescription());
        return v;
    }

    public void createQuiz() {
        /*
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
        */
        this.quizQuestions = selectedTopic.getQuestions();
    }


    public void numQuestions(int num, TextView totalNum) {
        String text = totalNum.getText().toString();
        totalNum.setText(text + " " + num);
    }

    public void beginQuiz() {
        ((FragmentQuiz)getActivity()).setQuestionList(this.quizQuestions);
        Fragment questionFragment = new QuizQuestionFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, questionFragment).commit();
    }
}
