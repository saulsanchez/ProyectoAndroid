package com.proyectofinal.saul.comunioadmin;

/**
 * Created by Sa�l on 01/06/2015.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends FragmentActivity
{
    private static final long SPLASH_SCREEN_DELAY = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startMainActivity();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }

    private void startMainActivity()
    {
        //Functions.clearLoginPreferences(getApplicationContext());
        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
        this.finish();
    }
}

