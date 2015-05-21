package edu.washington.wsmay1.quizdroid;
import org.json.*;

import java.util.*;
import java.io.*;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

public class QuizApp extends android.app.Application implements TopicRepository {
    private HashMap<String, Topic> topicMap;
    private static QuizApp instance = null;
    private int downloadInterval;
    private String downloadUrl;
    private Intent downloadServiceIntent;
    private DownloadManager dmManager;
    private boolean initialized = false;


    public QuizApp() {

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
        dmManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String url = mySharedPreferences.getString("urlPref", "http://tednewardsandbox.site44.com/questions.json");
        int duration = Integer.parseInt(mySharedPreferences.getString("delayPref", "5"));
        downloadInterval = duration;
        downloadUrl = url;
        downloadServiceIntent = new Intent(this, DownloadService.class);
        start();
    }

    public void stop() {
        stopService(downloadServiceIntent);
    }

    public void start() {
        startService(downloadServiceIntent);
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
            topicMap = new HashMap<String, Topic>();
            for (int i = 0; i < data.length(); i++) {
                JSONObject topicData = (JSONObject) data.get(i);
                String title = topicData.getString("title");
                String description = topicData.getString("desc");
                JSONArray questionData = topicData.getJSONArray("questions");
                ArrayList<Question> questions = new ArrayList<Question>();
                for (int j = 0; j < questionData.length(); j++) {
                    JSONObject questionInfo = (JSONObject) questionData.get(j);
                    String questionText = questionInfo.getString("text");
                    int answer = questionInfo.getInt("answer");
                    answer--;
                    JSONArray choices = questionInfo.getJSONArray("answers");
                    ArrayList<String> choicesList = new ArrayList<String>();
                    for (int k = 0; k < choices.length(); k++) {
                        choicesList.add(choices.get(k).toString());
                    }
                    questions.add(new Question(questionText, choicesList, answer));
                }
                Topic topic = new Topic(title, description, questions);
                topicMap.put(title, topic);
            }
            initialized = true;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONArray createJSON() {
        String json = null;
        try {
            File myFile = new File(getFilesDir().getAbsolutePath(), "/data.json");
            FileInputStream inputStream = new FileInputStream(myFile);
            //InputStream inputStream = getAssets().open("questions.json");
            json = readJSONFile(inputStream);
            JSONArray data = new JSONArray(json);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeToFile(String data) {
        try {
            File file = new File(getFilesDir().getAbsolutePath(), "/data.json");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public int getDownloadInterval() {
        return downloadInterval;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadInterval(int interval) {
        downloadInterval = interval;
    }

    public void setDownloadUrl(String url) {
        downloadUrl = url;
    }

    private Intent getDownloadServiceIntent() {
        return downloadServiceIntent;
    }

    public HashMap<String, Topic> getTopicMap() {
        return this.topicMap;
    }

    public boolean getInitialized() {
        return this.initialized;
    }
}
