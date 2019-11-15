package com.harry.lucidwaker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    TextView contentTextView;
    TextView backTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        contentTextView = findViewById(R.id.contentTextView);
        backTextView = findViewById(R.id.backTextView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("About");

        String content = "Lucid Waker is an alarm app designed to assist lucid dreamers " +
                "who exercise techniques that utilize alarms, such as FILD and Rausis.\n\n" +
                "Hello, my name is Harry, and I am a college student who has been a lucid dreamer for a few years. " +
                "Since I wanted to apply my programming skills to my lucid dreaming lifestyle, " +
                "I developed a simple program that schedules alarms and tones automatically.\n\n" +
                "And after realizing that I was able to lucid dream more easily using my personalized alarm program, " +
                "I then decided to turn it into a mobile app in hopes of supporting more lucid dreamers like myself.\n\n" +
                "As this is my first app, it lacks many fancy features and designs. " +
                "Nevertheless, I sincerely wish this app would serve its functions " +
                "and assist its users to lucid dream more conveniently!\n\n" +
                "For any questions or suggestions, please reach out to me at lucidwakerapp@gmail.com.\n\n" +
                "I truly thank you for your interest and taking your valuable time reading about me and my app!";
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
        startActivity(new Intent(AboutActivity.this, MainActivity.class));
    }
}
