package com.securelocker;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends AppCompatActivity {

    private static boolean statusActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusActivity = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        statusActivity = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        statusActivity = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        statusActivity = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        statusActivity = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}