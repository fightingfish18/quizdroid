package edu.washington.wsmay1.quizdroid;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.content.Intent;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button math = (Button) findViewById(R.id.math);
        Button physics = (Button) findViewById(R.id.physics);
        Button marvel = (Button) findViewById(R.id.marvel);

        View.OnClickListener buttonPush = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButton(v.getId());
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void handleButton(int id) {
        Intent intent = new Intent(this, FragmentQuiz.class);
        intent.putExtra("subject", id);
        startActivity(intent);
    }
}
