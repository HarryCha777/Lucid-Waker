package com.harry.lucidwaker;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

public class App extends Application {
    public static final String CHANNEL_ID = "appChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("TAG", "Build.VERSION.SDK_INT >= Build.VERSION_CODES.O");
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "App Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
        else {
            Log.d("TAG", "NOT!!!: Build.VERSION.SDK_INT >= Build.VERSION_CODES.O");
        }
    }
}