package edu.washington.wsmay1.quizdroid;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.ParcelFileDescriptor;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;


public class MainActivity extends ActionBarActivity {
    private QuizApp myApp;
    private String topicName;
    private DownloadManager dmManager;
    private boolean initialized;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myApp = (QuizApp) getApplication();
        if (new MyContextWrapper(MainActivity.this).isAirplaneModeOn()) {
            myApp.stop();
            createDialog();
        }
        dmManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE); // Add more filters here that you want the receiver to listen to
        registerReceiver(receiver, filter);
        initialized = myApp.getInitialized();
        if (initialized) {
            initializeQuiz();
        }
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
                                    Log.e("asdf", myFile.getAbsolutePath());
                                    myApp.createTopicData(myApp.createJSON());
                                    initialized = true;
                                    initializeQuiz();

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

    public void initializeQuiz() {
        HashMap<String, Topic> topics = myApp.getTopicMap();
        Set<String> topicNames = topics.keySet();
        ArrayList<Button> buttons = new ArrayList<Button>();
        Button math = (Button) findViewById(R.id.math);
        buttons.add(math);
        Button physics = (Button) findViewById(R.id.physics);
        buttons.add(physics);
        Button marvel = (Button) findViewById(R.id.marvel);
        buttons.add(marvel);
        int i = 0;
        for (Iterator<String> iter = topicNames.iterator(); iter.hasNext();) {
            String topic = iter.next();
            buttons.get(i).setText(topic);
            i++;
        }
        View.OnClickListener buttonPush = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button)v;
                handleButton(b.getText().toString());
            }
        };
        math.setOnClickListener(buttonPush);
        physics.setOnClickListener(buttonPush);
        marvel.setOnClickListener(buttonPush);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(this, SetPreferencesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void handleButton(String title) {
        Intent intent = new Intent(this, FragmentQuiz.class);
        intent.putExtra("subject", title);
        startActivity(intent);
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
                    MainActivity.this.finish();
                }
            });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myApp.stop();
    }
}
