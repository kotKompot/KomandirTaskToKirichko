package com.kirichko.kirichkorouteplanner.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by Киричко on 27.03.2016.
 */
public class App extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        App.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return App.context;
    }
}