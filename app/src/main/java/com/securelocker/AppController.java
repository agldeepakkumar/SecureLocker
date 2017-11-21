package com.securelocker;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.github.omadahealth.lollipin.lib.managers.LockManager;
import com.securelocker.activities.CustomPinActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
        lockManager.enableAppLock(this, CustomPinActivity.class);
        lockManager.getAppLock().setLogoId(R.drawable.security_lock);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath).build());
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
