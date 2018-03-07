package com.p4.accountsaver.ui.base;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created by averychoke on 5/3/18.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
