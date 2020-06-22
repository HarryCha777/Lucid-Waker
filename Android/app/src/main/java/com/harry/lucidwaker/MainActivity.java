package com.harry.lucidwaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    // navigation menu
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    String prevActivity = "";
    TextView countdownTextView, waitTextView,
            durationPopUpTitleTextView, durationPopUpUnitTextView, durationPopUpCancelTextView, durationPopUpSaveTextView,
            infoPopUpTitleTextView, infoPopUpMessageTextView, infoPopUpDoneTextView;
    TextView alarmNameTextView, autoOffNameTextView, waitNameTextView, alarm2NameTextView, autoOff2NameTextView;
    static TimePicker timePicker;
    NumberPicker durationPopUpNumberPicker;
    Button alarmButton, alarm2Button, autoOffButton, autoOff2Button, waitButton,
            silentButton, isochronicButton, binauralButton, fildButton, chainingButton, rausisButton, setButton;
    ImageButton autoOffInfoImageButton, toneInfoImageButton, modeInfoImageButton, waitInfoImageButton,
            alarm2InfoImageButton, autoOff2InfoImageButton;
    ImageView durationPopUpIconImageView, infoPopUpIconImageView;
    ConstraintLayout waitConstraintLayout, alarm2ConstraintLayout, autoOff2ConstraintLayout,
            durationPopUpConstraintLayout, infoPopUpConstraintLayout;
    View durationPopUpBackgroundView, infoPopUpBackgroundView;
    ScrollView mainScrollView, infoPopUpMessageScrollView;

    private AppUpdateManager appUpdateManager;

    // check and install any updates in onResume
    @Override
    protected void onResume() {
        super.onResume();

        checkUpdateOnResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainScrollView = findViewById(R.id.mainScrollView);
        countdownTextView = findViewById(R.id.countdownTextView);
        alarmNameTextView = findViewById(R.id.alarmNameTextView);
        autoOffNameTextView = findViewById(R.id.autoOffNameTextView);
        waitNameTextView = findViewById(R.id.waitNameTextView);
        alarm2NameTextView = findViewById(R.id.alarm2NameTextView);
        autoOff2NameTextView = findViewById(R.id.autoOff2NameTextView);
        timePicker = findViewById(R.id.timePicker);
        alarmButton = findViewById(R.id.alarmButton);
        alarm2Button = findViewById(R.id.alarm2Button);
        autoOffButton = findViewById(R.id.autoOffButton);
        autoOffInfoImageButton = findViewById(R.id.autoOffInfoImageButton);
        toneInfoImageButton = findViewById(R.id.toneInfoImageButton);
        modeInfoImageButton = findViewById(R.id.modeInfoImageButton);
        waitInfoImageButton = findViewById(R.id.waitInfoImageButton);
        alarm2InfoImageButton = findViewById(R.id.alarm2InfoImageButton);
        autoOff2InfoImageButton = findViewById(R.id.autoOff2InfoImageButton);
        autoOff2Button = findViewById(R.id.autoOff2Button);
        waitButton = findViewById(R.id.waitButton);
        silentButton = findViewById(R.id.silentButton);
        isochronicButton = findViewById(R.id.isochronicButton);
        binauralButton = findViewById(R.id.binauralButton);
        fildButton = findViewById(R.id.fildButton);
        chainingButton = findViewById(R.id.chainingButton);
        rausisButton = findViewById(R.id.rausisButton);
        waitConstraintLayout = findViewById(R.id.waitConstraintLayout);
        alarm2ConstraintLayout = findViewById(R.id.alarm2ConstraintLayout);
        autoOff2ConstraintLayout = findViewById(R.id.autoOff2ConstraintLayout);
        waitTextView = findViewById(R.id.waitTextView);
        setButton = findViewById(R.id.setButton);

        Button transparentOutsideDurationPopUpTouchTopButton = findViewById(R.id.transparentOutsideDurationPopUpTouchTopButton);
        Button transparentOutsideDurationPopUpTouchBottomButton = findViewById(R.id.transparentOutsideDurationPopUpTouchBottomButton);
        Button transparentOutsideDurationPopUpTouchLeftButton = findViewById(R.id.transparentOutsideDurationPopUpTouchLeftButton);
        Button transparentOutsideDurationPopUpTouchRightButton = findViewById(R.id.transparentOutsideDurationPopUpTouchRightButton);
        durationPopUpBackgroundView = findViewById(R.id.durationPopUpBackgroundView);
        durationPopUpIconImageView = findViewById(R.id.durationPopUpIconImageView);
        durationPopUpTitleTextView = findViewById(R.id.durationPopUpTitleTextView);
        durationPopUpNumberPicker = findViewById(R.id.durationPopUpNumberPicker);
        durationPopUpUnitTextView = findViewById(R.id.durationPopUpUnitTextView);
        durationPopUpCancelTextView = findViewById(R.id.durationPopUpCancelTextView);
        durationPopUpSaveTextView = findViewById(R.id.durationPopUpSaveTextView);
        durationPopUpConstraintLayout = findViewById(R.id.durationPopUpConstraintLayout);

        Button transparentOutsideInfoPopUpTouchTopButton = findViewById(R.id.transparentOutsideInfoPopUpTouchTopButton);
        Button transparentOutsideInfoPopUpTouchBottomButton = findViewById(R.id.transparentOutsideInfoPopUpTouchBottomButton);
        Button transparentOutsideInfoPopUpTouchLeftButton = findViewById(R.id.transparentOutsideInfoPopUpTouchLeftButton);
        Button transparentOutsideInfoPopUpTouchRightButton = findViewById(R.id.transparentOutsideInfoPopUpTouchRightButton);
        infoPopUpBackgroundView = findViewById(R.id.infoPopUpBackgroundView);
        infoPopUpIconImageView = findViewById(R.id.infoPopUpIconImageView);
        infoPopUpTitleTextView = findViewById(R.id.infoPopUpTitleTextView);
        infoPopUpMessageScrollView = findViewById(R.id.infoPopUpMessageScrollView);
        infoPopUpMessageTextView = findViewById(R.id.infoPopUpMessage);
        infoPopUpDoneTextView = findViewById(R.id.infoPopUpDoneTextView);
        infoPopUpConstraintLayout = findViewById(R.id.infoPopUpConstraintLayout);

        checkUpdateOnCreate();

        if (!App.settingsRetrieved) {
            ((App) getApplication()).retrieveSettings();
            App.settingsRetrieved = true;

            // if app is new or updated, reset shared preferences.
            int appVersionCode = -1;
            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                appVersionCode = pInfo.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
            }
            if (App.versionCode != appVersionCode) {
                clearAppData();
                SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
                prefs.edit().clear().commit();
                ((App) getApplication()).retrieveSettings();

                App.versionCode = appVersionCode;
                ((App) getApplication()).saveSettings();
            }
        }

        setUpNavMenu();
        durationPopUpNumberPicker.setMinValue(1);
        durationPopUpNumberPicker.setMaxValue(100);
        durationPopUpConstraintLayout.setVisibility(View.GONE);
        infoPopUpConstraintLayout.setVisibility(View.GONE);

        // if an alarm is set, app is swiped off, and app is reopened, go to AlarmSet screen
        if (((App) this.getApplication()).isAlarmSet) {
            App.currActivity = "AlarmSet";
            startActivity(new Intent(MainActivity.this, AlarmSetActivity.class));
        }

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
                            if (App.currActivity.equals("Main")) {
                                if (!prevActivity.equals("Main")) { // run only once each time this activity starts
                                    restoreTimePicker();
                                }

                                // keep setting tone and mode. if not, after app is exited and returned, they will all be turned off.
                                setTone();
                                setMode();
                                setNames();
                                handleMoreButtons();
                                handleCountdown();
                                ((App) getApplication()).saveSettings();
                            }
                            prevActivity = App.currActivity;
                        }
                    });
                }
            }
        };

        final Thread t = new Thread(r);
        t.start();

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // cancel any pop-up that might be open
                durationPopUpCancelTextView.performClick();
                infoPopUpDoneTextView.performClick();
            }
        });
        silentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durationPopUpCancelTextView.performClick();
                infoPopUpDoneTextView.performClick();
                App.tone = 1;
            }
        });
        isochronicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durationPopUpCancelTextView.performClick();
                infoPopUpDoneTextView.performClick();
                App.tone = 2;
            }
        });
        binauralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durationPopUpCancelTextView.performClick();
                infoPopUpDoneTextView.performClick();
                App.tone = 3;
            }
        });
        fildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durationPopUpCancelTextView.performClick();
                infoPopUpDoneTextView.performClick();
                App.mode = 1;
            }
        });
        chainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durationPopUpCancelTextView.performClick();
                infoPopUpDoneTextView.performClick();
                App.mode = 2;
            }
        });
        rausisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durationPopUpCancelTextView.performClick();
                infoPopUpDoneTextView.performClick();
                App.mode = 3;
            }
        });
        autoOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoPopUpDoneTextView.performClick();
                enableDisableViewGroup(mainScrollView, false);
                durationPopUpConstraintLayout.setVisibility(View.VISIBLE);
                durationPopUpIconImageView.setBackgroundResource(R.drawable.auto_off);
                durationPopUpTitleTextView.setText("Auto-Off Duration");
                durationPopUpNumberPicker.setValue(App.autoOff1);
                durationPopUpUnitTextView.setText("Sec");
                App.currDurationPopUp = "AutoOff";
            }
        });
        autoOff2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoPopUpDoneTextView.performClick();
                enableDisableViewGroup(mainScrollView, false);
                durationPopUpConstraintLayout.setVisibility(View.VISIBLE);
                durationPopUpIconImageView.setBackgroundResource(R.drawable.auto_off);
                durationPopUpTitleTextView.setText("2nd Auto-Off Duration");
                durationPopUpNumberPicker.setValue(App.autoOff2);
                durationPopUpUnitTextView.setText("Sec");
                App.currDurationPopUp = "AutoOff2";
            }
        });
        waitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoPopUpDoneTextView.performClick();
                enableDisableViewGroup(mainScrollView, false);
                durationPopUpConstraintLayout.setVisibility(View.VISIBLE);
                durationPopUpIconImageView.setBackgroundResource(R.drawable.wait);
                if (App.mode == 2) { // chaining
                    durationPopUpTitleTextView.setText("Chaining Wait Duration");
                } else { // rausis
                    durationPopUpTitleTextView.setText("Rausis Wait Duration");
                }
                durationPopUpNumberPicker.setValue(App.wait);
                durationPopUpUnitTextView.setText("Min");
                App.currDurationPopUp = "Wait";
            }
        });
        transparentOutsideDurationPopUpTouchBottomButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                durationPopUpCancelTextView.performClick();
                return true;
            }
        });
        transparentOutsideDurationPopUpTouchTopButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                durationPopUpCancelTextView.performClick();
                return true;
            }
        });
        transparentOutsideDurationPopUpTouchLeftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                durationPopUpCancelTextView.performClick();
                return true;
            }
        });
        transparentOutsideDurationPopUpTouchRightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                durationPopUpCancelTextView.performClick();
                return true;
            }
        });
        durationPopUpCancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableDisableViewGroup(mainScrollView, true);
                durationPopUpConstraintLayout.setVisibility(View.GONE);
                App.currDurationPopUp = "";
            }
        });
        durationPopUpSaveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.currDurationPopUp.equals("AutoOff"))
                    App.autoOff1 = durationPopUpNumberPicker.getValue();
                if (App.currDurationPopUp.equals("AutoOff2"))
                    App.autoOff2 = durationPopUpNumberPicker.getValue();
                if (App.currDurationPopUp.equals("Wait"))
                    App.wait = durationPopUpNumberPicker.getValue();
                App.currDurationPopUp = "";
                enableDisableViewGroup(mainScrollView, true);
                durationPopUpConstraintLayout.setVisibility(View.GONE);
            }
        });
        autoOffInfoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durationPopUpCancelTextView.performClick();
                enableDisableViewGroup(mainScrollView, false);
                infoPopUpConstraintLayout.setVisibility(View.VISIBLE);
                infoPopUpIconImageView.setBackgroundResource(R.drawable.auto_off);
                infoPopUpTitleTextView.setText("Auto-Off Info");
                String message = "This is the number of seconds the alarm will ring.\n\n" +
                        "The alarm will then turn itself off automatically.\n\n" +
                        "Since you don't need to move after waking up, " +
                        "your relaxed body will be in the ideal condition for lucid dreaming.";
                infoPopUpMessageTextView.setText(message);
                infoPopUpMessageScrollView.smoothScrollTo(0, 0);
                App.currInfoPopUp = "AutoOff";
            }
        });
        toneInfoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durationPopUpCancelTextView.performClick();
                enableDisableViewGroup(mainScrollView, false);
                infoPopUpConstraintLayout.setVisibility(View.VISIBLE);
                infoPopUpIconImageView.setVisibility(View.GONE);
                infoPopUpTitleTextView.setText("Tone Info");
                String message = "The app will play the selected tone automatically after the first alarm goes off.\n\n" +
                        "The app's isochronic tone emits theta sound waves at regular interval to help you relax.\n\n" +
                        "The app's binaural beats play two tones in different frequencies to help your brain enter meditative state.";
                infoPopUpMessageTextView.setText(message);
                infoPopUpMessageScrollView.smoothScrollTo(0, 0);
                App.currInfoPopUp = "Tone";
            }
        });
        modeInfoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durationPopUpCancelTextView.performClick();
                enableDisableViewGroup(mainScrollView, false);
                infoPopUpConstraintLayout.setVisibility(View.VISIBLE);
                infoPopUpIconImageView.setVisibility(View.GONE);
                infoPopUpTitleTextView.setText("Mode Info");
                String message = "The app will present additional options depending on the selected mode.\n\n" +
                        "FILD is an acronym for Finger Induced Lucid Dreaming. " +
                        "To execute the FILD technique, set an alarm to ring after 5-6 hours of sleep. " +
                        "When the alarm rings, wake up but stay still on bed as the alarm turns itself off automatically. " +
                        "Then imagine slightly but continuously moving your index and middle fingers alternately. " +
                        "This will help you drift back to sleep consciously.\n\n" +
                        "Chaining mode automatically loops the FILD mode with specified Wait minutes in-between each alarm. " +
                        "This mode gives you multiple chances to attempt FILD and remember dreams in only one night. " +
                        "This mode will repeat forever until you quit the alarm.\n\n" +
                        "Rausis is a lucid dreaming technique developed by a French-speaking researcher named Jean Rausis. " +
                        "First, set an alarm to wake you up after 5-6 hours of sleep. " +
                        "After the alarm turns itself off, simply go back to sleep. " +
                        "After specified Wait minutes, second alarm will ring, which serves as a signal. " +
                        "As you hear this in your dream, stay asleep and become lucid as the second alarm turns itself off.\n\n" +
                        "I encourage learning more about FILD and Rausis techniques online if you wish to understand them further.\n" +
                        "Click this link to read about FILD in Lucid Dream Society: luciddreamsociety.com/lucid-dream-technique\n" +
                        "Click this link to read about CANWILD, a technique analogous to FILD, in DreamViews: dreamviews.com/wake-initiated-lucid-dreams-wild/87710-%2A%2Acrazyinsanes-wild-tutorial-%2A%2A.html\n" +
                        "Click this link to read about Rausis in Lucid Dreaming subreddit: reddit.com/r/LucidDreaming/comments/53mgi9/rausis_new_method_awesome_instant_results_lucid";
                infoPopUpMessageTextView.setText(message);
                infoPopUpMessageScrollView.smoothScrollTo(0, 0);
                App.currInfoPopUp = "Mode";
            }
        });
        waitInfoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durationPopUpCancelTextView.performClick();
                enableDisableViewGroup(mainScrollView, false);
                infoPopUpConstraintLayout.setVisibility(View.VISIBLE);
                infoPopUpIconImageView.setBackgroundResource(R.drawable.wait);
                if (App.mode == 2) { // chaining
                    infoPopUpTitleTextView.setText("Chaining Wait Info");
                    String message = "This is the number of minutes the app will stay silent " +
                            "between the end of previous alarm and start of next alarm.\n\n" +
                            "In Chaining mode, it is recommended to give yourself enough time to experience a dream each interval.";
                    infoPopUpMessageTextView.setText(message);
                } else { // rausis
                    infoPopUpTitleTextView.setText("Rausis Wait Info");
                    String message = "This is the number of minutes the app will stay silent " +
                            "between the end of 1st alarm and start of 2nd alarm.\n\n" +
                            "In Rausis mode, you need to set it to wait for a time period " +
                            "long enough for you to be asleep again yet short enough not to lose your consciousness.";
                    infoPopUpMessageTextView.setText(message);
                }
                infoPopUpMessageScrollView.smoothScrollTo(0, 0);
                App.currInfoPopUp = "Wait";
            }
        });
        alarm2InfoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durationPopUpCancelTextView.performClick();
                enableDisableViewGroup(mainScrollView, false);
                infoPopUpConstraintLayout.setVisibility(View.VISIBLE);
                infoPopUpIconImageView.setBackgroundResource(R.drawable.alarm);
                infoPopUpTitleTextView.setText("Alarm 2 Info");
                String message = "This is the second alarm used in Rausis.\n\n" +
                        "This alarm will ring after specified Wait time following the first alarm.\n\n" +
                        "You need to adjust the alarm disruptive enough for you to notice it in your dream " +
                        "yet calm enough not to wake yourself up.";
                infoPopUpMessageTextView.setText(message);
                infoPopUpMessageScrollView.smoothScrollTo(0, 0);
                App.currInfoPopUp = "Alarm2";
            }
        });
        autoOff2InfoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durationPopUpCancelTextView.performClick();
                enableDisableViewGroup(mainScrollView, false);
                infoPopUpConstraintLayout.setVisibility(View.VISIBLE);
                infoPopUpIconImageView.setBackgroundResource(R.drawable.auto_off);
                infoPopUpTitleTextView.setText("Auto-Off 2 Info");
                String message = "This is the number of seconds the second alarm will ring.\n\n" +
                        "The alarm will then turn itself off automatically.\n\n" +
                        "You need to set it to ring for a time period long enough to cue yourself in your dream " +
                        "yet short enough not to wake yourself up.";
                infoPopUpMessageTextView.setText(message);
                infoPopUpMessageScrollView.smoothScrollTo(0, 0);
                App.currInfoPopUp = "AutoOff2";
            }
        });
        transparentOutsideInfoPopUpTouchBottomButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                infoPopUpDoneTextView.performClick();
                return true;
            }
        });
        transparentOutsideInfoPopUpTouchTopButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                infoPopUpDoneTextView.performClick();
                return true;
            }
        });
        transparentOutsideInfoPopUpTouchLeftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                infoPopUpDoneTextView.performClick();
                return true;
            }
        });
        transparentOutsideInfoPopUpTouchRightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                infoPopUpDoneTextView.performClick();
                return true;
            }
        });
        infoPopUpDoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableDisableViewGroup(mainScrollView, true);
                infoPopUpIconImageView.setVisibility(View.VISIBLE);
                infoPopUpConstraintLayout.setVisibility(View.GONE);
                App.currInfoPopUp = "";
            }
        });
        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.currActivity = "Alarm";
                startActivity(new Intent(MainActivity.this, AlarmActivity.class));
            }
        });
        alarm2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.isAlarm2 = true;
                App.currActivity = "Alarm";
                startActivity(new Intent(MainActivity.this, AlarmActivity.class));
            }
        });
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.isAlarmSet = true; // this determines if alarm is set or not if the app is swiped off and then reopened
                App.currActivity = "AlarmSet";
                startActivity(new Intent(MainActivity.this, AlarmSetActivity.class));
            }
        });
    }

    void setUpNavMenu() {
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.about:
                        App.currActivity = "About";
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        break;
                    case R.id.faq:
                        App.currActivity = "FAQ";
                        startActivity(new Intent(MainActivity.this, FaqActivity.class));
                        break;
                    case R.id.resources:
                        App.currActivity = "Resources";
                        startActivity(new Intent(MainActivity.this, ResourcesActivity.class));
                        break;
                    case R.id.credits:
                        App.currActivity = "Credits";
                        startActivity(new Intent(MainActivity.this, CreditsActivity.class));
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
    }

    // when navigation menu is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void setTone() {
        if (App.tone == 1) {
            silentButton.setPressed(true);
            isochronicButton.setPressed(false);
            binauralButton.setPressed(false);
        }
        if (App.tone == 2) {
            silentButton.setPressed(false);
            isochronicButton.setPressed(true);
            binauralButton.setPressed(false);
        }
        if (App.tone == 3) {
            silentButton.setPressed(false);
            isochronicButton.setPressed(false);
            binauralButton.setPressed(true);
        }
    }

    void setMode() {
        if (App.mode == 1) {
            fildButton.setPressed(true);
            chainingButton.setPressed(false);
            rausisButton.setPressed(false);
        }
        if (App.mode == 2) {
            fildButton.setPressed(false);
            chainingButton.setPressed(true);
            rausisButton.setPressed(false);
        }
        if (App.mode == 3) {
            fildButton.setPressed(false);
            chainingButton.setPressed(false);
            rausisButton.setPressed(true);
        }
    }

    void setNames() {
        if (!App.isSound1Off) {
            // if isMyMusic1, shorten to max 15 char.s if longer than 16 char.s
            String shortSoundName = App.sound1Name;
            if (App.isMyMusic1 && shortSoundName.length() > 16)
                shortSoundName = shortSoundName.substring(0, 15) + "...";
            alarmNameTextView.setText(shortSoundName);
        } else {
            alarmNameTextView.setText(App.vibration1Name);
        }
        autoOffNameTextView.setText(App.autoOff1 + " Sec");
        waitNameTextView.setText(App.wait + " Min");
        if (!App.isSound2Off) {
            // if isMyMusic1, shorten to max 15 char.s if longer than 16 char.s
            String shortSound2Name = App.sound2Name;
            if (App.isMyMusic2 && shortSound2Name.length() > 16)
                shortSound2Name = shortSound2Name.substring(0, 15) + "...";
            alarm2NameTextView.setText(shortSound2Name);
        } else {
            alarm2NameTextView.setText(App.vibration2Name);
        }
        autoOff2NameTextView.setText(App.autoOff2 + " Sec");
    }

    void handleMoreButtons() {
        if (App.mode == 1) {
            waitConstraintLayout.setVisibility(View.GONE);
            alarm2ConstraintLayout.setVisibility(View.GONE);
            autoOff2ConstraintLayout.setVisibility(View.GONE);
        }
        if (App.mode == 2) {
            waitConstraintLayout.setVisibility(View.VISIBLE);
            alarm2ConstraintLayout.setVisibility(View.GONE);
            autoOff2ConstraintLayout.setVisibility(View.GONE);

            waitTextView.setText("Chaining Wait");
        }
        if (App.mode == 3) {
            waitConstraintLayout.setVisibility(View.VISIBLE);
            alarm2ConstraintLayout.setVisibility(View.VISIBLE);
            autoOff2ConstraintLayout.setVisibility(View.VISIBLE);

            waitTextView.setText("Rausis Wait");
        }
    }

    void handleCountdown() {
        setAlarmTime();
        App.setCountdown();
        countdownTextView.setText(App.countdownText);
    }

    void setAlarmTime() {
        int setHour, setMin;
        if (Build.VERSION.SDK_INT < 23) {
            setHour = timePicker.getCurrentHour();
            setMin = timePicker.getCurrentMinute();
        } else {
            setHour = timePicker.getHour();
            setMin = timePicker.getMinute();
        }
        App.setHour = setHour;
        App.setMin = setMin;
    }

    void restoreTimePicker() {
        int setHour = App.setHour;
        int setMin = App.setMin;

        if (Build.VERSION.SDK_INT < 23) {
            timePicker.setCurrentHour(setHour);
            timePicker.setCurrentMinute(setMin);
        } else {
            timePicker.setHour(setHour);
            timePicker.setMinute(setMin);
        }
    }

    void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);
            if (view instanceof ViewGroup) {
                enableDisableViewGroup((ViewGroup) view, enabled);
            }
        }
    }

    void checkUpdateOnCreate() {
        // check and install any updates
        // see Michael Dougan's answer in https://stackoverflow.com/questions/55939853/how-to-work-with-androids-in-app-update-api
        appUpdateManager = AppUpdateManagerFactory.create(this);
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            // Checks that the platform will allow the specified type of update.
                            if ((appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
                                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                                // Request the update.
                                try {
                                    appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            AppUpdateType.IMMEDIATE,
                                            this,
                                            RESULT_OK);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
    }

    void checkUpdateOnResume() {
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                try {
                                    appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            AppUpdateType.IMMEDIATE,
                                            this,
                                            RESULT_OK);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
    }

    public void clearAppData() {
        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {
            String[] fileNames = applicationDirectory.list();
            for (String fileName : fileNames) {
                if (!fileName.equals("lib")) {
                    deleteFile(new File(applicationDirectory, fileName));
                }
            }
        }
    }

    public static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }

        return deletedAll;
    }

    @Override
    public void onBackPressed() {
        // if pop-up is present, simply cancel it
        if (App.currDurationPopUp.length() != 0
                || App.currInfoPopUp.length() != 0) {
            durationPopUpCancelTextView.performClick();
            infoPopUpDoneTextView.performClick();
            return;
        }

        // if API level > 15, exit app
        if (Build.VERSION.SDK_INT > 15) {
            this.finishAffinity();
            System.exit(0);
        }
        // if API level is 15, since finishAffinity() cannot be used, do nothing.
    }
}
