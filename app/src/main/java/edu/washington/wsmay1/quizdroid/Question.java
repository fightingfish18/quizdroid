package edu.washington.wsmay1.quizdroid;
import java.io.Serializable;
import java.util.*;

/**
 * Created by Smyth on 4/27/2015.
 */
public class Question implements Serializable {
    Map<String, Boolean> choices;
    String questionText;

    public Question(String text, ArrayList<String> answers, int correct) {
        choices = new TreeMap<String, Boolean>();
        for (int i = 0; i < answers.size(); i++) {
            if (i == correct) {
                choices.put(answers.get(i), true);
            } else {
                choices.put(answers.get(i), false);
            }
        }
        questionText = text;
    }


    public boolean isAnswer(String choice) {
        return choices.get(choice);
    }

    public String getQuestionText() {
        return this.questionText;
    }

    public Map<String, Boolean> getChoices() {
        return this.choices;
    }
}
