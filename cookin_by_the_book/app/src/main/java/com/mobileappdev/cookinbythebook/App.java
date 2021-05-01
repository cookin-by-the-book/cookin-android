package com.mobileappdev.cookinbythebook;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class App extends Application {
    public Context context;
    public SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        this.getSharedPreferences("AppGlobals", 0);
        context = getApplicationContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
}
