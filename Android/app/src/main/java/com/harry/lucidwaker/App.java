package com.harry.lucidwaker;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.Calendar;

public class App extends Application {
    // Make static as many variables and methods as possible
    // because App.varName can be used, which is shorter than ((App) getApplication).varName.

    static String currActivity = "Main"; // initial activity

    static int setHour, setMin, setDay;
    static String countdownText;

    static int autoOff1, autoOff2;
    static int tone; // 1 = Silent, 2 = Isochronic, 3 = Binaural
    static int mode; // 1 = FILD, 2 = Chaining, 3 = Rausis
    static int wait;

    static int ringtones1ID, ringtones2ID;
    static int myMusic1ID, myMusic2ID;
    static int vibration1ID, vibration2ID;

    static boolean isMyMusic1, isMyMusic2;
    static String myMusic1UriString, myMusic2UriString;
    static String sound1Name, sound2Name;
    static String vibration1Name, vibration2Name;

    static boolean isVibration1Off, isVibration2Off;
    static boolean isSound1Off, isSound2Off;

    static int volume1, volume2;

    static long[] basicPattern = new long[]{0, 1000, 1000};
    static long[] heartbeatPattern = new long[]{0, 150, 100, 150, 500};
    static long[] telephonePattern = new long[]{0, 50, 50};

    static boolean isAlarm2;
    static boolean isAlarmSet;
    static boolean isAlarming;

    static String currDurationPopUp = "";
    static String currInfoPopUp = "";

    static boolean isSleepHelpOn;
    static String sleepHelpName;

    static void setCountdown() {
        int currHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int currMin = Calendar.getInstance().get(Calendar.MINUTE);

        int currTotalMin = currHour * 60 + currMin;
        int setTotalMin = setHour * 60 + setMin;

        int diffMin;
        if (currTotalMin <= setTotalMin) diffMin = setTotalMin - currTotalMin;
        else diffMin = 24 * 60 - (currTotalMin - setTotalMin);

        if (diffMin == 0) {
            countdownText = "Immediately";
        } else {
            countdownText = "In ";
            if (diffMin == 1)
                countdownText += "less than a Minute";
            if (diffMin / 60 == 1)
                countdownText += (diffMin / 60) + " Hour";
            if (diffMin / 60 > 1)
                countdownText += (diffMin / 60) + " Hours";
            if (diffMin != 1 && diffMin / 60 >= 1 && diffMin % 60 >= 1)
                countdownText += " and ";
            if (diffMin != 1 && diffMin % 60 == 1)
                countdownText += (diffMin % 60) + " Minute";
            if (diffMin % 60 > 1)
                countdownText += (diffMin % 60) + " Minutes";
        }
    }

    static int soundnameToResource(String soundName) {
        if (soundName.equals("Punky")) return R.raw.punky;
        if (soundName.equals("Happy Rock")) return R.raw.happy_rock;
        if (soundName.equals("Creative Minds")) return R.raw.creative_minds;
        if (soundName.equals("Extreme Action")) return R.raw.extreme_action;
        if (soundName.equals("Dub Step")) return R.raw.dub_step;
        if (soundName.equals("Fade")) return R.raw.fade;
        if (soundName.equals("Force")) return R.raw.force;
        if (soundName.equals("Cloud 9")) return R.raw.cloud_9;
        if (soundName.equals("Invincible")) return R.raw.invincible;
        if (soundName.equals("Weird Dream")) return R.raw.weird_dream;
        if (soundName.equals("Cat")) return R.raw.cat;
        if (soundName.equals("Dog")) return R.raw.dog;
        if (soundName.equals("Rain")) return R.raw.rain;
        if (soundName.equals("Meditation")) return R.raw.meditation;
        if (soundName.equals("White Noise")) return R.raw.white_noise;
        if (soundName.equals("Isochronic Tone")) return R.raw.isochronic_tone;
        if (soundName.equals("Binaural Beats")) return R.raw.binaural_beats;
        return -1;
    }

    void saveSettings() {
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putInt("Set Hour", setHour);
        editor.putInt("Set Min", setMin);
        editor.putInt("Auto Off 1", autoOff1);
        editor.putInt("Tone", tone);
        editor.putInt("Mode", mode);
        editor.putInt("Wait", wait);
        editor.putInt("Auto Off 2", autoOff2);
        editor.putBoolean("Is Vibration 1 Off", isVibration1Off);
        editor.putBoolean("Is Vibration 2 Off", isVibration2Off);
        editor.putString("Vibration 1 Name", vibration1Name);
        editor.putString("Vibration 2 Name", vibration2Name);
        editor.putBoolean("Is Sound 1 Off", isSound1Off);
        editor.putBoolean("Is Sound 2 Off", isSound2Off);
        editor.putString("Sound 1 Name", sound1Name);
        editor.putString("Sound 2 Name", sound2Name);
        editor.putBoolean("Is My Music 1", isMyMusic1);
        editor.putBoolean("Is My Music 2", isMyMusic2);
        editor.putString("My Music 1 Uri String", myMusic1UriString);
        editor.putString("My Music 2 Uri String 2", myMusic2UriString);
        editor.putInt("Ringtones 1 ID", ringtones1ID);
        editor.putInt("Ringtones 2 ID", ringtones2ID);
        editor.putInt("My Music 1 ID", myMusic1ID);
        editor.putInt("My Music 2 ID", myMusic2ID);
        editor.putInt("Volume 1", volume1);
        editor.putInt("Volume 2", volume2);
        editor.putString("Sleep Help Name", sleepHelpName);
        editor.apply();
    }

    static boolean settingsRetrieved;

    void retrieveSettings() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        setHour = prefs.getInt("Set Hour", 6); // 2nd string is default value
        setMin = prefs.getInt("Set Min", 30);
        autoOff1 = prefs.getInt("Auto Off 1", 15);
        tone = prefs.getInt("Tone", 1);
        mode = prefs.getInt("Mode", 1);
        wait = prefs.getInt("Wait", 30);
        autoOff2 = prefs.getInt("Auto Off 2", 10);
        isVibration1Off = prefs.getBoolean("Is Vibration 1 Off", false);
        isVibration2Off = prefs.getBoolean("Is Vibration 2 Off", false);
        vibration1Name = prefs.getString("Vibration 1 Name", "Telephone");
        vibration2Name = prefs.getString("Vibration 2 Name", "Heartbeat");
        isSound1Off = prefs.getBoolean("Is Sound 1 Off", false);
        isSound2Off = prefs.getBoolean("Is Sound 2 Off", false);
        sound1Name = prefs.getString("Sound 1 Name", "Punky");
        sound2Name = prefs.getString("Sound 2 Name", "Punky");
        isMyMusic1 = prefs.getBoolean("Is My Music 1", false);
        isMyMusic2 = prefs.getBoolean("Is My Music 2", false);
        myMusic1UriString = prefs.getString("My Music 1 Uri String", "");
        myMusic2UriString = prefs.getString("My Music 2 Uri String", "");
        ringtones1ID = prefs.getInt("Ringtones 1 ID", 0);
        ringtones2ID = prefs.getInt("Ringtones 2 ID", 0);
        myMusic1ID = prefs.getInt("My Music 1 ID", 0);
        myMusic2ID = prefs.getInt("My Music 2 ID", 0);
        volume1 = prefs.getInt("Volume 1", 14);
        volume2 = prefs.getInt("Volume 2", 10);
        sleepHelpName = prefs.getString("Sleep Help Name", "Rain");
    }
}
