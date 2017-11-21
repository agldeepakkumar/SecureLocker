package com.securelocker.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.github.omadahealth.lollipin.lib.PinCompatActivity;
import com.github.omadahealth.lollipin.lib.managers.AppLock;
import com.securelocker.R;
import com.securelocker.util.Utils;

public class SplashActivity extends PinCompatActivity {

    public String TAG = SplashActivity.class.getSimpleName();
    SharedPreferences sharedPreferences;
    int SCREEN_TIME = 2000;
    Handler mHandler= new Handler();
    Runnable myRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mHandler.postDelayed(myRunnable = new Runnable() {
            @Override
            public void run() {
                if(!sharedPreferences.getBoolean(Utils.PREFS_LAUNCH_STATUS, false)) {
                    sharedPreferences.edit().putBoolean(Utils.PREFS_LAUNCH_STATUS, true).apply();
                    Intent intent = new Intent(SplashActivity.this, CustomPinActivity.class);
                    intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
                    startActivity(intent);
                    finish();
                } else {
                    if(sharedPreferences.getBoolean(Utils.PREFS_STATUS_PINLOCK, false)) {
                        startActivity(new Intent(SplashActivity.this, CustomPinActivity.class)
                                .putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN));
                        finish();
                    } else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                }
            }
        }, SCREEN_TIME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(myRunnable);
    }
}