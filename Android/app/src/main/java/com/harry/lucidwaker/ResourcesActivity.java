package com.harry.lucidwaker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResourcesActivity extends AppCompatActivity {

    TextView contentTextView;
    TextView backTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        contentTextView = findViewById(R.id.contentTextView);
        backTextView = findViewById(R.id.backTextView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Resources");

        String content = "The following is a list of lucid dreaming resources that I personally used and recommend.\n" +
                "Click on each of their links to visit them online.\n\n" +
                "How to Lucid\n" +
                "howtolucid.com\n" +
                "As a website managed by Stefan, this website is very beginner-friendly and personal.\n" +
                "I highly recommend checking out his website " +
                "as it contains many informative articles and useful tips and suggestions on lucid dreaming.\n\n" +
                "Lucid Dream Society\n" +
                "luciddreamsociety.com\n" +
                "Lucid Dream Society is an online community run by Merilin, a lucid dream enthusiast.\n" +
                "It is a home of numerous beneficial suggestions and free educational materials to learn from.\n" +
                "Therefore, I personally recommend reading its articles, especially for the lucid dreaming novices.\n\n" +
                "Exploring the World of Lucid Dreaming\n" +
                "amazon.com/Exploring-World-Dreaming-Stephen-LaBerge/dp/034537410X\n" +
                "This is a book written by Stephen LaBerge, a scientist and researcher with PhD from Stanford University, in 1990.\n" +
                "As this book can be considered the Bible of lucid dreaming, simply reading this book everyday can get its readers into the mindset of lucid dreaming.\n" +
                "I strongly recommend this book to all lucid dreamers!";
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
        startActivity(new Intent(ResourcesActivity.this, MainActivity.class));
    }
}
