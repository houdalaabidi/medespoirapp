package com.touchlink.medespoir.MVP.views.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.session.SharedPreferencesUserFactory;
import com.touchlink.medespoir.utils.MEConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreenActivity extends AppCompatActivity {



    private static final long SPLASH_SCREEN_TIMER = 5000;
    @SuppressLint("id")
    @BindView(R.id.logo_spash_screen)
    ImageView logo ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        SharedPreferencesUserFactory.initializedPreferences(getBaseContext());

        animateSplashScreen();
        launchSplashScreen();
        checkingDeviceDensity();
        getDeviceID();
    }

    private void getDeviceID() {
        //  Utility.getIDDevice(getApplicationContext());
    }


    private void checkingDeviceDensity() {

        // return 0.75 if it's LDPI
        // return 1.0 if it's MDPI
        // return 1.3 if it's TVDPI
        // return 1.5 if it's HDPI
        // return 2.0 if it's XHDPI
        // return 3.0 if it's XXHDPI
        // return 4.0 if it's XXXHDPI

        float density = getResources().getDisplayMetrics().density;
        Log.e(MEConstants.TAG, " device density = "+density);

    }

    private void animateSplashScreen() {


        Animation animation  = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
        animation.reset();
        logo.clearAnimation();
        logo.startAnimation(animation);
    }


    private void launchSplashScreen() {


        // Waiting 5s before starting the app
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if( SharedPreferencesUserFactory.retrieveUserSessionSettings()){
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } else {
                    startActivity(new Intent(SplashScreenActivity.this, ConnexionActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }

            }
        }, SPLASH_SCREEN_TIMER);

    }

}