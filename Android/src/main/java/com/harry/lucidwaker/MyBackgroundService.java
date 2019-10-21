package com.harry.lucidwaker;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import android.util.Log;
import java.util.Calendar;
import java.util.Date;

import com.harry.lucidwaker.R;
import com.unity3d.player.UnityPlayer;

import static com.harry.lucidwaker.App.CHANNEL_ID;

public class MyBackgroundService extends Service {
    static MediaPlayer alarm, binaural;
    static Vibrator vibrator;
    static Date startTime;
    static float timer = 0.0f;
    static int ringtoneNum, prevRingtoneScreenInformation, alarmHour, alarmMin;
    static String prevVolumeChangeInformation;
    static boolean alarming = false, enteredAlarmSetScreen = false, firstAutoOff = true, firstWaitTime = true, firstBinaural = true;
    static Notification notification;

    public MyBackgroundService() {
    }

    @Override
    public void onCreate() {
        //Toast.makeText(this, "Invoke background service onCreate method.", Toast.LENGTH_LONG).show();
        Log.d("TAG", "MyBackgroundService's onCreate() began!");

        alarm = MediaPlayer.create(getApplicationContext(), R.raw.fade);
        binaural = MediaPlayer.create(getApplicationContext(), R.raw.binaural_beats);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        timer = 0.0f;
        alarming = false;
        firstAutoOff = firstWaitTime = firstBinaural = true;
        prevVolumeChangeInformation = "";

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.app_icon_round)
                .setContentTitle("Lucid Waker alarm is set!")
                .setContentText("This notification helps keep the alarm running.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "Invoke background service onStartCommand method.", Toast.LENGTH_LONG).show();
        Log.d("TAG", "MyBackgroundService's onStartCommand() began!");

        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100); // run every 0.1 sec.
                    } catch (InterruptedException e) {}
                    ManageAlarmsWithUnity(); // manage alarms with Unity
                }
            }
        };
        // start the thread
        Thread serviceThread = new Thread(r);
        serviceThread.start();
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this, "Invoke background service onDestroy method.", Toast.LENGTH_LONG).show();
        if (alarm.isPlaying()) alarm.stop();
        if (binaural.isPlaying()) binaural.stop();
        vibrator.cancel();
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    Handler messageHandler = new Handler();
    public void displayInformation(final String information) {
        Runnable doDisplayError = new Runnable() {
            public void run() {
                if (information.length() != 0)
                    Toast.makeText(getApplicationContext(), "Info: " + information, Toast.LENGTH_LONG).show();
            }
        };
        messageHandler.post(doDisplayError);
    }
    void ManageAlarmsWithUnity() {
        // retrieve information from InformToService() function of gameobject "Android Service" in Unity
        String information = MyUnityPlugin.UnitySendMessageExtension("AndroidService", "InformToService", "No message to send");
        Log.d("Tag", "Information: " + information);
        //displayInformation(information);
        /*
        Notes about information:
        "" (blank) means volume did not change and AlarmSetScreen and RingtoneScreen are not active.

        If volume changed, information will be "VolumeChange [Ringtone][Volume]"
            This volume change applies to both volume1 and volume2.
            [Ringtone] = 0 means Vibration. [Ringtone] = 1 means Fade. [Ringtone] = 2 means Invisible.
            [Ringtone] = 3 means Force. [Ringtone] = 4 means Cloud 9. [Ringtone] = 5 means Weird Dream.
            [Ringtone] = 6 means Cat. [Ringtone] = 7 means Dog.
            [Volume]'s size will be 3 digits so there may be leading zeros.

        If RingtoneScreen is active, information will be "RingtoneScreen [Ringtone][Volume]"

        If AlarmSetScreen is active, information will be "AlarmSetScreen HHMM[Type][Ringtone][Volume]"
            "HHMM" means alarm is set at HH:MM.
            [Type] = 1 means Normal. [Type] = 2 means FILD/CANWILD. [Type] = 3 means Rausis. [Type] = 4 means Chaining.
            As of now, information's length is 9.

            Additional information attached to the end if alarm type if FILD: "[AutoOff][BinauralOn]"
            Additional information attached to the end if alarm type if Rausis: "[AutoOff][WaitTime][Volume2][AutoOff2][BinauralOn]"
            Additional information attached to the end if alarm type if Chaining: "[AutoOff][WaitTime]"

            [AutoOff], [AutoOff2], and [WaitTime]'s sizes will all be 2 digits so there may be leading zeros.
            [Volume2]'s size will be 3 digits so there may be leading zeros.
            [BinauralOn] will be 1 if binaural is on and 0 if binaural is off.
        */

        boolean volumeChange = false, ringtoneScreen = false, alarmSetScreen = false;
        if (information.length() != 0) { // if some information is received
            String state = information.substring(0, information.indexOf(' '));
            information = information.substring(information.indexOf(' ') + 1);
            if (state.equals("VolumeChange"))  volumeChange = true;
            if (state.equals("RingtoneScreen"))  ringtoneScreen = true;
            if (state.equals("AlarmSetScreen"))  alarmSetScreen = true;
        }

        if (!volumeChange && !ringtoneScreen && !alarmSetScreen) {
            if (prevVolumeChangeInformation.length() != 0) { // if ringtone volume was recently changed
                try {
                    Thread.sleep(1000); // wait for 1 second as the ringtone with changed volume plays
                } catch (InterruptedException e) {}
                prevVolumeChangeInformation = ""; // reset to indicate that ringtone volume was not recently changed
            }

            // stop all sounds and vibration whatsoever
            if (alarm.isPlaying()) alarm.stop();
            if (binaural.isPlaying()) binaural.stop();
            vibrator.cancel();

            alarming = false; // reset to indicate that app is currently not alarming
            UnityPlayer.UnitySendMessage("Global Alarm Manager", "AlarmBegan", "0"); // tell Unity that app is currently not alarming

            // reset to indicate that app never did Auto Off, started Wait Time, or played Binaural Beats yet
            firstAutoOff = firstWaitTime = firstBinaural = true;
            prevRingtoneScreenInformation = 0; // reset to indicate that app did not enter RingtoneScreen
            enteredAlarmSetScreen = false; // reset to indicate that app did not enter AlarmSetScreen
            stopForeground(true); // turn off foreground notification
        }

        if (volumeChange) {
            ringtoneNum = Integer.valueOf(information.substring(0, 1));
            int volume = Integer.valueOf(information.substring(1, 4));
            if (ringtoneNum == 0) PlayVibration();
            if (!alarm.isPlaying()) PlayAlarm(volume);
            UpdateAlarmVolume(volume);
            prevVolumeChangeInformation = information;
        }

        if (ringtoneScreen && prevRingtoneScreenInformation != Integer.valueOf(information)) {
            ringtoneNum = Integer.valueOf(information.substring(0, 1));
            int volume = Integer.valueOf(information.substring(1, 4));
            if (alarm.isPlaying()) alarm.stop();
            vibrator.cancel();
            if (ringtoneNum == 0) PlayVibration();
            else PlayAlarm(volume);
            prevRingtoneScreenInformation = Integer.valueOf(information);
        }

        int currHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int currMin = Calendar.getInstance().get(Calendar.MINUTE);

        // if AlarmSetScreen is active and it is first time entering AlarmSetScreen
        if (alarmSetScreen && !enteredAlarmSetScreen) {
            alarmHour = Integer.valueOf(information.substring(0, 2));
            alarmMin = Integer.valueOf(information.substring(2, 4));
            ringtoneNum = Integer.valueOf(information.substring(5, 6));

            startForeground(1, notification); // turn on foreground notification
            enteredAlarmSetScreen = true; // indicate that app entered AlarmSetScreen
        }
        if (alarmSetScreen && alarming == false && currHour == alarmHour && currMin == alarmMin) {
            Log.d("Tag", "Alarm ring started!");
            alarming = true; // indicate that app is currently alarming
            UnityPlayer.UnitySendMessage("Global Alarm Manager", "AlarmBegan", "1"); // tell Unity that app is currently alarming
            startTime = new Date(); // indicate that startTime is current time when alarm started
            timer = 0; // reset timer

            int volume = Integer.valueOf(information.substring(6, 9));
            if (ringtoneNum == 0) PlayVibration();
            else PlayAlarm(volume);
        }
        if (alarming == true) { // if app is currently alarming
            Date currTime = new Date(); // keep updating currTime with current time
            // keep updating timer with time difference in seconds between current time and startTime when alarm started
            timer = (currTime.getTime()-startTime.getTime())/1000.0f;
            Log.d("TAG", "Timer: " + timer);

            if (information.charAt(4) == '2') { // FILD / CANWILD
                int autoOffNumber = Integer.valueOf(information.substring(9, 11));

                // if timer passed autoOffNumber in seconds
                if (timer >= autoOffNumber) {
                    if (alarm.isPlaying()) alarm.stop();
                    vibrator.cancel();
                    int binauralOn = Integer.valueOf(information.substring(11, 12));

                    // if Binaural Beats is set to play and Binaural Beats did not play yet
                    if (binauralOn == 1 && firstBinaural) {
                        Log.d("Tag", "Binaural Beats Started!");
                        int volume = Integer.valueOf(information.substring(6, 9));
                        PlayBinauralBeats(volume);
                        firstBinaural = false; // indicate that Binaural Beats started playing
                    }
                }
            }
            if (information.charAt(4) == '3') { // Rausis
                int autoOffNumber = Integer.valueOf(information.substring(9, 11));
                int waitTimeNumber = Integer.valueOf(information.substring(11, 13));
                int autoOffNumber2 = Integer.valueOf(information.substring(16, 18));
                int binauralOn = Integer.valueOf(information.substring(18, 19));

                // if timer passed autoOffNumber in seconds and Auto Off did not run yet
                if (timer >= autoOffNumber && firstAutoOff) {
                    if (alarm.isPlaying()) alarm.stop();
                    vibrator.cancel();
                    firstAutoOff = false; // indicate that Auto Off is done
                    startTime = new Date(); // update startTime with current time when Auto Off is done
                    timer = 0; // reset timer
                }
                // if timer passed waitTimeNumber in minutes and Auto Off is already done and Wait Time did not start yet
                if (timer >= waitTimeNumber * 60 && !firstAutoOff && firstWaitTime) {
                    int volume2 = Integer.valueOf(information.substring(13, 16));
                    if (ringtoneNum == 0) PlayVibration();
                    else PlayAlarm(volume2);
                    firstWaitTime = false; // indicate that Wait Time already started
                    startTime = new Date(); // update startTime with current time when Wait Time started
                    timer = 0; // reset timer
                }
                if (timer >= autoOffNumber2 && !firstAutoOff && !firstWaitTime) {
                    if (alarm.isPlaying()) alarm.stop();
                    vibrator.cancel();

                    // if Binaural Beats is set to play and Binaural Beats did not play yet
                    if (binauralOn == 1 && firstBinaural) {
                        Log.d("TAG", "Binaural Beats Started!");
                        int volume = Integer.valueOf(information.substring(6, 9));
                        PlayBinauralBeats(volume);
                        firstBinaural = false; // indicate that Binaural Beats started playing
                    }
                }
            }
            if (information.charAt(4) == '4') { // Chaining
                int autoOffNumber = Integer.valueOf(information.substring(9, 11));
                int waitTimeNumber = Integer.valueOf(information.substring(11, 13));

                // if timer passed autoOffNumber in seconds and Auto Off did not run yet
                if (timer >= autoOffNumber && firstAutoOff) {
                    if (alarm.isPlaying()) alarm.stop();
                    vibrator.cancel();
                    firstAutoOff = false; // indicate that Auto Off is done
                    firstWaitTime = true; // indicate that Wait Time did not start yet
                    startTime = new Date(); // update startTime with current time when Auto Off was done
                    timer = 0; // reset timer
                }
                // if timer passed waitTimeNumber in minutes and Auto Off is already done and Wait Time did not start yet
                if (timer >= waitTimeNumber * 60 && !firstAutoOff && firstWaitTime) {
                    int volume = Integer.valueOf(information.substring(6, 9));
                    if (ringtoneNum == 0) PlayVibration();
                    else PlayAlarm(volume);
                    firstWaitTime = false; // indicate that Wait Time already started
                    firstAutoOff = true; // indicate that Auto Off did not run yet
                    startTime = new Date(); // update startTime with current time when Wait Time started
                    timer = 0; // reset timer
                }
            }
        }
    }

    void PlayAlarm(int volume) {
        Log.d("TAG", "Play Alarm!");
        if (ringtoneNum == 1) alarm = MediaPlayer.create(getApplicationContext(), R.raw.fade);
        if (ringtoneNum == 2) alarm = MediaPlayer.create(getApplicationContext(), R.raw.invincible);
        if (ringtoneNum == 3) alarm = MediaPlayer.create(getApplicationContext(), R.raw.force);
        if (ringtoneNum == 4) alarm = MediaPlayer.create(getApplicationContext(), R.raw.cloud9);
        if (ringtoneNum == 5) alarm = MediaPlayer.create(getApplicationContext(), R.raw.weird_dream);
        if (ringtoneNum == 6) alarm = MediaPlayer.create(getApplicationContext(), R.raw.cat);
        if (ringtoneNum == 7) alarm = MediaPlayer.create(getApplicationContext(), R.raw.dog);

        alarm.setLooping(true);

        Log.d("TAG", "Volume: " + volume);
        int maxVolume = 100;
        float log1 = (float) (1 - (Math.log(maxVolume - volume) / Math.log(maxVolume)));
        alarm.setVolume(log1,log1);

        try { alarm.prepare(); } catch (Exception e) { }
        alarm.start();
    }

    void PlayBinauralBeats(int volume) {
        Log.d("TAG", "Play Binaural Beats!");
        binaural = MediaPlayer.create(getApplicationContext(), R.raw.binaural_beats);

        binaural.setLooping(true);

        Log.d("TAG", "Volume: " + volume);
        int maxVolume = 100;
        float log1 = (float) (1 - (Math.log(maxVolume - volume) / Math.log(maxVolume)));
        binaural.setVolume(log1,log1);

        try { binaural.prepare(); } catch (Exception e) { }
        binaural.start();
    }

    void PlayVibration() {
        Log.d("TAG", "Play Vibration!");

        // vibrate for max one hour in Regular alarm.
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(60*60*1000, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(24*60*1000); //deprecated in API 26
        }
    }

    void UpdateAlarmVolume(int volume) {
        int maxVolume = 100;
        float log1 = (float) (1 - (Math.log(maxVolume - volume) / Math.log(maxVolume)));
        alarm.setVolume(log1,log1);
    }
}