package com.example.android_hw4_race.init;

import android.content.Context;
import android.content.SharedPreferences;

public class MySP {
    private static MySP instance;
    private final SharedPreferences preferences;

    private MySP(Context context){
        preferences = context.getSharedPreferences("DB_FILE", Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new MySP(context);
        }
    }

    public static MySP getInstance() {
        return instance;
    }

    public void putString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public String getString(String key, String def) {
        return preferences.getString(key, def);
    }
}

