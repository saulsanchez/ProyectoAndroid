package com.proyectofinal.saul.comunioadmin;

/**
 * Created by Saul on 19/05/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends FragmentActivity
{
    private static final long SPLASH_SCREEN_DELAY = 1500;
    private TimerTask task;
    private Timer timer;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        task = new TimerTask() {
            @Override
            public void run() {
                startMainActivity();
            }
        };
        timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }

    private void startMainActivity()
    {
        SharedPreferences preferences = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        if (String.valueOf(preferences.getInt("precioPunto", 0)).equals("0") &&
                String.valueOf(preferences.getInt("precioEstrella", 0)).equals("0") &&
                String.valueOf(preferences.getInt("precioPuesto1", 0)).equals("0") &&
                String.valueOf(preferences.getInt("precioPuesto2", 0)).equals("0") &&
                String.valueOf(preferences.getInt("precioPuesto3", 0)).equals("0")) {
            Intent intent = new Intent(SplashActivity.this, Preferencias.class);
            startActivity(intent);
            this.finish();
        }
        else {
            intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}

