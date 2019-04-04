package com.janhoracek.doitwithandroid;

import android.animation.Animator;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;


public class MainActivity extends AppCompatActivity {
    private Button mButton;
    private LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lottie = findViewById(R.id.animation_view);
        lottie.setImageAssetsFolder("images");
        lottie.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                checkFirstRun();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
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
        mButton = (Button) findViewById(R.id.button);
        checkFirstRun();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
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
            Log.d("FRFFS", "This is normal run");
            return;

        } else if (savedVersionCode == DOESNT_EXIST) {

            // TODO Intent na first start
            Log.d("FRFFS", "This is the first run");
            prefs.edit().putInt(USER_LEVEL, 1).apply();
            prefs.edit().putInt(USER_EXPERIENCE, 0).apply();
            prefs.edit().putInt(NEXT_EXPERIENCE, 1000).apply();

        } else if (currentVersionCode > savedVersionCode) {

            // TODO This is an upgrade
        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }
}
