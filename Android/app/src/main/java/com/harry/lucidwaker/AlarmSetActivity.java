package com.harry.lucidwaker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AlarmSetActivity extends AppCompatActivity {

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

        modeTextView = findViewById(R.id.modeTextView);
        countdownTextView = findViewById(R.id.countdownTextView);
        alarmTimeTextView = findViewById(R.id.alarmTimeTextView);
        sleepHelpTextView = findViewById(R.id.sleepHelpTextView);
        sleepHelpSpinner = findViewById(R.id.sleepHelpSpinner);
        sleepHelpImageButton = findViewById(R.id.sleepHelpImageButton);
        quitButton = findViewById(R.id.quitButton);

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
                startActivity(new Intent(AlarmSetActivity.this, MainActivity.class));
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

    @Override
    public void onBackPressed() {
        quitButton.performClick();
    }
}
