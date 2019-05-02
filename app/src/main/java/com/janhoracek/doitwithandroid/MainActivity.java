package com.janhoracek.doitwithandroid;

import android.animation.Animator;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;
import com.janhoracek.doitwithandroid.Application.ApplicationActivity;
import com.janhoracek.doitwithandroid.Application.FirstRunActivity;


public class MainActivity extends AppCompatActivity {
    private LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lottie = findViewById(R.id.animation_view);
        checkFirstRun();
        lottie.setImageAssetsFolder("images");
        lottie.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(MainActivity.this, FirstRunActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    private void checkFirstRun() {

        final String PREFS_NAME = "com.janhoracek.doitwithandroid.SettingsSharedPrefs";
        final String USER_LEVEL = "com.janhoracek.doitwithandroid.USER_LEVEL";
        final String USER_EXPERIENCE = "com.janhoracek.doitwithandroid.USER_EXPERIENCE";
        final String NEXT_EXPERIENCE = "com.janhoracek.doitwithandroid.NEXT_EXPERIENCE";
        final String PREF_VERSION_CODE_KEY = "1";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            // This is just a normal run
            Intent intent = new Intent(MainActivity.this, ApplicationActivity.class);
            startActivity(intent);
            finish();

            Log.d("FRFFS", "This is normal run");
            return;

        } else if (savedVersionCode == DOESNT_EXIST) {

            // First run
            //TODO intent
            Log.d("FRFFS", "This is the first run");
            prefs.edit().putInt(USER_LEVEL, 1).apply();
            prefs.edit().putInt(USER_EXPERIENCE, 0).apply();
            prefs.edit().putInt(NEXT_EXPERIENCE, 1000).apply();

            /*
            Intent intent = new Intent(MainActivity.this, FirstRunActivity.class);
            startActivity(intent);
            finish();*/

        } else if (currentVersionCode > savedVersionCode) {

            Intent intent = new Intent(MainActivity.this, ApplicationActivity.class);
            startActivity(intent);
            finish();
        }

        // Update the shared preferences with the current version code
        //prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }



}


