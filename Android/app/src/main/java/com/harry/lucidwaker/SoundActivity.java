package com.harry.lucidwaker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.PermissionRequest;

import java.io.File;
import java.util.ArrayList;

public class SoundActivity extends AppCompatActivity {

    String prevActivity = "Alarm";
    int prevTabSelection = -1;

    SoundPagerAdapter sectionsPagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;

    static int prevRingtonesID, ringtonesID;
    static int prevMyMusicID, myMusicID;
    boolean myMusicListed; // plan to re-list since new music may have been added or deleted in the phone.
    static boolean myMusicSelected;
    static RadioGroup ringtonesRadioGroup;
    static RadioGroup myMusicRadioGroup;

    AudioManager audioManager;
    static MediaPlayer alarm;
    ArrayList<File> myMusicList;

    void logVars() {
        Log.d("TAG", "MyMusicSelected: " + myMusicSelected + "   " +
                "PrevRingtonesID:    " + prevRingtonesID + "    RingtonesID: " + ringtonesID + "    " +
                "PrevMyMusicID:    " + prevMyMusicID + "    MyMusicID: " + myMusicID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sound");

        sectionsPagerAdapter = new SoundPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(sectionsPagerAdapter);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

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
                            if (App.currActivity.equals("Sound")) {
                                logVars();
                                if (!prevActivity.equals("Sound")) // run only once each time this activity starts
                                    doWhenEnteredActivity();

                                logVars();
                                if (prevTabSelection != tabLayout.getSelectedTabPosition()
                                        && tabLayout.getSelectedTabPosition() == 1) // run only once each time tab changed to My Music
                                    doWhenChangedToMyMusic();
                                prevTabSelection = tabLayout.getSelectedTabPosition();

                                logVars();
                                if (tabLayout.getSelectedTabPosition() == 0)
                                    doWhenInRingtones();
                                else
                                    doWhenInMyMusic();
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

    void doWhenEnteredActivity() {
        if (!App.isAlarm2) {
            ringtonesID = App.ringtones1ID;
            myMusicID = App.myMusic1ID;
            myMusicSelected = App.isMyMusic1;

            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, App.volume1, 0);
        } else {
            ringtonesID = App.ringtones2ID;
            myMusicID = App.myMusic2ID;
            myMusicSelected = App.isMyMusic2;

            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, App.volume2, 0);
        }

        ringtonesRadioGroup = findViewById(R.id.ringtonesRadioGroup);
        myMusicRadioGroup = findViewById(R.id.myMusicRadioGroup);

        prevMyMusicID = prevRingtonesID = -1; // without making prev values different, music selected as default might not play

        if (!myMusicSelected) {
            if (ringtonesID == 0) {
                // if ringtones1ID is not set, default is Punky
                RadioButton ringtoneRadioButton = findViewById(R.id.punkyRadioButton);
                ringtoneRadioButton.toggle();
                ringtonesID = ringtonesRadioGroup.getCheckedRadioButtonId();
            } else {
                // if ringtones1ID is set, toggle the ringtone
                ringtonesRadioGroup.check(ringtonesID);
            }
        } else {
            // if myMusic was selected last time, just make myMusic list, toggle the music, and go to myMusic tab
            listMyMusic();
            myMusicRadioGroup.check(myMusicID);
            tabLayout.getTabAt(1).select();
        }
    }

    void doWhenInRingtones() {
        ringtonesID = ringtonesRadioGroup.getCheckedRadioButtonId();
        if (prevRingtonesID != ringtonesID && ringtonesID != -1) {
            myMusicSelected = false;
            myMusicRadioGroup.clearCheck(); // un-toggle selected sound in My Music tab
            prevMyMusicID = myMusicID = -1;

            playSelectedSound();
        }
        prevRingtonesID = ringtonesID;
    }

    void doWhenChangedToMyMusic() {
        if (!myMusicListed) {
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            listMyMusic();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            if (response.isPermanentlyDenied()) {
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        }
    }

    void doWhenInMyMusic() {
        myMusicID = myMusicRadioGroup.getCheckedRadioButtonId();
        if (prevMyMusicID != myMusicID && myMusicID != -1) {
            myMusicSelected = true;
            ringtonesRadioGroup.clearCheck(); // un-toggle selected sound in Ringtone tab
            prevRingtonesID = ringtonesID = -1;

            playSelectedSound();
        }
        prevMyMusicID = myMusicID;
    }

    void listMyMusic() {
        if (!myMusicListed) {
            myMusicList = getMyMusicList(Environment.getExternalStorageDirectory());
            for (int i = 0; i < myMusicList.size(); i++) {
                RadioButton myMusicRadioButton = new RadioButton(this);
                myMusicRadioButton.setText(myMusicList.get(i).getName().replace(".mp3", "").replace(".wav", ""));
                myMusicRadioButton.setTextSize(26);
                myMusicRadioButton.setPadding(20, 30, 0, 30);
                myMusicRadioButton.setId(i);
                myMusicRadioGroup.addView(myMusicRadioButton);
            }
            myMusicListed = true;
        }
    }

    public ArrayList<File> getMyMusicList(File root) {
        ArrayList<File> at = new ArrayList<File>();
        File[] files = root.listFiles();
        for (File singleFile : files)
            if (singleFile.isDirectory() && !singleFile.isHidden())
                at.addAll(getMyMusicList(singleFile));
            else if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav"))
                at.add(singleFile);
        return at;
    }

    void playSelectedSound() {
        if (alarm != null) {
            if (alarm.isPlaying()) alarm.stop();
            alarm.reset();
        } else alarm = new MediaPlayer();

        if (myMusicSelected) {
            try {
                RadioButton myMusicRadioButton = myMusicRadioGroup.findViewById(myMusicID);
                int index = myMusicRadioGroup.indexOfChild(myMusicRadioButton);
                String uriString = myMusicList.get(index).toString();

                // update Uri Strings
                if (!App.isAlarm2)
                    App.myMusic1UriString = uriString;
                else
                    App.myMusic2UriString = uriString;

                Uri u = Uri.parse(uriString);
                alarm.setDataSource(this, u);
            } catch (Exception e) {
            }
        } else {
            RadioButton ringtonesRadioButton = findViewById(ringtonesID);
            String ringtoneSoundName = ringtonesRadioButton.getText().toString();
            Uri u = Uri.parse("android.resource://com.harry.lucidwaker/" + App.soundnameToResource(ringtoneSoundName));

            try {
                alarm.setDataSource(this, u);
            } catch (Exception e) {
            }
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

    // on action bar's back button pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!App.isAlarm2) {
            if (myMusicSelected) {
                RadioButton myMusicRadioButton = myMusicRadioGroup.findViewById(myMusicID);
                App.sound1Name = myMusicRadioButton.getText().toString();
            } else {
                RadioButton ringtonesRadioButton = ringtonesRadioGroup.findViewById(ringtonesID);
                App.sound1Name = ringtonesRadioButton.getText().toString();
            }

            App.ringtones1ID = ringtonesRadioGroup.getCheckedRadioButtonId();
            App.myMusic1ID = myMusicRadioGroup.getCheckedRadioButtonId();
            App.isMyMusic1 = myMusicSelected;
        } else {
            if (myMusicSelected) {
                RadioButton myMusicRadioButton = myMusicRadioGroup.findViewById(myMusicID);
                App.sound2Name = myMusicRadioButton.getText().toString();
            } else {
                RadioButton ringtonesRadioButton = ringtonesRadioGroup.findViewById(ringtonesID);
                App.sound2Name = ringtonesRadioButton.getText().toString();
            }

            App.ringtones2ID = ringtonesRadioGroup.getCheckedRadioButtonId();
            App.myMusic2ID = myMusicRadioGroup.getCheckedRadioButtonId();
            App.isMyMusic2 = myMusicSelected;
        }

        alarm.stop();
        alarm.reset();
        alarm.release();
        alarm = new MediaPlayer();
        App.currActivity = "Alarm";
        startActivity(new Intent(SoundActivity.this, AlarmActivity.class));
    }
}
