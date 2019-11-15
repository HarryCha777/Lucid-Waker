package com.harry.lucidwaker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class VibrationActivity extends AppCompatActivity {

    String prevActivity = "Alarm";
    int vibrationID, prevvibrationID;
    String vibrationName;
    RadioGroup vibrationRadioGroup;
    static Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibration);

        vibrationRadioGroup = findViewById(R.id.vibrationRadioGroup);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Vibration");
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

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
                            if (App.currActivity.equals("Vibration")) {
                                if (!prevActivity.equals("Vibration")) { // run only once each time this activity starts
                                    if (!App.isAlarm2) {
                                        vibrationID = App.vibration1ID;
                                        vibrationName = App.vibration1Name;
                                    } else {
                                        vibrationID = App.vibration2ID;
                                        vibrationName = App.vibration2Name;
                                    }

                                    if (vibrationID == 0) {
                                        RadioButton radioButton;
                                        if (vibrationName.equals("Basic"))
                                            radioButton = findViewById(R.id.basicRadioButton);
                                        else if (vibrationName.equals("Heartbeat"))
                                            radioButton = findViewById(R.id.heartBeatRadioButton);
                                        else if (vibrationName.equals("Telephone"))
                                            radioButton = findViewById(R.id.telephoneRadioButton);
                                        else
                                            radioButton = findViewById(R.id.constantRadioButton);
                                        radioButton.toggle();
                                        vibrationID = vibrationRadioGroup.getCheckedRadioButtonId();
                                    } else {
                                        vibrationRadioGroup.check(vibrationID);
                                    }
                                }
                                playSelectedVibration();
                            } else {
                                vibrator.cancel(); // keep cancelling since sometimes it may keep on vibrating even after leaving the activity
                            }
                            prevActivity = App.currActivity;
                        }
                    });
                }
            }
        };

        Thread t = new Thread(r);
        t.start();
    }

    void playSelectedVibration() {
        vibrationID = vibrationRadioGroup.getCheckedRadioButtonId();
        if (prevvibrationID != vibrationID) {
            RadioButton checkedRadioButton = findViewById(vibrationID);
            vibrationName = checkedRadioButton.getText().toString();

            if (vibrationName.equals("Basic")) {
                vibrator.cancel();
                vibrator.vibrate(App.basicPattern, 0);
            } else if (vibrationName.equals("Heartbeat")) {
                vibrator.cancel();
                vibrator.vibrate(App.heartbeatPattern, 0);
            } else if (vibrationName.equals("Telephone")) {
                vibrator.cancel();
                vibrator.vibrate(App.telephonePattern, 0);
            } else if (vibrationName.equals("Constant")) {
                vibrator.cancel();
                vibrator.vibrate(24 * 60 * 1000); // max 1 hour
            }
        }
        prevvibrationID = vibrationID;
    }

    // on action bar's back button pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!App.isAlarm2) {
            App.vibration1ID = vibrationRadioGroup.getCheckedRadioButtonId();
            App.vibration1Name = vibrationName;
        } else {
            App.vibration2ID = vibrationRadioGroup.getCheckedRadioButtonId();
            App.vibration2Name = vibrationName;
        }

        vibrator.cancel();
        App.currActivity = "Alarm";
        startActivity(new Intent(VibrationActivity.this, AlarmActivity.class));
    }
}
