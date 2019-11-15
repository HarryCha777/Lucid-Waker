package com.harry.lucidwaker;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class AlarmActivity extends AppCompatActivity {

    String prevActivity = "Main";
    String vibrationName, soundName;
    boolean vibrationOff, soundOff;
    int volume;
    Button vibrationButton, soundButton;
    TextView vibrationNameTextView, soundNameTextView;
    Switch vibrationSwitch, soundSwitch;
    static SeekBar volumeSeekBar;
    ConstraintLayout volumeConstraintLayout;
    MediaPlayer alarm;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        vibrationButton = findViewById(R.id.vibrationButton);
        soundButton = findViewById(R.id.soundButton);
        volumeSeekBar = findViewById(R.id.volumeSeekBar);
        vibrationNameTextView = findViewById(R.id.vibrationNameTextView);
        soundNameTextView = findViewById(R.id.soundNameTextView);
        vibrationSwitch = findViewById(R.id.vibrationSwitch);
        soundSwitch = findViewById(R.id.soundSwitch);
        volumeConstraintLayout = findViewById(R.id.volumeConstraintLayout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Alarm");

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
                            if (App.currActivity.equals("Alarm")) {
                                if (!prevActivity.equals("Alarm")) { // run only once each time this activity starts
                                    getValues();
                                    handleVibrationOff();
                                    handleSoundOff();

                                    audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                                    volumeSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM));
                                    volumeSeekBar.setProgress(volume);

                                    alarm = new MediaPlayer();
                                    alarm.setAudioStreamType(AudioManager.STREAM_ALARM);
                                    alarm.setLooping(true);

                                    if (!App.isAlarm2 && App.isMyMusic1) {
                                        try {
                                            alarm.setDataSource(AlarmActivity.this, Uri.parse(App.myMusic1UriString));
                                        } catch (Exception e) {
                                        }
                                    } else if (App.isAlarm2 && App.isMyMusic2) {
                                        try {
                                            alarm.setDataSource(AlarmActivity.this, Uri.parse(App.myMusic2UriString));
                                        } catch (Exception e) {
                                        }
                                    } else {
                                        try {
                                            alarm.setDataSource(AlarmActivity.this, Uri.parse("android.resource://com.harry.lucidwaker/" +
                                                    App.soundnameToResource(soundName)));
                                        } catch (Exception e) {
                                        }
                                    }
                                }

                                vibrationOff = !vibrationSwitch.isChecked();
                                soundOff = !soundSwitch.isChecked();
                                handleVibrationOff();
                                handleSoundOff();
                                volume = volumeSeekBar.getProgress();
                            }
                            prevActivity = App.currActivity;
                        }
                    });
                }
            }
        };

        Thread t = new Thread(r);
        t.start();

        vibrationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!vibrationOff && soundOff) { // only one switch at max can be turned off
                    Toast.makeText(AlarmActivity.this, "Cannot turn off both vibration and sound.", Toast.LENGTH_SHORT).show();
                    vibrationSwitch.toggle();
                }
            }
        });
        soundSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vibrationOff && !soundOff) {
                    Toast.makeText(AlarmActivity.this, "Cannot turn off both vibration and sound.", Toast.LENGTH_SHORT).show();
                    soundSwitch.toggle();
                }
            }
        });
        vibrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateValues();
                App.currActivity = "Vibration";
                startActivity(new Intent(AlarmActivity.this, VibrationActivity.class));
            }
        });
        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateValues();
                App.currActivity = "Sound";
                startActivity(new Intent(AlarmActivity.this, SoundActivity.class));
            }
        });
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        alarm.stop();
                    }
                };
                countDownTimer.start(); // stop playing after one second
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                try { // if not prepared
                    alarm.prepareAsync();
                    alarm.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                } catch (Exception e) { // if already prepared
                    alarm.start();
                }
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (audioManager != null) {
                    volumeSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM));
                    audioManager.setStreamVolume(AudioManager.STREAM_ALARM, progress, 0);
                }
            }
        });
    }

    void handleVibrationOff() {
        if (vibrationOff == true) {
            vibrationNameTextView.setText("OFF");
            vibrationSwitch.setChecked(false);
            vibrationButton.setEnabled(false);
        } else {
            vibrationNameTextView.setText(vibrationName);
            vibrationSwitch.setChecked(true);
            vibrationButton.setEnabled(true);
        }
    }

    void handleSoundOff() {
        if (soundOff == true) {
            volumeConstraintLayout.setVisibility(View.GONE);
            soundNameTextView.setText("OFF");
            soundSwitch.setChecked(false);
            soundButton.setEnabled(false);
        } else {
            volumeConstraintLayout.setVisibility(View.VISIBLE);
            soundSwitch.setChecked(true);
            soundButton.setEnabled(true);

            // whether isMyMusic is on or off, shorten to max 10 char.s if longer than 11 char.s
            String shortSoundName = soundName;
            if (shortSoundName.length() > 11)
                shortSoundName = shortSoundName.substring(0, 10) + "...";
            soundNameTextView.setText(shortSoundName);
        }
    }

    // on action bar's back button pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        updateValues();
        App.isAlarm2 = false;
        App.currActivity = "Main";
        startActivity(new Intent(AlarmActivity.this, MainActivity.class));
    }

    void getValues() {
        if (!App.isAlarm2) {
            vibrationName = App.vibration1Name;
            vibrationOff = App.isVibration1Off;
            soundName = App.sound1Name;
            soundOff = App.isSound1Off;
            volume = App.volume1;
        } else {
            vibrationName = App.vibration2Name;
            vibrationOff = App.isVibration2Off;
            soundName = App.sound2Name;
            soundOff = App.isSound2Off;
            volume = App.volume2;
        }
    }

    void updateValues() {
        if (!App.isAlarm2) {
            App.vibration1Name = vibrationName;
            App.isVibration1Off = vibrationOff;
            App.sound1Name = soundName;
            App.isSound1Off = soundOff;
            App.volume1 = volume;
        } else {
            App.vibration2Name = vibrationName;
            App.isVibration2Off = vibrationOff;
            App.sound2Name = soundName;
            App.isSound2Off = soundOff;
            App.volume2 = volume;
        }
    }
}
