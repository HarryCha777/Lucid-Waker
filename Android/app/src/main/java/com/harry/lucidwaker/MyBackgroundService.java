package com.harry.lucidwaker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

public class MyBackgroundService extends Service {
    static final String CHANNEL_ID = "appChannel";
    static Notification notification;

    boolean stopThread;
    boolean firstAutoOff = true, firstWait = true;
    AudioManager audioManager;
    String prevSleepHelpName = "";
    static MediaPlayer sleepHelp, alarm, tone;
    static Vibrator vibrator;
    Date startTime;
    float timer;

    @Override
    public void onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "App Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_notif)
                .setContentTitle("Lucid Waker alarm is set!")
                .setContentText("This notification helps keep the alarm running.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
        startForeground(1, notification);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // default volume1 6 (out of 15) for Sleep Help music
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 6, 0);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100); // run every 0.1 sec.
                    } catch (InterruptedException e) {
                    }

                    if (App.currActivity.equals("AlarmSet")) {
                        handleSleepHelp();
                        handleAlarm();
                    }
                    if (stopThread) break;
                }
            }
        };

        Thread t = new Thread(r);
        t.start();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopThread = true;
        App.isAlarming = false;
        App.isSleepHelpOn = false;

        if (sleepHelp != null && sleepHelp.isPlaying()) sleepHelp.stop();
        vibrator.cancel();
        if (alarm != null && alarm.isPlaying()) alarm.stop();
        if (tone != null && tone.isPlaying()) tone.stop();
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    void handleSleepHelp() {
        if (!App.isAlarming) {
            if (App.isSleepHelpOn
                    && (sleepHelp == null || !sleepHelp.isPlaying() || !prevSleepHelpName.equals(App.sleepHelpName))) {
                if (sleepHelp != null) sleepHelp.reset();
                else sleepHelp = new MediaPlayer();

                try {
                    sleepHelp.setDataSource(this, Uri.parse("android.resource://com.harry.lucidwaker/" +
                            App.soundnameToResource(App.sleepHelpName)));
                } catch (Exception e) {
                }

                sleepHelp.setAudioStreamType(AudioManager.STREAM_ALARM);
                sleepHelp.setLooping(true);
                try { // if not prepared
                    sleepHelp.prepareAsync();
                    sleepHelp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                } catch (Exception e) { // if already prepared
                    sleepHelp.start();
                }
            }
            if (!App.isSleepHelpOn && sleepHelp != null) sleepHelp.stop();
            prevSleepHelpName = App.sleepHelpName;
        } else if (sleepHelp != null && sleepHelp.isPlaying()) sleepHelp.stop();
    }

    void handleAlarm() {
        int currDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int currHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int currMin = Calendar.getInstance().get(Calendar.MINUTE);
        int currHourAndMin = currHour * 60 + currMin;
        int setHourAndMin = App.setHour * 60 + App.setMin;

        // check if time is equal to or passed set time
        // because service might have been killed right on set time and restarted later by START_STICKY.
        if (!App.isAlarming && currDay == App.setDay && currHourAndMin >= setHourAndMin) {
            Log.d("Tag", "Alarm ring started!");
            App.isSleepHelpOn = false;
            App.isAlarming = true; // indicate that app is currently isAlarming
            startTime = new Date(); // indicate that startTime is current time when alarm started
            timer = 0; // reset timer

            if (!App.isSound1Off) startAlarm(false);
            if (!App.isVibration1Off) startVibrator(false);
        }
        if (App.isAlarming) { // if app is currently isAlarming
            Date currTime = new Date(); // keep updating currTime with current time
            // keep updating timer with time difference in seconds between current time and startTime when alarm started
            timer = (currTime.getTime() - startTime.getTime()) / 1000.0f;
            Log.d("TAG", "Timer: " + timer);

            if (App.mode == 1) { // FILD
                // if timer passed autoOffNumber in seconds
                if (timer >= App.autoOff1) {
                    if (alarm != null && alarm.isPlaying()) alarm.stop();
                    vibrator.cancel();
                    if (App.tone != 1 && (tone == null || !tone.isPlaying()))
                        startTone();
                }
            }
            if (App.mode == 2) { // Chaining
                // if timer passed autoOffNumber in seconds and Auto Off did not run yet
                if (timer >= App.autoOff1 && firstAutoOff) {
                    vibrator.cancel();
                    if (alarm.isPlaying()) alarm.stop();
                    if (App.tone != 1) startTone();

                    firstAutoOff = false; // indicate that Auto Off is done
                    firstWait = true; // indicate that Wait did not start yet
                    startTime = new Date(); // update startTime with current time when Auto Off was done
                    timer = 0; // reset timer
                }
                // if timer passed waitTimeNumber in minutes and Auto Off is already done and Wait did not start yet
                if (timer >= App.wait * 60 && !firstAutoOff && firstWait) {
                    if (tone != null && tone.isPlaying()) tone.stop();
                    if (!App.isSound1Off) startAlarm(false);
                    if (!App.isVibration1Off) startVibrator(false);

                    firstWait = false; // indicate that Wait already started
                    firstAutoOff = true; // indicate that Auto Off did not run yet
                    startTime = new Date(); // update startTime with current time when Wait started
                    timer = 0; // reset timer
                }
            }
            if (App.mode == 3) { // Rausis
                // if timer passed autoOff1 in seconds and Auto Off did not run yet
                if (timer >= App.autoOff1 && firstAutoOff) {
                    vibrator.cancel();
                    if (alarm != null && alarm.isPlaying()) alarm.stop();
                    if (App.tone != 1) startTone();

                    firstAutoOff = false; // indicate that Auto Off is done
                    startTime = new Date(); // update startTime with current time when Auto Off is done
                    timer = 0; // reset timer
                }
                // if timer passed wait in minutes and Auto Off is already done and Wait did not start yet
                if (timer >= App.wait * 60 && !firstAutoOff && firstWait) {
                    if (tone != null && tone.isPlaying()) tone.stop();
                    if (!App.isVibration2Off) startVibrator(true);
                    if (!App.isSound2Off) startAlarm(true);

                    firstWait = false; // indicate that Wait already started
                    startTime = new Date(); // update startTime with current time when Wait started
                    timer = 0; // reset timer
                }
                if (timer >= App.autoOff2 && !firstAutoOff && !firstWait) {
                    vibrator.cancel();
                    if (alarm != null && alarm.isPlaying()) alarm.stop();
                    if (App.tone != 1 && !tone.isPlaying()) startTone();
                }
            }
        }
    }

    void startVibrator(boolean secondAlarm) {
        String vibrationName;
        if (!secondAlarm) vibrationName = App.vibration1Name;
        else vibrationName = App.vibration2Name;

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

    void startAlarm(boolean secondAlarm) {
        if (!secondAlarm) {
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, App.volume1, 0);
        } else {
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, App.volume2, 0);
        }

        if (alarm != null) alarm.reset();
        else alarm = new MediaPlayer();

        try {
            if (!secondAlarm && App.isMyMusic1) {
                try {
                    alarm.setDataSource(MyBackgroundService.this, Uri.parse(App.myMusic1UriString));
                } catch (Exception e) {
                }
            } else if (secondAlarm && App.isMyMusic2) {
                try {
                    alarm.setDataSource(MyBackgroundService.this, Uri.parse(App.myMusic2UriString));
                } catch (Exception e) {
                }
            } else {
                if (!secondAlarm)
                    alarm.setDataSource(this, Uri.parse("android.resource://com.harry.lucidwaker/" +
                            App.soundnameToResource(App.sound1Name)));
                else
                    alarm.setDataSource(this, Uri.parse("android.resource://com.harry.lucidwaker/" +
                            App.soundnameToResource(App.sound2Name)));
            }
        } catch (Exception e) {
        }
        alarm.setAudioStreamType(AudioManager.STREAM_ALARM);
        alarm.setLooping(true);
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

    void startTone() {
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 6, 0); // default volume1 6 (out of 15)

        if (tone != null) tone.reset();
        else tone = new MediaPlayer();

        if (App.tone == 2) { // Isochronic
            try {
                tone.setDataSource(this, Uri.parse("android.resource://com.harry.lucidwaker/" + R.raw.isochronic_tone));
            } catch (Exception e) {
            }
        } else { // Binaural Beats
            try {
                tone.setDataSource(this, Uri.parse("android.resource://com.harry.lucidwaker/" + R.raw.binaural_beats));
            } catch (Exception e) {
            }
        }
        tone.setAudioStreamType(AudioManager.STREAM_ALARM);
        tone.setLooping(true);
        try { // if not prepared
            tone.prepareAsync();
            tone.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        } catch (Exception e) { // if already prepared
            tone.start();
        }
    }
}
