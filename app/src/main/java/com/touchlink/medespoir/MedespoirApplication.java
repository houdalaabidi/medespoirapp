package com.touchlink.medespoir;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.touchlink.medespoir.utils.LocaleManager;

public class MedespoirApplication extends Application {

    public  static LocaleManager localeManager ;
    private static Context mInstance ;

    public static String currentLanguage ;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        mInstance = getApplicationContext() ;
        currentLanguage = localeManager.getLanguage();
    }

    public static synchronized Context getInstance(){
        return  mInstance ;
    }
    public static synchronized String getDeviceLanguage(){
        return currentLanguage ;
    }

    @Override
    protected void attachBaseContext(Context base) {

        localeManager = new LocaleManager(base);
        super.attachBaseContext(localeManager.setLocale(base));
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        localeManager.setLocale(this);
    }
}
