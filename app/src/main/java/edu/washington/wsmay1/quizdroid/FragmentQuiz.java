package edu.washington.wsmay1.quizdroid;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.ParcelFileDescriptor;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class FragmentQuiz extends ActionBarActivity  {
    private ArrayList<Question> quizQuestions;
    private Topic selectedTopic;
    private String selected;
    //private int activityId;
    private View.OnClickListener beginQuizListener;
    private int qNum;
    private int totalCorrect;
    private String lastCorrect;
    private QuizApp myApp;
    private DownloadManager dmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_quiz);
        Intent selection = getIntent();
        myApp = (QuizApp) getApplication();
        dmManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        HashMap<String, Topic> topicMap = myApp.getTopicMap();
        selected = selection.getStringExtra("subject");
        selectedTopic = topicMap.get(selected);
        this.qNum = 0;
        this.totalCorrect = 0;
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE); // Add more filters here that you want the receiver to listen to
        registerReceiver(receiver, filter);
        //activityId = selection.getIntExtra("subject", 0);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment overviewFrag = new subjectOverviewFragment();
        transaction.add(R.id.fragmentContainer, overviewFrag);
        transaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public Topic getSelectedTopic() {
        return this.selectedTopic;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SetPreferencesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setQuestionList(ArrayList<Question> questions) {
        this.quizQuestions = questions;
    }

    public ArrayList<Question> getQuizQuestions() {
        return this.quizQuestions;
    }

    public int getqNum() {
        return this.qNum;
    }

    public void setqNum(int num) {
        this.qNum = num;
    }

    public void setTotalCorrect(int correct) {
        this.totalCorrect = correct;
    }

    public int getTotalCorrect() {
        return this.totalCorrect;
    }

    public String getSelected() {
        return this.selected;
    }

    public void setSelected(String answer) {
        this.selected = answer;
    }

    public String getLastCorrect() {
        return this.lastCorrect;
    }

    public void setLastCorrect(String correct) {
        this.lastCorrect = correct;
    }

    public void onDestroy() {
        super.onDestroy();
        myApp.stop();
    }

    public void createDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Airplane Mode");

        // set dialog message
        alertDialogBuilder
                .setMessage("Airplane Mode is currently enabled, would you like to disable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void createFailDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Download Failed");

        // set dialog message
        alertDialogBuilder
                .setMessage("Your download has failed.  Keep trying or Exit?")
                .setCancelable(false)
                .setPositiveButton("Keep Trying", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        FragmentQuiz.this.finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myApp.stop();
        myApp.start();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE); // Add more filters here that you want the receiver to listen to
        registerReceiver(receiver, filter);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (dmManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                if (downloadID != 0) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadID);
                    Cursor c = dmManager.query(query);
                    if (c.moveToFirst()) {
                        int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        Log.d("DM Sample", "Status Check: " + status);
                        switch (status) {
                            case DownloadManager.STATUS_PAUSED:
                            case DownloadManager.STATUS_PENDING:
                            case DownloadManager.STATUS_RUNNING:
                                break;
                            case DownloadManager.STATUS_SUCCESSFUL:
                                // The download-complete message said the download was "successfu" then run this code
                                ParcelFileDescriptor file;
                                StringBuffer strContent = new StringBuffer("");

                                try {
                                    file = dmManager.openDownloadedFile(downloadID);
                                    FileInputStream fis = new FileInputStream(file.getFileDescriptor());
                                    Scanner console = new Scanner(fis);
                                    while (console.hasNextLine()) {
                                        strContent.append(console.nextLine());
                                    }
                                    String data = strContent.toString();
                                    Log.e("data", data);
                                    myApp.writeToFile(data);
                                    File myFile = new File(getFilesDir().getAbsolutePath(), "/data.json");
                                    myApp.createTopicData(myApp.createJSON());

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case DownloadManager.STATUS_FAILED:
                                // YOUR CODE HERE! Your download has failed! Now what do you want it to do? Retry? Quit application? up to you!
                                Toast.makeText(getApplicationContext(), "Download Failed", Toast.LENGTH_SHORT).show();
                                createFailDialog();
                                break;
                        }
                    }
                }
            }
        }
    };

}
