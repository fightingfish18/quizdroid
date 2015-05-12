package edu.washington.wsmay1.quizdroid;
import org.json.*;

import java.util.*;
import java.io.*;
import android.util.Log;

public class QuizApp extends android.app.Application implements TopicRepository {
    private HashMap<String, Topic> topicMap;
    private static QuizApp instance = null;


    public QuizApp() {
        Log.e("QuizApp", "QuizApp created!");
    }

    public static QuizApp getInstance() {
        if (instance == null) {
            instance = new QuizApp();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        topicMap = new HashMap<String, Topic>();
        String json = null;

        // Fetch data.json in assets/ folder
        try {
            InputStream inputStream = getAssets().open("questions.json");
            json = readJSONFile(inputStream);
            JSONArray data = new JSONArray(json);
            createTopicData(data);



        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("quizapp", "this worked");

    }

    public String readJSONFile(InputStream inputStream) throws IOException {
        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();
        return new String(buffer, "UTF-8");
    }

    public void createTopicData(JSONArray data) {
        try {
            for (int i = 0; i < data.length(); i++) {
                Log.e("json", data.get(i).toString());
                JSONObject topicData = (JSONObject) data.get(i);
                String title = topicData.getString("title");
                String description = topicData.getString("desc");
                JSONArray questionData = topicData.getJSONArray("questions");
                ArrayList<Question> questions = new ArrayList<Question>();
                for (int j = 0; j < questionData.length(); j++) {
                    JSONObject questionInfo = (JSONObject) questionData.get(i);
                    String questionText = questionInfo.getString("text");
                    int answer = questionInfo.getInt("answer");
                    answer--;
                    JSONArray choices = questionInfo.getJSONArray("answers");
                    ArrayList<String> choicesList = new ArrayList<String>();
                    for (int k = 0; k < choices.length(); k++) {
                        choicesList.add(choices.get(i).toString());
                    }
                    questions.add(new Question(questionText, choicesList, answer));
                }
                Topic topic = new Topic(title, description, questions);
                topicMap.put(title, topic);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
