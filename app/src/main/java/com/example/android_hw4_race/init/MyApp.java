package com.example.android_hw4_race.init;

import android.app.Application;

import com.example.android_hw4_race.data.GameManager;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MySignal.init(this);
        MyImage.init(this);
        MySP.init(this);
        MyGPS.init(this);
        GameManager.init();
    }
}
