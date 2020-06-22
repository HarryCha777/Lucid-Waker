package com.harry.lucidwaker;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Calendar;

public class AlarmSetActivity extends AppCompatActivity {

    ConstraintLayout rateAskPopUpConstraintLayout, rateYesPopUpConstraintLayout, rateNoPopUpConstraintLayout;
    TextView rateAskPopUpYesTextView, rateAskPopUpNoTextView,
            rateYesPopUpYesTextView, rateYesPopUpNoTextView,
            rateNoPopUpYesTextView, rateNoPopUpNoTextView;

    String prevActivity = "Main";
    TextView modeTextView;
    TextView countdownTextView;
    TextView alarmTimeTextView;
    TextView sleepHelpTextView;
    static Spinner sleepHelpSpinner;
    ImageButton sleepHelpImageButton;
    Button quitButton;
    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_set);

        rateAskPopUpConstraintLayout = findViewById(R.id.rateAskPopUpConstraintLayout);
        rateAskPopUpYesTextView = findViewById(R.id.rateAskPopUpYesTextView);
        rateAskPopUpNoTextView = findViewById(R.id.rateAskPopUpNoTextView);

        rateYesPopUpConstraintLayout = findViewById(R.id.rateYesPopUpConstraintLayout);
        rateYesPopUpYesTextView = findViewById(R.id.rateYesPopUpYesTextView);
        rateYesPopUpNoTextView = findViewById(R.id.rateYesPopUpNoTextView);

        rateNoPopUpConstraintLayout = findViewById(R.id.rateNoPopUpConstraintLayout);
        rateNoPopUpYesTextView = findViewById(R.id.rateNoPopUpYesTextView);
        rateNoPopUpNoTextView = findViewById(R.id.rateNoPopUpNoTextView);

        modeTextView = findViewById(R.id.modeTextView);
        countdownTextView = findViewById(R.id.countdownTextView);
        alarmTimeTextView = findViewById(R.id.alarmTimeTextView);
        sleepHelpTextView = findViewById(R.id.sleepHelpTextView);
        sleepHelpSpinner = findViewById(R.id.sleepHelpSpinner);
        sleepHelpImageButton = findViewById(R.id.sleepHelpImageButton);
        quitButton = findViewById(R.id.quitButton);

        rateAskPopUpConstraintLayout.setVisibility(View.GONE);
        rateYesPopUpConstraintLayout.setVisibility(View.GONE);
        rateNoPopUpConstraintLayout.setVisibility(View.GONE);
        checkRatePopUp();

        String[] items = new String[]{"Rain", "Meditation", "White Noise", "Isochronic Tone", "Binaural Beats"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, items);
        sleepHelpSpinner.setAdapter(adapter);
        sleepHelpSpinner.setSelection(adapter.getPosition(App.sleepHelpName));

        sleepHelpImageButton.setImageResource(R.drawable.play);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100); // run every 0.1 sec.
                    } catch (InterruptedException e) {
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (App.currActivity.equals("AlarmSet")) {
                                if (prevActivity.equals("Main")) { // run only once each time this activity starts
                                    if (App.mode == 1)
                                        modeTextView.setText("FILD");
                                    else if (App.mode == 2)
                                        modeTextView.setText("Chaining");
                                    else modeTextView.setText("Rausis");
                                    alarmTimeTextView.setText(getAlarmTime());

                                    // set setDay depending on whether set time is before current time or not
                                    App.setDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                                    int currHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                                    int currMin = Calendar.getInstance().get(Calendar.MINUTE);
                                    if (App.setHour < currHour || (App.setHour == currHour && App.setMin < currMin)) {
                                        App.setDay++; // ring tomorrow
                                        if (App.setDay > 7) {
                                            App.setDay -= 7; // DAY_OF_WEEK is from 1 to 7 inclusive
                                        }
                                    }

                                    serviceIntent = new Intent(AlarmSetActivity.this, MyBackgroundService.class);
                                    startService(serviceIntent);
                                }

                                handleCountdown();
                                handleSleepHelp();
                            }
                            prevActivity = App.currActivity;
                        }
                    });
                }
            }
        };

        Thread t = new Thread(r);
        t.start();

        sleepHelpImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.isSleepHelpOn = !App.isSleepHelpOn;
                if (App.isSleepHelpOn)
                    sleepHelpImageButton.setImageResource(R.drawable.pause);
                else sleepHelpImageButton.setImageResource(R.drawable.play);
            }
        });
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(serviceIntent);
                App.isAlarmSet = false;
                App.currActivity = "Main";
                finish();
            }
        });
    }

    String getAlarmTime() {
        int setHour = App.setHour;
        int setMin = App.setMin;

        String setMinStr = Integer.toString(setMin);
        if (setMin < 10)
            setMinStr = "0" + setMinStr;
        String period = "AM";

        if (setHour == 12) {
            period = "PM";
        } else if (setHour > 12) {
            setHour -= 12;
            period = "PM";
        } else if (setHour == 0) {
            setHour = 12;
        }

        return setHour + ":" + setMinStr + " " + period;
    }

    void handleCountdown() {
        if (App.isAlarming) {
            countdownTextView.setText("The Alarm Began !");
        } else {
            App.setCountdown();
            countdownTextView.setText(App.countdownText);
        }
    }

    void handleSleepHelp() {
        if (App.isSleepHelpOn)
            sleepHelpImageButton.setImageResource(R.drawable.pause);
        // keep running this since it will be reset to play image when app is exited and returned

        App.sleepHelpName = sleepHelpSpinner.getSelectedItem().toString();
        if (App.isAlarming) {
            sleepHelpTextView.setVisibility(View.GONE);
            sleepHelpSpinner.setVisibility(View.GONE);
            sleepHelpImageButton.setVisibility(View.GONE);
        }
    }

    void checkRatePopUp() {
        // check to show rate pop up or not
        // see Raghav Sood's answer in https://stackoverflow.com/questions/14514579/how-to-implement-rate-it-feature-in-android-app
        SharedPreferences prefs = getApplication().getSharedPreferences("App Rater", 0);
        if (prefs.getBoolean("Don't Show Again", false)) {
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("Launch Count", 0) + 1;
        editor.putLong("Launch Count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("First Launch Date", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("First Launch Date", date_firstLaunch);
        }

        final int DAYS_UNTIL_PROMPT = 3; // Min number of days
        final int LAUNCHES_UNTIL_PROMPT = 4; // Min number of launches

        // Wait for at least n days and n launches before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRatePopUp(editor);
            }
        }

        editor.commit();
    }

    void showRatePopUp(final SharedPreferences.Editor editor) {
        rateAskPopUpConstraintLayout.setVisibility(View.VISIBLE);

        rateAskPopUpYesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateAskPopUpConstraintLayout.setVisibility(View.GONE);
                rateYesPopUpConstraintLayout.setVisibility(View.VISIBLE);
            }
        });
        rateAskPopUpNoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateAskPopUpConstraintLayout.setVisibility(View.GONE);
                rateNoPopUpConstraintLayout.setVisibility(View.VISIBLE);
            }
        });

        rateYesPopUpYesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + "com.harry.lucidwaker");
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(AlarmSetActivity.this, "Sorry, something went wrong.", Toast.LENGTH_LONG).show();
                }

                rateYesPopUpConstraintLayout.setVisibility(View.GONE);
                if (editor != null) {
                    editor.putBoolean("Don't Show Again", true);
                    editor.commit();
                }
            }
        });
        rateYesPopUpNoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateYesPopUpConstraintLayout.setVisibility(View.GONE);
                if (editor != null) {
                    editor.putBoolean("Don't Show Again", true);
                    editor.commit();
                }
            }
        });

        rateNoPopUpYesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + "com.harry.lucidwaker");
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(AlarmSetActivity.this, "Sorry, something went wrong.", Toast.LENGTH_LONG).show();
                }

                rateNoPopUpConstraintLayout.setVisibility(View.GONE);
                if (editor != null) {
                    editor.putBoolean("Don't Show Again", true);
                    editor.commit();
                }
            }
        });
        rateNoPopUpNoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateNoPopUpConstraintLayout.setVisibility(View.GONE);
                if (editor != null) {
                    editor.putBoolean("Don't Show Again", true);
                    editor.commit();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        quitButton.performClick();
    }
}
