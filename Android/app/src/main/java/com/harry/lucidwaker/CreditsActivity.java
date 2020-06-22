package com.harry.lucidwaker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CreditsActivity extends AppCompatActivity {

    TextView contentTextView;
    TextView backTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        contentTextView = findViewById(R.id.contentTextView);
        backTextView = findViewById(R.id.backTextView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Credits");

        String content = "Binaural Beats\n" +
                "12000 Hz Etheric Force⎪10000 Hz Full Restore⎪Whole Being Regeneration⎪DNA Stimulation⎪Slow Trance\n" +
                "By Lovemotives Meditation Music\n" +
                "https://www.youtube.com/watch?v=jKo_Xhoxdak\n" +
                "License: Creative Commons Attribution license (reuse allowed)\n\n" +
                "Isochronic Tone\n" +
                "Theta Isochronic Tones (7Hz) - Deep Relaxation\n" +
                "By jamz subliminals\n" +
                "https://www.youtube.com/watch?v=hdpoNe7hyHI\n" +
                "License: Creative Commons Attribution license (reuse allowed)\n\n" +
                "Ringtones\n" +
                "Music: https://www.bensound.com/royalty-free-music\n" +
                "Alan Walker - Fade\n" +
                "https://www.youtube.com/watch?v=bM7SZ5SBzyY\n" +
                "Alan Walker - Force\n" +
                "https://www.youtube.com/watch?v=xshEZzpS4CQ\n" +
                "Itro & Tobu - Cloud 9\n" +
                "https://www.youtube.com/watch?v=VtKbiyyVZks\n" +
                "DEAF KEV - Invincible\n" +
                "https://www.youtube.com/watch?v=J2X5mJ3HDYE\n" +
                "Domastic - Weird Dream\n" +
                "https://www.youtube.com/watch?v=w9WwDddpHrg";
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
