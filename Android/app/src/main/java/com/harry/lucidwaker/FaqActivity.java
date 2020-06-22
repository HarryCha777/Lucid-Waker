package com.harry.lucidwaker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FaqActivity extends AppCompatActivity {

    TextView contentTextView;
    TextView backTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        contentTextView = findViewById(R.id.contentTextView);
        backTextView = findViewById(R.id.backTextView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("FAQ");

        String content = "The following is a list of answers (A) to questions (Q) that I was asked frequently.\n\n" +
                "Q: What can Chaining mode be used for?\n" +
                "A: Chaining mode simply repeats FILD mode. While FILD mode plays an alarm only once, " +
                "Chaining mode plays infinite number of alarms with Wait time in-between the consecutive alarms. " +
                "What makes Chaining mode useful is that it can be used to attempt FILD mode multiple times in one night, " +
                "which allows you to have multiple lucid dreams and dream recalls.\n\n" +
                "Q: My alarm does not ring sometimes over the night.\n" +
                "A: The alarm might be delayed by a minute or two or simply not ring at all " +
                "if the alarm is terminated by another app or if your device is low on memory or battery. " +
                "Such situations may result in the alarm's background process being killed continuously " +
                "if your device is turned off and stays inactive for hours as you sleep.\n" +
                "The most reliable course of action to keep the alarm alive over the night is to terminate all other unused apps, restart this app, " +
                "set the alarm right before bed, and refrain from interacting with other apps after setting the alarm to retain more memory space.\n\n" +
                "Q: Does this app help you lucid dream personally?\n" +
                "A: This app is simply a mobile application adaptation of a basic program I wrote a few years ago to help myself lucid dream. " +
                "So yes, this app is absolutely effective for me personally to lucid dream.\n\n" +
                "Q: What did you use to make this app?\n" +
                "A: I used Java in Android Studio to make this app.\n\n" +
                "Q: How can I contact you in private?\n" +
                "A: For any questions or suggestions, please reach out to me at lucidwakerapp@gmail.com. " +
                "And I would appreciate it if you clarify in your email that you use Android, not iOS.";
        contentTextView.setText(content);

        backTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // on action bar's back button pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        App.currActivity = "Main";
        finish();
    }
}
