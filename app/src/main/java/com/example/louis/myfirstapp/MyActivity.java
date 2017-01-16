package com.example.louis.myfirstapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class MyActivity extends AppCompatActivity {

    String[] s = new String[193];
    static int num;
    static int correct = 0;
    static int qAsked = 0;
    static int ans = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("Capitals.txt")));

            // do reading, usually loop until end of file reading
            String mLine = reader.readLine();
            int index = 0;
            while (mLine != null) {
                s[index] = mLine;
                index++;
                mLine = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        newQuestion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically le clicks on the Home/Uphand button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.reset) {
            correct = 0;
            qAsked = 0;

            newQuestion();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {

        TextView answer1 = (TextView) findViewById(R.id.answer1);
        TextView answer2 = (TextView) findViewById(R.id.answer2);
        TextView answer3 = (TextView) findViewById(R.id.answer3);
        TextView answer4 = (TextView) findViewById(R.id.answer4);

        answer1.setEnabled(false);
        answer2.setEnabled(false);
        answer3.setEnabled(false);
        answer4.setEnabled(false);

        int id = view.getId();
        if (id == ans)  {
            correct++;
            qAsked++;

            newQuestion();

        } else {
            new AlertDialog.Builder(this)
                .setTitle("Incorrect!")
                .setMessage("The capital is " + s[num].substring(s[num].indexOf(':') + 2).trim())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        qAsked++;
                        newQuestion();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        }
    }

    public void newQuestion() {
        TextView question = (TextView) findViewById(R.id.question);

        Random r = new Random();
        int prev = num;
        while (s[num] == null || prev == num) num = r.nextInt(193);
        String country = s[num].substring(0, s[num].indexOf('-') - 1);
        String questionString = "What is the capital city of " + country + "?";
        question.setText(questionString);

        TextView answers[]  = new TextView[4];
        answers[0] = (TextView) findViewById(R.id.answer1);
        answers[1] = (TextView) findViewById(R.id.answer2);
        answers[2] = (TextView) findViewById(R.id.answer3);
        answers[3] = (TextView) findViewById(R.id.answer4);

        answers[0].setTypeface(null, Typeface.NORMAL);
        answers[1].setTypeface(null, Typeface.NORMAL);
        answers[2].setTypeface(null, Typeface.NORMAL);
        answers[3].setTypeface(null, Typeface.NORMAL);

        int chosen = r.nextInt(4);
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        numbers.add(num);

        // setting text of answer buttons
        answers[chosen].setText(s[num].substring(s[num].indexOf('-') + 2).trim());
        ans = answers[chosen].getId();
        int curr = -1;
        int random = r.nextInt(193);
        while (numbers.size() != 4) {
            curr++; if (chosen == curr) curr++;
            while (numbers.contains(random) || s[random] == null) random = r.nextInt(193);
            if (numbers.size() == 1) {
                answers[curr].setText(s[random].substring(s[random].indexOf('-') + 2).trim());
            } else if (numbers.size() == 2) {
                answers[curr].setText(s[random].substring(s[random].indexOf('-') + 2).trim());
            } else if (numbers.size() == 3) {
                answers[curr].setText(s[random].substring(s[random].indexOf('-') + 2).trim());
            }
            numbers.add(random);
        }


        TextView score = (TextView)findViewById(R.id.score);
        String scoreString = "Score: " + correct + "/" + qAsked;
        score.setText(scoreString);

        answers[0].setEnabled(true);
        answers[1].setEnabled(true);
        answers[2].setEnabled(true);
        answers[3].setEnabled(true);
    }
}
