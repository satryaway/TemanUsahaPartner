package com.samstudio.temanusahapartner;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.samstudio.temanusahapartner.util.CommonConstants;

/**
 * Created by satryaway on 10/17/2015.
 * Singleton for application
 */
public class TemanUsahaApplication extends Application {
    private static TemanUsahaApplication instance;
    private SharedPreferences preferences;

    public synchronized static TemanUsahaApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        preferences = getSharedPreferences(CommonConstants.TU_APP, Context.MODE_PRIVATE);
    }

    public SharedPreferences getSharedPreferences(){
        return preferences;
    }

    public boolean isCustomer() {
        return getSharedPreferences().getBoolean(CommonConstants.TYPE, true);
    }
}
